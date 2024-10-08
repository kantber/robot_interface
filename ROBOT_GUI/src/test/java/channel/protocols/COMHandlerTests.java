package channel.protocols;

import channel.COMHandler;
import channel.COMModel;
import org.junit.Test;

public class COMHandlerTests {
    static final String linkReq = "C0 3C 01 FF C2";
    static final String linkOk  = "C0 3D 01 FD C1";
    static final String linkErr = "C0 3D 01 FC C0";

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
            Thread.sleep(300);  // Даем время на отправку сообщения
            stmCOMHandler.sendMessage(linkOk);
            Thread.sleep(3000); // Даем поработать модели 3 секунды
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        pcCOMHandler.requestStop();     // Остановка обработчика COM порта ПК
        stmCOMHandler.requestStop();    // Остановка обработчика COM порта STM
    }
}
