package channel.protocols;

import channel.datas.PHLData;
import lombok.Setter;

import java.util.Deque;
import java.util.List;

// Парсер физического уровня
// Выделяет фреймы из потока байт
public class PHL {

    private final byte FEND = (byte)0xC0;       // Байт начала фрейма
    private final byte FENDMIR = (byte)0x01;    // Зеркало байта начала фрейма
    private final byte FESC = (byte)0xCD;       // Байт esc-последовательности
    private final byte FESCMIR = (byte)0x02;     // Зеркало байта байтстаффинга

    private boolean inProgress = false;     // Фрейм в процессе приема
    private boolean byteStaffing = false;   // Подмена байта
    private Integer cnt = 0;                // Количество принятых байт

    @Setter
    private String name;

    private PHLData frame;  // Фрейм данных

    public PHL() {
    }

    public boolean parse(byte b) throws InvalidControlSummException {
        // Начало нового фрейма
        if (b == FEND) {
            this.reset();   // Состояние начала приема фрейма
            inProgress = true;
            return false;
        }

        // Если в процессе приема фрейма
        else if (inProgress) {
            // Обработка байтстаффинга
            if (byteStaffing) {
                if (b == FENDMIR) b = FEND;
                else if (b == FESCMIR) b = FESC;
                cnt++;
                byteStaffing = false;
            // Вход в обработку байтстаффинга
            } else if (b == FESC) {
                byteStaffing = true;
                return false;
            }
            // Прием данных
            if (cnt == 0) {
                frame.setAdress(b); // Первый байт - адрес
                System.out.printf("%S:  Adress = %02X, cnt = %d%n", this.name, b, cnt);
            }
            else if (cnt == 1) {
                frame.setDataLength(b); // Второй байт - длинна данных в поле data
                System.out.printf("%s:  Length = %02X, cnt = %d%n", this.name, b, cnt);
            }
            else if (cnt < frame.getDataLength() + 2) {
                frame.appendData(b);  // Прием поля data
                System.out.printf("%s:  Data = %02X, cnt = %d%n", this.name, b, cnt);
            }
            else {
                frame.setCs(b);                                            // Прием контрольной суммы
                System.out.printf("%s:  CS = %02X, cnt = %d%n", this.name, b, cnt);
                if (!frame.checkCs()) {                                    // Проверка контрольной суммы
                    // Контрольная сумма не сошлась
                    this.reset();                   // Сброс принятого фрейма
                    throw new InvalidControlSummException(); // Ошибка контрольной суммы
                } else return true;    // Фрейм принят
            }
            cnt++;
            return false;
        }
        return false;
    }

    // Сброс до состояния начала приема фрейма
    public void reset() {
        this.frame = new PHLData();
        cnt = 0;
        inProgress = false;
        byteStaffing = false;
    }
}
