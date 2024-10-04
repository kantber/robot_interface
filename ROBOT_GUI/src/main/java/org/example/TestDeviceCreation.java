package org.example;


import channel.COMModel;
import channel.Connector;
import channel.NoConnectorException;
import desktop.devices.LED;
import desktop.devices.engines.DCMotor;
import desktop.devices.engines.StepperMotor;
import desktop.devices.interfaces.IEngine;
import desktop.devices.interfaces.ILED;

public class TestDeviceCreation {

    public static void main(String[] args) {
        Model1 model1 = new Model1();
        model1.sendByteToStm((byte)0xC0);
        model1.sendByteToPc((byte)0xC0);
        model1.loopBackByte((byte)0xC0);
    }
}