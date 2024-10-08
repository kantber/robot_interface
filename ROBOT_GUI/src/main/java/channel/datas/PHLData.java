package channel.datas;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PHLData {
    byte adress;
    byte dataLength;
    List<Byte> data = new ArrayList<>();
    byte cs;

    public void appendData(byte b) {
        data.add(b);
    }

    // Подсчет контрольной суммы фрейма
    public boolean checkCs() {
        byte cs = 0x00;
        cs ^= adress;
        cs ^= dataLength;
        for (byte b: data) { cs ^= b; }
        if (cs == this.cs) return true;
        else return false;
    }
}
