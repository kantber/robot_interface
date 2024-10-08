package channel;

import lombok.Getter;
import lombok.Setter;

import java.io.EOFException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class COMModel{

    @Getter
    private static Integer id = 0;  //Общее количество созданных COM-портов

    private boolean txConnects = false; //tx коннектор получен

    private boolean rxConnects = false; //rx коннектор получен

    @Getter
    private Integer openConnectors = 0; //

    @Getter
    @Setter
    private String name; //Имя COM-порта

    @Getter
    @Setter
    private Integer baudrate; //Скорость COM-порта (пока никак не используется)

    @Getter
    private ConcurrentLinkedDeque<Byte> rx; //Буфер приема

    @Getter
    private ConcurrentLinkedDeque<Byte> tx; //Буфер передачи

    @Getter
    private final ConnectorTX connectorTx = new ConnectorTX(this);    //Подключение к одному концу моделируемого канала

    @Getter
    private final ConnectorRX connectorRx = new ConnectorRX(this);    //Подключение к другому концу моделируемого канала

    public COMModel() {
        COMModel.id++;
        this.name = "COM" + COMModel.id;
        this.baudrate = 115200;
        this.rx = new ConcurrentLinkedDeque<>();
        this.tx = new ConcurrentLinkedDeque<>();
    }

    public Connector getConnector() throws NoConnectorException {
        if (!txConnects) { txConnects = true; return connectorTx; }
        else if (!rxConnects) { rxConnects = true; return connectorRx; }
        else throw new NoConnectorException();
    }

    @Override
    public String toString() {
        return  "\nCOMModel{\n" +
                "\tname     " + name + "\n" +
                "\tbaudrate " + baudrate + "\n" +
                "}\n";
    }

    public static List<Byte> mesToFrame(String message) {
        return Arrays
                .stream(message.split(" "))
                .map((String s) -> (byte)Integer.parseInt(s, 16))
                .toList();
    }

    public static String frameToMes(List<Byte> frame) {
        return frame
                .stream()
                .map((b) -> String.format("%02X", b))
                .collect(Collectors.joining(" ")).trim();
    }

    private abstract class BaseConnector implements Connector {

        private COMModel channel;
        private boolean open = false;
        protected Integer prx = 0;    //Указатель чтения
        @Getter
        private String name;

        @Override
        public void setName(String name) {
            this.name = String.format("Connector '%s'", name);
        }

        @Override
        public boolean openConnector() {
            if (!open) {
                open = true;        //открытие коннектора
                openConnectors++;   //увеличение количества открытых коннекторов
            }
            return open;
        }

        @Override
        public void closeConnector() {
            if (open) {
                open = false;       //закрытие коннектора
                openConnectors--;   //уменьшение количества открытых коннекторов
            }
        }

        @Override
        public boolean isLink() {
            return openConnectors == 2;
        }

        @Override
        public void seek(Integer pos) throws EOFException {
            if (pos < this.getDataLen()) prx = pos;
            else throw new EOFException();
        }

        @Override
        public String getFrame() {
            return "";
        }
    }

    private class ConnectorTX extends BaseConnector implements Connector {
        private final COMModel channel;

        public ConnectorTX(COMModel channel) {
            this.channel = channel;
        }


        @Override
        public void sendByte(Byte b) {
            channel.tx.addLast(b); //передача байта по каналу TX
        }

        @Override
        public void sendFrame(String mes) {
            COMModel.mesToFrame(mes).forEach((b) -> channel.tx.addLast(b));
        }

        @Override
        public Byte getByte() {
            //return channel.rx.get(prx); //получение байта по указателю чтения
            return (byte)0x00;
        }

        @Override
        public Byte getNext() {
            return channel.rx.pop(); //получение первого байта из приемного буфера (RX)
        }

        @Override
        public void clearRx() {
            channel.rx = new ConcurrentLinkedDeque<>(); //очистка приемного буфера (RX)
        }

        @Override
        public boolean isData() {
            return !channel.rx.isEmpty(); //в приемном буфере (RX) есть непрочитанные данные
        }

        @Override
        public Integer getDataLen() {
            return channel.rx.size(); //получает количество непрочитанных байт из приемного буфера (RX)
        }

        @Override
        public void close() throws Exception {
            txConnects = false;
            System.out.println("ConnetorTX deleted");
        }
    }

    private class ConnectorRX extends BaseConnector implements Connector {
        private final COMModel channel;

        public ConnectorRX(COMModel channel) {
            this.channel = channel;
        }


        @Override
        public void sendByte(Byte b) {
            channel.rx.addLast(b); //передача байта по каналу RX
        }

        @Override
        public void sendFrame(String mes) {
            COMModel.mesToFrame(mes).forEach((b) -> channel.rx.addLast(b));
        }

        @Override
        public Byte getByte() {
            //return channel.tx.get(prx); //получение байта по указателю чтения
            return (byte)0x00;
        }

        @Override
        public Byte getNext() {
            return channel.tx.pop(); //получение первого байта из приемного буфера (TX)
        }

        @Override
        public String getFrame() {
            return "";
        }

        @Override
        public void clearRx() {
            channel.tx = new ConcurrentLinkedDeque<>(); //очистка приемного буфера (TX)
        }

        @Override
        public boolean isData() {
            return !channel.tx.isEmpty(); //в приемном буфере (TX) есть непрочитанные данные
        }

        @Override
        public Integer getDataLen() {
            return channel.tx.size(); //получает количество непрочитанных байт в приемном буфере (TX)
        }

        @Override
        public void close() throws Exception {
            rxConnects = false;
            System.out.println("ConnectorRX deleted");
        }
    }
}
