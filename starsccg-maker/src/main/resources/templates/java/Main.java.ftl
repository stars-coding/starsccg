package ${basePackage};

import ${basePackage}.cli.CommandExecutor;

/**
 * 主类（面向用户）
 *
<#if author??> * @author ${author}</#if>
<#if version??> * @version ${version}</#if>
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
//        args = new String[]{"generate", "-l"};
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}
