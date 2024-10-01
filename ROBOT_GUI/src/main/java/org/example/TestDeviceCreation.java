package org.example;


import devices.Device;

public class TestDeviceCreation {

    private final static String CHANNEL = "COM1";

    public static void main(String[] args) {
        Device d1 = new Device(CHANNEL);
        Device d2 = new Device(CHANNEL);
        Device d3 = new Device(CHANNEL);
        Device d4 = new Device(CHANNEL);

        d1.init();
        d2.init();
        d3.init();
        d4.init();

        d1.ping();
        d2.ping();
        d3.ping();
        d4.ping();
    }
}