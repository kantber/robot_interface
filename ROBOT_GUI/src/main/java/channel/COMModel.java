package channel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class COMModel{

    @Getter
    private static Integer id = 0;  //Общее количество созданных COM-портов

    @Getter
    private Integer connects = 0; //Подключенные коннекторы

    @Getter
    @Setter
    private String name; //Имя COM-порта

    @Getter
    @Setter
    private Integer baudrate; //Скорость COM-порта (пока никак не используется)

    @Getter
    private boolean link; //Подключены ли оба устройства

    private Integer ptx;
    private Integer prx;

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
        this.link = false;
        this.ptx = 0;
        this.prx = 0;
        this.rx = List.of();
        this.tx = List.of();
    }

    public Connector getConnector() throws NoConnectorException {
        if (connects++ == 0) return connectorTx;
        else if (connects++ == 1) return connectorRx;
        else throw new NoConnectorException();
    }

    @Override
    public String toString() {
        return  "\nCOMModel{\n" +
                "\tname     " + name + "\n" +
                "\tbaudrate " + baudrate + "\n" +
                "\tlink     " + link + "\n" +
                "}\n";
    }
}
