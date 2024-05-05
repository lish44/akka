import org.junit.jupiter.api.Test;
import src.bases.RingBuffer;

/**
 * {@code @author:} wh
 * {@code @date:} 2024/5/3 01:28
 */
public class RingBufferTest {
    @Test
    public void testRingBuffer() {
        RingBuffer ringBuffer = new RingBuffer(1, 0);
        try {
            for (int i = 0; i < 10; i++) {
                ringBuffer.write(String.valueOf(i).getBytes());
            }
            ringBuffer.read(System.out);
            ringBuffer.read(System.out);
            ringBuffer.read(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
