package com.stars.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.stars.generator.FileGenerator;
import com.stars.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

/**
 * 生成命令
 *
 * @author stars
 * @version 1.0
 */
@Command(name = "generate", description = "生成代码", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {

    @Option(names = {"-l", "--loop"}, arity = "0..1", description = "是否循环", interactive = true, echo = true)
    private boolean loop = true;

    @Option(names = {"-a", "--author"}, arity = "0..1", description = "作者注释", interactive = true, echo = true)
    private String author = "stars";

    @Option(names = {"-o", "--outputText"}, arity = "0..1", description = "输出信息", interactive = true, echo = true)
    private String outputText = "sum = ";

    @Override
    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        FileGenerator.doGenerate(dataModel);
        return 0;
    }
}
