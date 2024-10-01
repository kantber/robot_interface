package devices.interfaces;

public interface ILED extends IDevice {
    public void init();
    public void switchOn();
    public void switchOff();
    public void toggle();
}
