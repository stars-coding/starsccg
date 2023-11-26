package com.stars.maker.generator.main;

import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * 核心生成器
 *
 * @author stars
 * @version 1.0.0
 */
public class MainGenerator extends GenerateTemplate {

    /**
     * 入口函数
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, TemplateException, InterruptedException {
        MainGenerator mainGenerator = new MainGenerator();
        mainGenerator.doGenerate();
    }
}
