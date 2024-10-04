package channel;

public interface Connector {
    void sendByte(Byte b);
    void sendFrame(String frame);
    Byte getByte();
    String getFrame();
    void flush();
    boolean open();
    void close();
}
