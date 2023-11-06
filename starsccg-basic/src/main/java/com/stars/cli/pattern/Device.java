package com.stars.cli.pattern;

/**
 * 设备
 *
 * @author stars
 * @version 1.0
 */
public class Device {

    private String name;

    public Device(String name) {
        this.name = name;
    }

    /**
     * 打开
     */
    public void turnOn() {
        System.out.println(this.name + " 设备打开");
    }

    /**
     * 关闭
     */
    public void turnOff() {
        System.out.println(this.name + " 设备关闭");
    }
}
