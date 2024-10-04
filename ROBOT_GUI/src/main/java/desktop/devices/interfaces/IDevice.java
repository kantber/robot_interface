package desktop.devices.interfaces;

import desktop.devices.Pin;

import java.util.List;

public interface IDevice {
    public Integer getId();
    public List<Pin> getPins();
    public void addPin(Pin pin);
    public void setChannel(String channel);
    public void init();
    public boolean ping();

}
