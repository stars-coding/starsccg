package com.stars.maker.cli;

import com.stars.maker.cli.command.GenerateCommand;
import com.stars.maker.cli.command.ListCommand;
import com.stars.maker.cli.command.ConfigCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * 命令执行器
 *
 * @author stars
 * @version 1.0
 */
@Command(name = "starsccg", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable {

    private final CommandLine commandLine;

    {
        this.commandLine = new CommandLine(this)
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand());
    }

    @Override
    public void run() {
        // 不输入子命令时，给出提示
        System.out.println("请输入具体命令，或者输入 --help 查看命令提示");
    }

    /**
     * 执行命令
     *
     * @param args 参数
     * @return
     */
    public Integer doExecute(String[] args) {
        return this.commandLine.execute(args);
    }
}
