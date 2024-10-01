package devices.interfaces;

public interface IEngine extends IDevice {
    public void init();
    public void setSpeed(Double v);
    public void setAccelerate(Double a);

    // постоянное вращение по часовой стрелке
    public void rotateCW();

    // постоянное ващение против часовой стрелки
    public void rotateCCW();

    // вращение на определенный угол
    public void rotateTo(Double angle);
}
