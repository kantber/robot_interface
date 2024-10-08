package channel.datas;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PHLData {
    byte adress;
    byte dataLength;
    List<Byte> data;
    byte crc;

    public void appendData(byte b) {
        data.add(b);
    }
}
