package com.stars.cli.command;

import cn.hutool.core.util.ReflectUtil;
import com.stars.model.DataModel;
import picocli.CommandLine.Command;

import java.lang.reflect.Field;

/**
 * 查看参数信息命令
 *
 * @author stars
 * @version 1.0.0
 */
@Command(name = "config", description = "查看参数信息", mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable {

    @Override
    public void run() {
        // 实现 config 命令的逻辑
        System.out.println("查看参数信息");

        // 利用 Hutool 工具列获取字段属性
        Field[] fields = ReflectUtil.getFields(DataModel.class);

        // 遍历并打印每个字段的信息
        for (Field field : fields) {
            System.out.println("字段名称：" + field.getName());
            System.out.println("字段类型：" + field.getType());
            System.out.println("---");
        }
    }
}
