package com.stars.web.controller;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.stars.maker.generator.main.GenerateTemplate;
import com.stars.maker.generator.main.ZipGenerator;
import com.stars.maker.meta.Meta;
import com.stars.maker.meta.MetaValidator;
import com.stars.maker.util.os.OsUtil;
import com.stars.maker.util.os.enums.OsTypeEnum;
import com.stars.web.annotation.AuthCheck;
import com.stars.web.common.BaseResponse;
import com.stars.web.common.DeleteRequest;
import com.stars.web.common.ErrorCode;
import com.stars.web.common.ResultUtils;
import com.stars.web.constant.UserConstant;
import com.stars.web.exception.BusinessException;
import com.stars.web.exception.ThrowUtils;
import com.stars.web.manager.CacheManager;
import com.stars.web.manager.CosManager;
import com.stars.web.model.dto.generator.*;
import com.stars.web.model.entity.Generator;
import com.stars.web.model.entity.User;
import com.stars.web.model.vo.GeneratorVO;
import com.stars.web.service.GeneratorService;
import com.stars.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 代码生成器控制器
 *
 * @author stars
 * @version 1.0.0
 */
@RestController
@RequestMapping("/generator")
@Slf4j
public class GeneratorController {

    @Resource
    private GeneratorService generatorService;

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    @Resource
    private CacheManager cacheManager;

