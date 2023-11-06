package com.stars.cli.pattern;

/**
 * 远程控制
 *
 * @author stars
 * @version 1.0
 */
public class RemoteControl {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * 按按钮
     */
    public void pressButton() {
        this.command.execute();
    }
}
