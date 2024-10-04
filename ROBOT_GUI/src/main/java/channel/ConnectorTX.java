package channel;

public class ConnectorTX implements Connector {
    COMModel com;

    public ConnectorTX(COMModel com) {
        this.com = com;
    }

    @Override
    public void sendByte(Byte b) {
        com.
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
