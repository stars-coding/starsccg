package com.stars.maker.generator.main;

import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * 核心生成器
 * 分支流程
 * 主流程固化，分支流程扩展
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

    /**
     * 构建产物
     *
     * @param outputPath
     * @param sourceCopyDestPath
     * @param jarPath
     * @param shellOutputFilePath
     */
    @Override
    protected String buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath) {
        System.out.println("没有生成精简包");
        return "";
    }
}
