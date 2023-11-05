package com.stars.cli.pattern;

/**
 * 打开命令
 *
 * @author stars
 * @version 1.0
 */
public class TurnOnCommand implements Command {

    private Device device;

    public TurnOnCommand(Device device) {
        this.device = device;
    }

    /**
     * 执行命令
     */
    public void execute() {
        device.turnOn();
    }
}
