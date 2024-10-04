package channel;

public class ConnectorRX implements Connector{
    COMModel com;

    public ConnectorRX(COMModel com) {
        this.com = com;
    }

    @Override
    public void sendByte(Byte b) {

    }

    @Override
    public void sendFrame(String frame) {

    }

    @Override
    public Byte getByte() {
        return 0;
    }

    @Override
    public String getFrame() {
        return "";
    }

    @Override
    public void flush() {

    }

    @Override
    public boolean open() {
        return false;
    }

    @Override
    public void close() {

    }
}
