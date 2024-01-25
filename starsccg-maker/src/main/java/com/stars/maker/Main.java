package com.stars.maker;

import com.stars.maker.generator.main.GenerateTemplate;
import com.stars.maker.generator.main.ZipGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * 主类（面向用户）
 *
 * @author stars
 * @version 1.0.0
 */
public class Main {

    /**
     * 入口函数
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException, TemplateException, IOException {
//        args = new String[]{"generate", "-l", "-a", "-o"};
//        args = new String[]{"config"};
//        args = new String[]{"list"};
//        CommandExecutor commandExecutor = new CommandExecutor();
//        commandExecutor.doExecute(args);
//        MainGenerator mainGenerator = new MainGenerator();
//        mainGenerator.doGenerate();
        GenerateTemplate generateTemplate = new ZipGenerator();
        generateTemplate.doGenerate();
    }
}
