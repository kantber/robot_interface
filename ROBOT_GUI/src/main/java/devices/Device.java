package devices;

import devices.interfaces.IDevice;

public class Device implements IDevice {
    //class
    private static Integer device_count = 0;  // Общая переменная для всех объектов

    //object
    private final Integer id;
    private String channel;

    public Device() {
        id = ++device_count;
        System.out.printf("Device %d created%n", this.id);
    }

    public Device(String channel) {
        this();
        this.channel = channel;
    }

    @Override
    public void setChannel(String channel) { this.channel = channel; }

    @Override
    public void init() { System.out.printf("Device %d init on channel %s%n", this.id, this.channel); }

    @Override
    public boolean ping() {
        System.out.printf("Device %d exists on channel %s%n", this.id, this.channel);
        return true;
    }
}
