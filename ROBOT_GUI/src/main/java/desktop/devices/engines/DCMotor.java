package desktop.devices.engines;

public class DCMotor extends Engine {

    public DCMotor() { super(); this.type = EngineType.DC; }
    public DCMotor(String channel) {super(channel); this.type = EngineType.DC; }

    @Override
    public void rotateCW() {
        System.out.println("rotate clockwise continuously");
    }

    @Override
    public void rotateCCW() {
        System.out.println("rotate conterclockwise continuously");
    }

    @Override
    public void rotateTo(Double angle) {
        System.out.printf("rotate to %f angel continuously%n", angle);
    }
}
