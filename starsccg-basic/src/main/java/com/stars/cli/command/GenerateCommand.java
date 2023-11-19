package com.stars.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.stars.generator.MainGenerator;
import com.stars.model.MainTemplateConfig;
import lombok.Data;

import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

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

    @Option(names = {"-a", "--author"}, arity = "0..1", description = "作者名称", interactive = true, echo = true)
    private String author = "stars";

    @Option(names = {"-o", "--outputText"}, arity = "0..1", description = "输出文本", interactive = true, echo = true)
    private String outputText = "sum = ";

    @Override
    public Integer call() throws Exception {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        BeanUtil.copyProperties(this, mainTemplateConfig);
        System.out.println("配置信息：" + mainTemplateConfig);
        MainGenerator.doGenerate(mainTemplateConfig);
        return 0;
    }
}
