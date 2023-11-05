package com.stars.cli.pattern;

/**
 * 关闭命令
 *
 * @author stars
 * @version 1.0
 */
public class TurnOffCommand implements Command {

    private Device device;

    public TurnOffCommand(Device device) {
        this.device = device;
    }

    /**
     * 执行命令
     */
    public void execute() {
        device.turnOff();
    }
}
