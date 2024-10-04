package desktop.devices.interfaces;

public interface ILED extends IDevice {
    public void init();
    public String getState();
    public void switchOn();
    public void switchOff();
    public void toggle();
    public void blink(Integer n);
}
