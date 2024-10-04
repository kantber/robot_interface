package desktop.devices.engines;

import lombok.Getter;
import lombok.Setter;

import static desktop.devices.engines.EngineType.STEPPER;

public class StepperMotor extends Engine {

    @Getter
    @Setter
    private final Integer steps;

    @Getter
    private Double SPS; //velocity in steps

    @Getter
    private Double APS; //accelerate in steps

    public StepperMotor(Integer steps) { super(); this.type = STEPPER; this.steps = steps; }
    public StepperMotor(String channel, Integer steps) {super(channel); this.type = STEPPER; this.steps = steps; }

    @Override
    public void setSpeed(Double v) {
        System.out.println("calculate SPS from v");
        this.SPS = v/3.14;
    }

    @Override
    public void setAccelerate(Double a) {
        System.out.println("calculate APS from a");
        this.APS = a/3.14;
    }

    @Override
    public void rotateCW() {
        System.out.printf("rotate clockwise by step%n   SPS = %f%n   APS = %f%n", this.SPS, this.APS);
    }

    @Override
    public void rotateCCW() {
        System.out.printf("rotate conterclockwise by step%n   SPS = %f%n   APS = %f%n", this.SPS, this.APS);
    }

    @Override
    public void rotateTo(Double angle) {
        Double steps = angle / 360 * this.steps;
        System.out.printf("step %f steps", steps);
    }
}
