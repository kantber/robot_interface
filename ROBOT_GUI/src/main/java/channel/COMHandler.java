package channel;

import channel.protocols.PHL;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;


public class COMHandler implements Runnable {

    private COMModel com;           // Модель COM порта

    private Connector connector;    // Коннектор для подключения к модели COM порта

    private String name;            // Имя устройства

    private ConcurrentLinkedDeque<Byte> txBuf;      // Буфер для передаваемых дынных

    private static ReentrantLock lock = new ReentrantLock(); // Семафор для вывода логов

    private String PHLogPath;

    private PHL phlParser;  // Парсер физического уровня

    @Getter
    @Setter
    private volatile boolean run = false; // volatile гарантирует, что изменение этой переменной будут видны в других потоках

    static final String linkOk  = "C0 3D 01 FD C1";
    static final String linkErr = "C0 3D 01 FC C0";

    public COMHandler(COMModel com, String name) {
        this.com = com;
        this.name = name;
        txBuf = new ConcurrentLinkedDeque<>(); // Инициализация буфера на передачу
        PHLogPath = String.format("log/%s.log", this.name);  // Создание пути до файла с логом физического уровня
        phlParser = new PHL(); // Пока что прибьем это тут гвоздями
        phlParser.setName(this.name);
    }

    @Override
    public void run() {
        Integer dataLen;

        try {

            connector = com.getConnector();     // Получение коннектора для подключения к модели COM порта
            connector.setName(this.name);       // Установка имени для коннектора
            run = connector.openConnector();    // Открыть COM порт

            this.writePHLog(String.format("%s открыт%n", connector.getName()));

            while(run) {    // Пока порт открыт:
                if (connector.isData()) {   // Если в приемнике есть данные
                    byte b = connector.getNext();   // Считать принятый байт
                    this.writePHLog(String.format("%s:  <-   %s%n", this.name, String.format("%02X", b))); // лог
                    if (phlParser.parse(b)) {
                        this.writePHLog(String.format("Фрейм успешно принят %s\n", phlParser.getFrame().toString()));
                        if (phlParser.getFrame().getData().get(0) == (byte)0xFF) {
                            this.sendMessage(linkOk);
                        } else {
                            this.sendMessage(linkErr);
                        }
                    }
                }
                if (txBuf.size() != 0) { // Если есть данные на передачу - отправляем их тут
                    byte b = txBuf.pop();
                    connector.sendByte(b);
                    this.writePHLog(String.format("%s:  ->   %s%n", this.name, String.format("%02X", b))); // лог
                }
            }

        } catch (Exception e) {
            this.writePHLog("Ошибка в потоке обработки COM порта: " + this.name);
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
                this.writePHLog("Сообщение не должно быть пустым");
                return;
            }

            // Разбиение строки на байты и добавление их в буфер на передачу
            COMModel.mesToFrame(mes).forEach(b -> {
                txBuf.addLast(b);   // Добавление байта в буфер на передачу
                //this.writePHLog(String.format("Добавлен байт на передачу  %02X%n", txBuf.getLast()));
            });

        } catch (Exception e) {
            this.writePHLog("Ошибка при отправке сообщения: " + e.getMessage());
        }

    }

    // Метод для остановки потока
    public void requestStop() {
        run = false;
        this.writePHLog(String.format("%s закрыт%n", connector.getName())); // лог
    }

    // Метод для записи в лог физического уровня
    private void writePHLog(String message) {
        // Проверка на пустое сообщение
        if (message == null || message.isEmpty()) {
            System.out.printf("%s: Сообщение для записи в лог не должно быть пустым %n", this.name);
            return;
        }

        // Открываем файл в режиме добавления
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PHLogPath, true))) {
            LocalDateTime now = LocalDateTime.now();    // Получение текущего времени
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS"); // Установка формата времени

            String timestamp = now.format(formatter);   // Форматирование временной метки

            bw.write("[ " + timestamp + " ] ");
            bw.write(message);
        } catch (IOException e) {
            System.out.println("Ошибка при записи в лог: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
