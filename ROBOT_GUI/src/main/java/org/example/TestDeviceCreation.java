package org.example;


import channel.COMHandler;
import channel.COMModel;
import org.junit.Test;


public class TestDeviceCreation {
    static final String linkReq = "C0 3C 12 01 FF FF";
    static final String linkOk  = "CO 3D 02 01 FD FD";
    static final String linkErr = "C0 3d 02 01 FC FC";

    @Test
    public void testChannelExchange() {
        COMModel com = new COMModel();  // Создание модели COM порта
        COMHandler pcCOMHandler  = new COMHandler(com, "PC");   // Создание обработчика COM порта на стороне ПК
        COMHandler stmCOMHandler = new COMHandler(com, "STM");  // Создание обработчика COM порта на стороне STM

        Thread PCCOMThread  = new Thread(pcCOMHandler);   // Добавляем обработчик COM порта ПК в отдельный поток
        Thread STMCOMThread = new Thread(stmCOMHandler);  // Добавляем обработчик COM порта STM в отдельный поток

        PCCOMThread.start();    // Запуск обработчика COM порта ПК
        STMCOMThread.start();   // Запуск обработчика COM порта STM

        try {
            Thread.sleep(300);  // Даем время на инициализацию канала связи
            pcCOMHandler.sendMessage(linkReq);
            Thread.sleep(3000); // Даем поработать модели 5 секунд
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        pcCOMHandler.requestStop();     // Остановка обработчика COM порта ПК
        stmCOMHandler.requestStop();    // Остановка обработчика COM порта STM
    }

}