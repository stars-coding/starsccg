package com.stars.maker.generator;

import cn.hutool.core.io.FileUtil;
import com.stars.maker.util.os.OsUtil;
import com.stars.maker.util.os.enums.OsTypeEnum;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * 脚本文件生成器
 *
 * @author stars
 * @version 1.0.0
 */
public class ScriptGenerator {

    /**
     * 生成脚本文件
     *
     * @param outputPath
     * @param jarPath
     * @throws IOException
     */
    public static void doGenerate(String outputPath, String jarPath) throws IOException {
        // 获取当前操作系统类型
        String osType = OsUtil.OS_TYPE;

        // 声明字符流
        StringBuilder stringBuilder = new StringBuilder();

        if (OsTypeEnum.WINDOWS.getValue().equals(osType)) {
            // 拼接字符串
            stringBuilder.append("@echo off").append("\n");
            stringBuilder.append(String.format("java -jar %s %%*", jarPath)).append("\n");
            // 将字符串写入文件
            FileUtil.writeBytes(stringBuilder.toString().getBytes(StandardCharsets.UTF_8), outputPath + ".bat");
        } else if (OsTypeEnum.LINUX.getValue().equals(osType)) {
            // 拼接字符串
            stringBuilder.append("#!/bin/bash").append("\n");
            stringBuilder.append(String.format("java -jar %s \"$@\"", jarPath)).append("\n");
            // 将字符串写入文件
            FileUtil.writeBytes(stringBuilder.toString().getBytes(StandardCharsets.UTF_8), outputPath);
            // Linux 添加可执行权限
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(Paths.get(outputPath), permissions);
        } else {
            // 拼接字符串
            stringBuilder.append("#!/bin/bash").append("\n");
            stringBuilder.append(String.format("java -jar %s \"$@\"", jarPath)).append("\n");
            // 将字符串写入文件
            FileUtil.writeBytes(stringBuilder.toString().getBytes(StandardCharsets.UTF_8), outputPath);
            // Linux 添加可执行权限
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(Paths.get(outputPath), permissions);
        }
    }
}
