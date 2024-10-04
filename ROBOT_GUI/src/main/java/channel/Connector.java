package channel;

import java.io.EOFException;

public interface Connector extends AutoCloseable {
    String getName();
    void setName(String name);
    void sendByte(Byte b);
    void sendFrame(String frame);
    Byte getByte();
    Byte getNext();
    String getFrame();
    void clearRx();
    boolean openConnector();
    void closeConnector();
    boolean isData();
    Integer getDataLen();
    boolean isLink();
    void seek(Integer pos) throws EOFException;
}

