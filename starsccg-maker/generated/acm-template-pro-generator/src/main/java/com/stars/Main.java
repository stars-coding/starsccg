package com.stars;

import com.stars.cli.CommandExecutor;

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
    public static void main(String[] args) {
//        args = new String[]{"generate", "-l", "-a", "-o"};
//        args = new String[]{"config"};
//        args = new String[]{"list"};
        args = new String[]{"generate", "-l"};
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}
