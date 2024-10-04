package org.example;


import channel.COMModel;
import desktop.devices.LED;
import desktop.devices.engines.DCMotor;
import desktop.devices.engines.StepperMotor;
import desktop.devices.interfaces.IEngine;
import desktop.devices.interfaces.ILED;

public class TestDeviceCreation {

    private final static String CHANNEL = "COM1";

    public static void main(String[] args) {
        COMModel com1 = new COMModel();
        COMModel com2 = new COMModel();
        com1.open();
        com1.close();
        com2.open();
        com2.close();


        DCMotor dc1 = new DCMotor("COM1");
        dc1.setSpeed(20.0);
        dc1.setAccelerate(10.0);
        StepperMotor step1 = new StepperMotor("COM1", 1000);
        step1.setSpeed(20.0);
        step1.setAccelerate(10.0);
        LED led_red = new LED("COM1", "Red");
        LED led_green = new LED("COM1", "Green");

        trajectory1(dc1, led_red);
        trajectory2(dc1, led_green);
        trajectory1(step1, led_green);
        trajectory2(step1, led_red);
    }

    public static void trajectory1(IEngine engine, ILED led) {
        System.out.println("Trajectory 1:");
        led.blink(1);
        System.out.printf("Go to the 1-st point for %d%n", engine.getId());
        led.switchOn();
        engine.rotateCW();
        System.out.printf("Go to the 2-nd point for %d%n", engine.getId());
        led.switchOff();
        engine.rotateCCW();
        System.out.printf("Go to the 3-d point for %d%n", engine.getId());
        led.toggle();
        engine.rotateTo(128.0);
        System.out.println("Trajectory 1 end");
        led.blink(2);
        System.out.println();
    }

    public static void trajectory2(IEngine engine, ILED led) {
        System.out.println("Trajectory 2:");
        led.blink(1);
        System.out.printf("Go to the 3-st point for %d%n", engine.getId());
        led.switchOn();
        engine.rotateCW();
        System.out.printf("Go to the 2-nd point for %d%n", engine.getId());
        led.switchOff();
        engine.rotateCCW();
        System.out.printf("Go to the 1-d point for %d%n", engine.getId());
        led.toggle();
        engine.rotateTo(128.0);
        System.out.println("Trajectory 2 end");
        led.blink(2);
        System.out.println();
    }
}