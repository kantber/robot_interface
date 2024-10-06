package org.example;

import channel.COMModel;
import channel.Connector;
import channel.NoConnectorException;

public class Model1 {

    private final static String LINK = "Состояние соединения: %b%n";

    COMModel  COM;                  //указатель модели канала связи
    Connector pcConnector;          //указатель на коннектор для ПК
    Connector stmConnector;         //указатель на коннектор для STM

    public Model1() {
        COM = new COMModel();  //создание объекта модели канала связи

        try {
            pcConnector = COM.getConnector();   //получение коннектора для ПК
            pcConnector.setName("PC connector");
            stmConnector = COM.getConnector();  //получение коннектора для STM
            stmConnector.setName("STM connector");

            System.out.printf(LINK, pcConnector.isLink());
            pcConnector.openConnector();    //открытие соединения на стороне ПК
            System.out.printf(LINK, stmConnector.isLink());
            stmConnector.openConnector();   //открытие соединения на стороне STM
            System.out.printf(LINK, stmConnector.isLink());

        } catch (NoConnectorException e) {
            System.out.println("There are no connectors available.");
        }
    }

    public void sendByteToStm(Byte b) {
        pcConnector.sendByte(b);
        System.out.printf("Коннектор %s отправил байт %s%n", pcConnector.getName(), String.format("%02X", b));
        Byte receive = stmConnector.getNext();
        System.out.printf("Коннектор %s принял байт %s%n%n", stmConnector.getName(), String.format("%02X", receive));
    }

    public void sendByteToPc(Byte b) {
        stmConnector.sendByte(b);
        System.out.printf("Коннектор %s отправил байт %s%n", stmConnector.getName(), String.format("%02X", b));
        Byte receive = pcConnector.getNext();
        System.out.printf("Коннектор %s принял байт %s%n%n", pcConnector.getName(), String.format("%02X", receive));
    }

    public void loopBackByte(Byte b) {

    }
}
