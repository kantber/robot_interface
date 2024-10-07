package org.example;


import channel.COMModel;

import java.util.List;

public class TestDeviceCreation {

    static final String linkReq = "C0 3C 12 01 FF FF";
    static final String linkOk  = "CO 3D 02 01 FD FD";
    static final String linkErr = "C0 3d 02 01 FC FC";

    public static void main(String[] args) {
        Model1 model1 = new Model1();
        List<Byte> linkReqB = COMModel.mesToFrame(linkReq);
        System.out.println(linkReqB);
        String linkReqS = COMModel.frameToMes(linkReqB);
        System.out.println(linkReqS);
        System.out.println(COMModel.mesToFrame(linkReq));
        model1.sendByteToStm((byte)0xC0);
        model1.sendByteToStm((byte)0x3C);
        model1.sendByteToPc((byte)0xC0);
        model1.sendByteToPc((byte)0x3D);
        model1.loopBackByte((byte)0xC0);
    }
}