package devices.interfaces;

public interface IDevice {
    public void setChannel(String channel);
    public void init();
    public boolean ping();

}
