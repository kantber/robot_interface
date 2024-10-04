package channel;

import lombok.Getter;
import lombok.Setter;

import java.io.EOFException;
import java.util.*;

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
    private List<Byte> rx; //Буфер приема

    @Getter
    private List<Byte> tx; //Буфер передачи

    @Getter
    private final ConnectorTX connectorTx = new ConnectorTX(this);    //Подключение к одному концу моделируемого канала

    @Getter
    private final ConnectorRX connectorRx = new ConnectorRX(this);    //Подключение к другому концу моделируемого канала

    public COMModel() {
        COMModel.id++;
        this.name = "COM" + COMModel.id;
        this.baudrate = 115200;
        this.rx = new ArrayList<Byte>();
        this.tx = new ArrayList<Byte>();
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

    private abstract class BaseConnector implements Connector {

        private COMModel channel;
        private boolean open = false;
        protected Integer prx = 0;    //Указатель чтения
        @Getter
        @Setter
        private String name;


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
            channel.tx.add(b); //передача байта по каналу TX
        }

        @Override
        public void sendFrame(String frame) {

        }

        @Override
        public Byte getByte() {
            return channel.rx.get(prx); //получение байта по указателю чтения
        }

        @Override
        public Byte getNext() {
            return channel.rx.getFirst(); //получение первого байта из приемного буфера (RX)
        }

        @Override
        public void clearRx() {
            channel.rx = new ArrayList<Byte>(); //очистка приемного буфера (RX)
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
            channel.rx.add(b); //передача байта по каналу RX
        }

        @Override
        public void sendFrame(String frame) {

        }

        @Override
        public Byte getByte() {
            return channel.tx.get(prx); //получение байта по указателю чтения
        }

        @Override
        public Byte getNext() {
            return channel.tx.getFirst(); //получение первого байта из приемного буфера (TX)
        }

        @Override
        public String getFrame() {
            return "";
        }

        @Override
        public void clearRx() {
            channel.tx = new ArrayList<Byte>(); //очистка приемного буфера (TX)
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
