package desktop.devices;

import desktop.devices.interfaces.IDevice;

import java.util.List;

public abstract class Device implements IDevice {
    //class
    private static Integer device_count = 0;  // Общая переменная для всех объектов

    //object
    List<Pin> pins;


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
    public Integer getId() { return this.id; }

    @Override
    public List<Pin> getPins() {
        return this.pins;
    }

    @Override
    public void addPin(Pin pin) {
        this.pins.add(pin);
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
