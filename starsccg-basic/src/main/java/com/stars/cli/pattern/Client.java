package com.stars.cli.pattern;

/**
 * 客户端
 *
 * @author stars
 * @version 1.0
 */
public class Client {

    /**
     * 入口函数
     *
     * @param args
     */
    public static void main(String[] args) {
        // 创建接收者对象
        Device xiaomi = new Device("XIAOMI");
        Device huawei = new Device("HUAWEI");

        // 创建具体命令对象，可以绑定不同设备
        TurnOnCommand turnOn = new TurnOnCommand(xiaomi);
        TurnOffCommand turnOff = new TurnOffCommand(huawei);

        // 创建调用者
        RemoteControl remote = new RemoteControl();

        // 执行命令
        remote.setCommand(turnOn);
        remote.pressButton();

        remote.setCommand(turnOff);
        remote.pressButton();
    }
}
