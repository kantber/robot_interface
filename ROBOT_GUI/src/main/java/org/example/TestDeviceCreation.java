package org.example;




public class TestDeviceCreation {

    String linkReq = "C03C1201FFFF";
    String linkRes = "CO3D0201FDFD";

    public static void main(String[] args) {
        Model1 model1 = new Model1();
        model1.sendByteToStm((byte)0xC0);
        model1.sendByteToStm((byte)0x3C);
        model1.sendByteToPc((byte)0xC0);
        model1.sendByteToPc((byte)0x3D);
        model1.loopBackByte((byte)0xC0);
    }
}