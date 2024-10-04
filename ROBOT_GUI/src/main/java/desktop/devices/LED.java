package desktop.devices;

import desktop.devices.interfaces.ILED;

public class LED extends Device implements ILED {

    private String state = "Off";
    private String color;

    public LED() { super(); }
    public LED(String channel) { super(channel); }
    public LED(String channel, String color) { super(channel); this.color = color; }

    public void setColor(String color) { this.color = color; }
    public String getColor() { return this.color; }

    @Override
    public String getState() { return this.state; }

    @Override
    public void switchOn() {
        this.state = "On";
        System.out.println("On " + this.color);
    }

    @Override
    public void switchOff() {
        this.state = "Off";
        System.out.println("Off " + this.color);
    }

    @Override
    public void toggle() {
        if (this.state.equals("On")) switchOff();
        else switchOn();
    }

    @Override
    public void blink(Integer n) {
        for (int i = 0; i < n; i++) {
            switchOn();
            switchOff();
        }
    }
}
