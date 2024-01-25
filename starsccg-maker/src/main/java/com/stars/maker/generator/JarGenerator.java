package com.stars.maker.generator;

import com.stars.maker.util.os.OsUtil;
import com.stars.maker.util.os.enums.OsTypeEnum;

import java.io.*;
import java.util.Map;

/**
 * jar 生成器
 *
 * @author stars
 * @version 1.0.0
 */
public class JarGenerator {

    /**
     * 生成 jar 包
     *
     * @param projectDir
     * @throws IOException
     * @throws InterruptedException
     */
    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        // 获取当前操作系统类型
        String osType = OsUtil.OS_TYPE;

        // 声明 Maven 命令
        String mavenCommand = null;

        // 清理之前构建的 jar 包再执行打包
        // 注意不同操作系统，执行的命令不同
        if (OsTypeEnum.WINDOWS.getValue().equals(osType)) {
            mavenCommand = "mvn.cmd clean package -DskipTests=true";
        } else if (OsTypeEnum.LINUX.getValue().equals(osType)) {
            mavenCommand = "mvn clean package -DskipTests=true";
        } else {
            mavenCommand = "mvn clean package -DskipTests=true";
        }

        // 依据空格拆分命名，必须
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        // 指定路径执行命名
        processBuilder.directory(new File(projectDir));
        Map<String, String> environment = processBuilder.environment();
        System.out.println(environment);
        // 相当于打开一个终端，执行命令
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
    }
}
