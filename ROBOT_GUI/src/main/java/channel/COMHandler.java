package channel;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;


public class COMHandler implements Runnable {

    private COMModel com;           // Модель COM порта

    private Connector connector;    // Коннектор для подключения к модели COM порта

    private String name;            // Имя устройства

    private ConcurrentLinkedDeque<Byte> rxBuf;      // Буфер для принятых данных

    private ConcurrentLinkedDeque<Byte> txBuf;      // Буфер для передаваемых дынных

    private static ReentrantLock lock = new ReentrantLock(); // Семафор для вывода логов

    @Getter
    @Setter
    private volatile boolean run = false; // volatile гарантирует, что изменение этой переменной будут видны в других потоках

    public COMHandler(COMModel com, String name) {
        this.com = com;
        this.name = name;
        rxBuf = new ConcurrentLinkedDeque<>(); // Инициализация приемного буфера
        txBuf = new ConcurrentLinkedDeque<>(); // Инициализация буфера на передачу
    }

    @Override
    public void run() {
        Integer dataLen;

        try {
            connector = com.getConnector();     // Получение коннектора для подключения к модели COM порта
            connector.setName(this.name);       // Установка имени для коннектора
            run = connector.openConnector();    // Открыть COM порт

            lock.lock();
            System.out.printf("%s открыт%n", connector.getName()); // лог
            lock.unlock();

            while(run) {    // Пока порт открыт:
                if (connector.isData()) {   // Если в приемнике есть данные
                    byte b = connector.getNext();   // Считать принятый байт
                    lock.lock();
                    System.out.printf("%s:  ->   ", this.name); // лог
                    System.out.printf("%s принял байт %s%n", connector.getName(), String.format("%02X", b)); // лог
                    lock.unlock();
                    rxBuf.addLast(b); // Пишем данные в буфер
                    // Здесь должен быть парсер посылок
                }
                if (txBuf.size() != 0) { // Если есть данные на передачу - отправляем их тут
                    byte b = txBuf.pop();
                    connector.sendByte(b);
                    lock.lock();
                    System.out.printf("%s:  ->   ", this.name); // лог
                    System.out.printf("%s отправил байт %s%n", connector.getName(), String.format("%02X", b)); // лог
                    lock.unlock();
                }
            }

        } catch (Exception e) {
            System.out.println("Ошибка в потоке обработки COM порта: " + this.name);
            System.out.println(e.getCause());
        } finally {
            // Закрываем соединение при завершении работы
            if (connector != null) {
                connector.closeConnector(); // Закрыть COM порт
            }
        }
    }

    public void sendMessage(String mes) {
        try {
            // Проверим, что сообщение не пустое
            if (mes == null || mes.isEmpty()) {
                System.out.println("Сообщение не должно быть пустым");
                return;
            }

            // Разбиение строки на байты и добавление их в буфер на передачу
            COMModel.mesToFrame(mes).forEach(b -> {
                txBuf.addLast(b);   // Добавление байта в буфер на передачу
                lock.lock();
                System.out.printf("Добавлен байт на передачу  %02X%n", txBuf.getLast(), txBuf.getLast());
                lock.unlock();
            });

        } catch (Exception e) {
            System.out.println("Ошибка при отправке сообщения: " + e.getMessage());
        }

    }

    // Метод для остановки потока
    public void requestStop() {
        run = false;
        System.out.printf("%s закрыт%n", connector.getName()); // лог
    }
}