    /**
     * 添加代码生成器
     *
     * @param generatorAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addGenerator(@RequestBody GeneratorAddRequest generatorAddRequest,
                                           HttpServletRequest request) {
        if (generatorAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 属性类型转换
        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorAddRequest, generator);

        List<String> tags = generatorAddRequest.getTags();
        generator.setTags(JSONUtil.toJsonStr(tags));

        Meta.FileConfig fileConfig = generatorAddRequest.getFileConfig();
        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));

        Meta.ModelConfig modelConfig = generatorAddRequest.getModelConfig();
        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));

        // 校验代码生成器属性
        this.generatorService.validGenerator(generator, true);

        // 设置代码生成器的添加人
        User loginUser = this.userService.getLoginUser(request);
        generator.setUserId(loginUser.getId());

        // 校验代码生成器是否成功保存
        boolean result = this.generatorService.save(generator);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

        // 返回添加的代码生成器 ID
        Long newGeneratorId = generator.getId();
        return ResultUtils.success(newGeneratorId);
    }

    /**
     * 删除代码生成器（本人或管理员）
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteGenerator(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 根据 ID 判断待删除的代码生成器是否存在
        Long id = deleteRequest.getId();
        Generator oldGenerator = this.generatorService.getById(id);
        ThrowUtils.throwIf(oldGenerator == null, ErrorCode.NOT_FOUND_ERROR);

        // 权限校验，仅本人或管理员可删除
        User user = this.userService.getLoginUser(request);
        Long userId = user.getId();
        Long oldGeneratorUserId = oldGenerator.getUserId();
        if (!oldGeneratorUserId.equals(userId) && !this.userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 返回待删除的代码生成器是否删除成功
        boolean flag = this.generatorService.removeById(id);
        return ResultUtils.success(flag);
    }

    /**
     * 更新代码生成器（管理员）
     *
     * @param generatorUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateGenerator(@RequestBody GeneratorUpdateRequest generatorUpdateRequest,
                                                 HttpServletRequest request) {
        if (generatorUpdateRequest == null || generatorUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 权限校验，仅管理员可更新
        if (!this.userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 属性类型转换
        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorUpdateRequest, generator);

        List<String> tags = generatorUpdateRequest.getTags();
        generator.setTags(JSONUtil.toJsonStr(tags));

        Meta.FileConfig fileConfig = generatorUpdateRequest.getFileConfig();
        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));

        Meta.ModelConfig modelConfig = generatorUpdateRequest.getModelConfig();
        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));

        // 校验代码生成器属性
        this.generatorService.validGenerator(generator, false);

        // 根据 ID 判断代码生成器是否存在
        Long id = generatorUpdateRequest.getId();
        Generator oldGenerator = this.generatorService.getById(id);
        ThrowUtils.throwIf(oldGenerator == null, ErrorCode.NOT_FOUND_ERROR);

        // 返回是否成功更新代码生成器
        boolean result = this.generatorService.updateById(generator);
        return ResultUtils.success(result);
    }

    /**
     * 根据 ID 获取代码生成器视图
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<GeneratorVO> getGeneratorVOById(Long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 根据 ID 获取未脱敏的代码生成器
        Generator generator = this.generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 获取代码生成器视图
        GeneratorVO generatorVO = this.generatorService.getGeneratorVO(generator, request);
        return ResultUtils.success(generatorVO);
    }

    /**
     * 分页获取代码生成器列表（仅管理员）
     *
     * @param generatorQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Generator>> listGeneratorByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest,
                                                             HttpServletRequest request) {
        if (generatorQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 权限校验，仅管理员可更新
        if (!this.userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 获取分页信息
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();

        // 获取代码生成器分页
        Page<Generator> generatorPage = this.generatorService
                .page(new Page<>(current, size), this.generatorService.getQueryWrapper(generatorQueryRequest));
        return ResultUtils.success(generatorPage);
    }

    /**
     * 分页获取代码生成器视图列表
     *
     * @param generatorQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<GeneratorVO>> listGeneratorVOByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest,
                                                                 HttpServletRequest request) {
        if (generatorQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取分页信息
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();

        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        // 获取代码生成器分页
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Page<Generator> generatorPage = this.generatorService
                .page(new Page<>(current, size), this.generatorService.getQueryWrapper(generatorQueryRequest));
        stopWatch.stop();
        System.out.println("查询代码生成器：" + stopWatch.getTotalTimeMillis());

        // 获取代码生成器视图分页
        stopWatch = new StopWatch();
        stopWatch.start();
        Page<GeneratorVO> generatorVOPage = this.generatorService.getGeneratorVOPage(generatorPage, request);
        stopWatch.stop();
        System.out.println("查询关联数据：" + stopWatch.getTotalTimeMillis());
        return ResultUtils.success(generatorVOPage);
    }

    /**
     * 快速分页获取代码生成器视图列表
     *
     * @param generatorQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo/fast")
    public BaseResponse<Page<GeneratorVO>> listGeneratorVOByPageFast(@RequestBody GeneratorQueryRequest generatorQueryRequest,
                                                                     HttpServletRequest request) {
        if (generatorQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取分页信息
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();

        // 优先从缓存读取
        String cacheKey = this.getPageCacheKey(generatorQueryRequest);
        Object cacheValue = this.cacheManager.get(cacheKey);
        if (cacheValue != null) {
            return ResultUtils.success((Page<GeneratorVO>) cacheValue);
        }

        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        QueryWrapper<Generator> queryWrapper = this.generatorService.getQueryWrapper(generatorQueryRequest);
        queryWrapper.select("id",
                "name",
                "description",
                "tags",
                "picture",
                "status",
                "userId",
                "createTime",
                "updateTime"
        );
        Page<Generator> generatorPage = this.generatorService.page(new Page<>(current, size), queryWrapper);
        Page<GeneratorVO> generatorVOPage = this.generatorService.getGeneratorVOPage(generatorPage, request);

        // 写入缓存
        this.cacheManager.put(cacheKey, generatorVOPage);

        return ResultUtils.success(generatorVOPage);
    }

    /**
     * 分页获取当前用户的代码生成器视图列表
     *
     * @param generatorQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<GeneratorVO>> listMyGeneratorVOByPage(@RequestBody GeneratorQueryRequest generatorQueryRequest,
                                                                   HttpServletRequest request) {
        if (generatorQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 为代码生成器查询请求设置当前登录用户 ID
        User loginUser = this.userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        generatorQueryRequest.setUserId(loginUserId);

        // 获取分页信息
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();

        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        // 获取代码生成器分页
        Page<Generator> generatorPage = this.generatorService
                .page(new Page<>(current, size), this.generatorService.getQueryWrapper(generatorQueryRequest));

        // 获取代码生成器视图分页
        Page<GeneratorVO> generatorVOPage = this.generatorService.getGeneratorVOPage(generatorPage, request);
        return ResultUtils.success(generatorVOPage);
    }

    /**
     * 编辑代码生成器（本人或管理员）
     *
     * @param generatorEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editGenerator(@RequestBody GeneratorEditRequest generatorEditRequest,
                                               HttpServletRequest request) {
        if (generatorEditRequest == null || generatorEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 属性类型转换
        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorEditRequest, generator);

        List<String> tags = generatorEditRequest.getTags();
        generator.setTags(JSONUtil.toJsonStr(tags));

        Meta.FileConfig fileConfig = generatorEditRequest.getFileConfig();
        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));

        Meta.ModelConfig modelConfig = generatorEditRequest.getModelConfig();
        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));

        // 参数校验
        this.generatorService.validGenerator(generator, false);

        // 根据 ID 判断待编辑的代码生成器是否存在
        Long id = generatorEditRequest.getId();
        Generator oldGenerator = this.generatorService.getById(id);
        ThrowUtils.throwIf(oldGenerator == null, ErrorCode.NOT_FOUND_ERROR);

        // 权限校验，仅本人或管理员可编辑
        User loginUser = this.userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        Long editGeneratorUserId = oldGenerator.getUserId();
        if (!editGeneratorUserId.equals(loginUserId) && !this.userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 返回是否成功编辑代码生成器
        boolean result = this.generatorService.updateById(generator);

        // 清理缓存
        if (result) {
            String cacheFileDir = this.getCacheFileDir(id);
            FileUtil.del(cacheFileDir);
        }

        return ResultUtils.success(result);
    }

    /**
     * 根据 ID 下载代码生成器
     *
     * @param id
     * @return
     */
    @GetMapping("/download")
    public void downloadGeneratorById(long id, HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取当前登录用户
        User loginUser = this.userService.getLoginUser(request);

        // 根据 ID 获取代码生成器
        Generator generator = this.generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 获取产物包路径
        String filepath = generator.getDistPath();
        if (StrUtil.isBlank(filepath)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "代码生成器的产物包不存在");
        }

