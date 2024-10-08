package channel.protocols;

import channel.datas.PHLData;

import java.util.Deque;
import java.util.List;

// Парсер физического уровня
// Выделяет фреймы из потока байт
public class PHL {

    private final byte FEND = (byte)0xC0;   // Байт начала фрейма
    private final byte FESC = (byte)0xCD;   // Байт esc-последовательности

    private boolean inProgress = false;     // Фрейм в процессе приема
    private boolean byteStaffing = false;   // Подмена байта
    private Integer cnt = 0;                // Количество принятых байт

    private PHLData frame;  // Фрейм данных

    public PHL() {
    }

    public void parse(byte b) {
        // Начало нового фрейма
        if (b == FEND) {
            inProgress = true;      // фрейм в процессе приема
            frame = new PHLData();  // создаем новый фрейм
            cnt = 0;                // обнулим количество принятых байт
        }

        // Если в процессе приема фрейма
        else if (inProgress) {
            if (byteStaffing) {

                cnt++;
                byteStaffing = false;
            } else if (b == FESC) {
                byteStaffing = true;
            } else {
                if (cnt == 0) frame.setAdress(b);
                else if (cnt == 1) frame.setDataLength(b);
                else if (cnt < frame.getDataLength() + 2) frame.appendData(b);
                else {
                    frame.setCrc(b);
                    this.checkCrc();
                }
                cnt++;
            }
        }

        else return;    // Пропускаем байт - мусор
    }
}
