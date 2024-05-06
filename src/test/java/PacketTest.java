import org.junit.jupiter.api.Test;
import src.bases.Packet;

import java.util.NoSuchElementException;

/**
 * @author wh
 * @date 2024/5/6 09:52
 */
public class PacketTest {
    @Test
    public void test() {

        Packet packet = Packet.reader(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A});
        try {
//            int value = packet.readUint16();
            int value = packet.readInt32();
            packet.returnPacket();
            System.out.println("Read value: " + value);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}
