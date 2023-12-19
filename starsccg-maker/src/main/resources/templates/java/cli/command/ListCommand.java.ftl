package ${basePackage}.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine.Command;

import java.io.File;
import java.util.List;

/**
 * 查看文件列表命令
 *
<#if author??> * @author ${author}</#if>
<#if version??> * @version ${version}</#if>
 */
@Command(name = "list", description = "查看文件列表", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {

    @Override
    public void run() {
        // 输入路径
        String inputPath = "<#if fileConfig.inputRootPath??>${fileConfig.inputRootPath}</#if>";
        List<File> files = FileUtil.loopFiles(inputPath);
        for (File file : files) {
            System.out.println(file);
        }
    }
}
