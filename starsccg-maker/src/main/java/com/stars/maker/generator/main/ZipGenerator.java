package com.stars.maker.generator.main;

/**
 * 生成器压缩包
 * 分支流程
 * 主流程固化，分支流程扩展
 *
 * @author stars
 * @version 1.0.0
 */
public class ZipGenerator extends GenerateTemplate {

    /**
     * 构建产物
     *
     * @param outputPath
     * @param sourceCopyDestPath
     * @param jarPath
     * @param shellOutputFilePath
     * @return
     */
    @Override
    protected String buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath) {
        String distPath = super.buildDist(outputPath, sourceCopyDestPath, jarPath, shellOutputFilePath);
        return super.buildZip(distPath);
    }
}
