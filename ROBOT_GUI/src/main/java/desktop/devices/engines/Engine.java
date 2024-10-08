package desktop.devices.engines;

import desktop.devices.Device;
import desktop.devices.interfaces.IEngine;
import lombok.Getter;

public abstract class Engine extends Device implements IEngine {

    Double v;
    Double a;

    @Getter
    EngineType type;

    public Engine() {
        super();
        v = 0.0;
        a = 0.0;
    }

    public Engine(String channel) {
        this.setChannel(channel);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public boolean ping() {
        return super.ping();
    }

    @Override
    public void setSpeed(Double v) { this.v = v; }

    @Override
    public void setAccelerate(Double a) { this.a = a; }

    @Override
    public abstract void rotateCW();

    @Override
    public abstract void rotateCCW();

    @Override
    public abstract void rotateTo(Double angle);
}