        // 追踪事件
        this.log.info("用户 {} 下载了 {}", loginUser, filepath);

        // 设置响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + filepath);

        // 优先从缓存读取
        String zipFilePath = this.getCacheFilePath(id, filepath);
        if (FileUtil.exist(zipFilePath)) {
            // 写入响应
            Files.copy(Paths.get(zipFilePath), response.getOutputStream());
            return;
        }

        // COS 对象输入流
        COSObjectInputStream cosObjectInput = null;
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            COSObject cosObject = this.cosManager.getObject(filepath);
            cosObjectInput = cosObject.getObjectContent();
            // 处理下载到的流
            byte[] bytes = IOUtils.toByteArray(cosObjectInput);
            stopWatch.stop();
            System.out.println("下载耗时：" + stopWatch.getTotalTimeMillis());
            // 写入响应
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            this.log.error("file download error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码生成器下载失败");
        } finally {
            if (cosObjectInput != null) {
                cosObjectInput.close();
            }
        }
    }

    /**
     * 使用代码生成器
     *
     * @param generatorUseRequest
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/use")
    public void useGenerator(@RequestBody GeneratorUseRequest generatorUseRequest,
                             HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取用户输入的请求参数
        Long id = generatorUseRequest.getId();
        Map<String, Object> dataModel = generatorUseRequest.getDataModel();

        // 获取当前登录用户
        User loginUser = this.userService.getLoginUser(request);
        this.log.info("userId = {} 使用了生成器 id = {}", loginUser.getId(), id);

        // 根据 ID 获取代码生成器
        Generator generator = this.generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 获取生成器的存储路径
        String distPath = generator.getDistPath();
        if (StrUtil.isBlank(distPath)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "代码生成器的产物包不存在");
        }

        // 从 COS 下载代码生成器的压缩包

        // 定义独立的工作空间
        String projectPath = System.getProperty("user.dir");
        projectPath = projectPath.replaceAll("\\\\", "/");
        String tempDirPath = String.format("%s/.temp/use/%s", projectPath, id);
        String zipFilePath = tempDirPath + "/dist.zip";

        // 压缩文件路径不存在则新建
        if (!FileUtil.exist(zipFilePath)) {
            FileUtil.touch(zipFilePath);
        }

        // 使用文件缓存
        String cacheFilePath = getCacheFilePath(id, distPath);
        Path cacheFilePathObj = Paths.get(cacheFilePath);
        Path zipFilePathObj = Paths.get(zipFilePath);

        if (!FileUtil.exist(zipFilePath)) {
            // 有缓存，复制文件
            if (FileUtil.exist(cacheFilePath)) {
                Files.copy(cacheFilePathObj, zipFilePathObj);
            } else {
                // 没有缓存，从对象存储下载文件
                FileUtil.touch(zipFilePath);
                // 从 COS 上下载代码生成器
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                try {
                    this.cosManager.download(distPath, zipFilePath);
                } catch (Exception e) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码生成器下载失败");
                }
                stopWatch.stop();
                System.out.println("下载耗时：" + stopWatch.getTotalTimeMillis());
                // 写文件缓存
                File parentFile = cacheFilePathObj.toFile().getParentFile();
                if (!FileUtil.exist(parentFile)) {
                    FileUtil.mkdir(parentFile);
                }
                Files.copy(zipFilePathObj, cacheFilePathObj);
            }
        }

        // 解压压缩包，得到脚本文件
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File unzipDistDir = ZipUtil.unzip(zipFilePath);
        stopWatch.stop();
        System.out.println("解压耗时：" + stopWatch.getTotalTimeMillis());

        // 将用户输入的参数写到 JSON 文件中
        stopWatch = new StopWatch();
        stopWatch.start();
        String dataModelFilePath = tempDirPath + "/dataModel.json";
        String jsonStr = JSONUtil.toJsonStr(dataModel);
        FileUtil.writeUtf8String(jsonStr, dataModelFilePath);
        stopWatch.stop();
        System.out.println("写数据文件：" + stopWatch.getTotalTimeMillis());

        // 执行脚本
        // 获取当前操作系统类型
        String osType = OsUtil.OS_TYPE;
        // 找到脚本文件所在路径
        File scriptFile = null;
        if (OsTypeEnum.WINDOWS.getValue().equals(osType)) {
            scriptFile = FileUtil.loopFiles(unzipDistDir, 2, null)
                    .stream()
                    .filter(file -> file.isFile()
                            && "generator.bat".equals(file.getName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
        } else if (OsTypeEnum.LINUX.getValue().equals(osType)) {
            scriptFile = FileUtil.loopFiles(unzipDistDir, 2, null)
                    .stream()
                    .filter(file -> file.isFile()
                            && "generator".equals(file.getName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            // 添加可执行权限
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(scriptFile.toPath(), permissions);
        } else {
            scriptFile = FileUtil.loopFiles(unzipDistDir, 2, null)
                    .stream()
                    .filter(file -> file.isFile()
                            && "generator".equals(file.getName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            // 添加可执行权限
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(scriptFile.toPath(), permissions);
        }

        // 构造命令
        File scriptDir = scriptFile.getParentFile();
        // 注意，如果是 Mac / Linux 系统，要用 "./generator"
        String scriptAbsolutePath = scriptFile.getAbsolutePath().replace("\\", "/");
        String[] commands = new String[]{scriptAbsolutePath, "json-generate", "--file=" + dataModelFilePath};

        // 拆分，重点
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(scriptDir);

        try {
            stopWatch = new StopWatch();
            stopWatch.start();
            Process process = processBuilder.start();

            // 读取命令的输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();
            System.out.println("命令执行结束，退出码：" + exitCode);
            stopWatch.stop();
            System.out.println("执行脚本：" + stopWatch.getTotalTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "执行代码生成器脚本错误");
        }

        // 压缩得到的生成结果，返回给前端
        stopWatch = new StopWatch();
        stopWatch.start();
        String generatedPath = scriptDir.getAbsolutePath() + "/generated";
        String resultPath = tempDirPath + "/result.zip";
        File resultFile = ZipUtil.zip(generatedPath, resultPath);
        stopWatch.stop();
        System.out.println("压缩结果：" + stopWatch.getTotalTimeMillis());

        // 设置响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + resultFile.getName());
        Files.copy(resultFile.toPath(), response.getOutputStream());

        // 清理文件，异步
        CompletableFuture.runAsync(() -> {
            FileUtil.del(tempDirPath);
        });
    }

    /**
     * 制作代码生成器
     *
     * @param generatorMakeRequest
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/make")
    public void makeGenerator(@RequestBody GeneratorMakeRequest generatorMakeRequest,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1) 获取输入参数
        Meta meta = generatorMakeRequest.getMeta();
        String zipFilePath = generatorMakeRequest.getZipFilePath();

        // 获取当前登录用户
        User loginUser = this.userService.getLoginUser(request);
        this.log.info("userId = {} 在线制作代码生成器", loginUser.getId());

        // 2) 创建独立的工作空间，从 COS 上下载压缩包到服务器本地
        String projectPath = System.getProperty("user.dir");
        String id = IdUtil.getSnowflakeNextId() + RandomUtil.randomString(6);
        String tempDirPath = String.format("%s/.temp/make/%s", projectPath, id);
        String localZipFilePath = tempDirPath + "/project.zip";

        // 校验本地压缩文件路径是否存在，不存在则创建
        if (!FileUtil.exist(localZipFilePath)) {
            FileUtil.touch(localZipFilePath);
        }

        // 下载文件
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            this.cosManager.download(zipFilePath, localZipFilePath);
            stopWatch.stop();
            System.out.println("下载文件：" + stopWatch.getTotalTimeMillis());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "压缩包下载失败");
        }

        // 3）解压，得到项目模板文件
        File unzipDistDir = ZipUtil.unzip(localZipFilePath);

        // 4）构造 meta 对象和代码生成器的输出路径
        String sourceRootPath = unzipDistDir.getAbsolutePath();
        meta.getFileConfig().setSourceRootPath(sourceRootPath);
        // 校验和处理默认值
        MetaValidator.doValidAndFill(meta);
        String outputPath = tempDirPath + "/generated/" + meta.getName();

        // 5）调用 maker 方法制作代码生成器
        GenerateTemplate generateTemplate = new ZipGenerator();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            generateTemplate.doGenerate(meta, outputPath);
            stopWatch.stop();
            System.out.println("制作耗时：" + stopWatch.getTotalTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码生成器制作失败");
        }

        // 6）下载制作好的代码生成器压缩包
        String suffix = "-dist.zip";
        String zipFileName = meta.getName() + suffix;
        // 代码生成器压缩包的绝对路径
        String distZipFilePath = outputPath + suffix;

        // 设置响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipFileName);
        Files.copy(Paths.get(distZipFilePath), response.getOutputStream());

        // 7）清理工作空间的文件
        CompletableFuture.runAsync(() -> {
            FileUtil.del(tempDirPath);
        });
    }

    /**
     * 缓存代码生成器
     *
     * @param generatorCacheRequest
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("/cache")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public void cacheGenerator(@RequestBody GeneratorCacheRequest generatorCacheRequest, HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        if (generatorCacheRequest == null || generatorCacheRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long id = generatorCacheRequest.getId();
        Generator generator = this.generatorService.getById(id);
        if (generator == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        String distPath = generator.getDistPath();
        if (StrUtil.isBlank(distPath)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "代码生成器的产物包不存在");
        }

        String zipFilePath = this.getCacheFilePath(id, distPath);

        try {
            this.cosManager.download(distPath, zipFilePath);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码生成器下载失败");
        }
    }

    /**
     * 获取缓存文件路径
     *
     * @param id
     * @param distPath
     * @return
     */
    public String getCacheFilePath(long id, String distPath) {
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = String.format("%s/.temp/cache/%s", projectPath, id);
        String zipFilePath = tempDirPath + "/" + distPath;
        return zipFilePath;
    }

    /**
     * 获取分页缓存 KEY
     *
     * @param generatorQueryRequest
     * @return
     */
    public static String getPageCacheKey(GeneratorQueryRequest generatorQueryRequest) {
        String jsonStr = JSONUtil.toJsonStr(generatorQueryRequest);
        // 请求参数编码
        String base64 = Base64Encoder.encode(jsonStr);
        String key = "generator:page:" + base64;
        return key;
    }

    /**
     * 获取缓存文件所在的目录
     *
     * @param id 生成器 id
     * @return
     */
    public String getCacheFileDir(long id) {
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = String.format("%s/.temp/cache/%s", projectPath, id);
        return tempDirPath;
    }
}
