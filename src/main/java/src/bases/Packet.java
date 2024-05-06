package src.bases;

import scala.runtime.Static;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * {@code @author:} wh
 * {@code @date:} 2024/5/5 22:38
 */
public class Packet {
    private int pos;
    private byte[] data;

    private static final GPool<Packet> pool = new GPool<>(Packet::new);

    public static Packet getPacket() {
        try {
            return pool.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Packet() {
        pos = 0;
        data = new byte[0];
    }

    public static Packet reader(byte[] data) {
        Packet packet = getPacket();
        packet.data = Arrays.copyOf(data, data.length);
        return packet;
    }

    public static Packet writer() {
        return getPacket();
    }

    // 返还对象
    public void returnPacket() {
        reset();
        pool.put(this);
    }

    public void reset() {
        pos = 0;
        Arrays.fill(data, (byte) 0);
        // data = new byte[0];
    }

    public void rewind(int pos) {
        this.pos = pos;
    }

    public byte[] getData() {
        return data;
    }

    public byte[] getRemainData() {
        return Arrays.copyOfRange(data, pos, data.length);
    }

    public int length() {
        return data.length;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void seek(int n) {
        pos += n;
    }

    public boolean readBool() throws NoSuchElementException {
        byte b = readByte();

        return b == 1;
    }

    public int readInt8() throws NoSuchElementException {
        int ret = data[pos];
        pos++;
        return ret;
    }

    public byte readByte() throws NoSuchElementException {
        if (pos >= data.length) {
            throw new NoSuchElementException("Read byte failed");
        }

        byte ret = data[pos];
        pos++;
        return ret;
    }

    // 读取一个字节
    public int nextBytesSize() throws NoSuchElementException {
        if (pos + 2 > data.length) {
            throw new NoSuchElementException("Read bytes header failed");
        }

        int ret = ((data[pos] & 0xFF) << 8) | (data[pos + 1] & 0xFF);
        pos += 2;
        return ret;
    }

    // 读取四个字节
    public int nextBytesSize32() throws NoSuchElementException {
        if (pos + 4 > data.length) {
            throw new NoSuchElementException("Read bytes header failed");
        }

        int ret = ((data[pos] & 0xFF) << 24) | ((data[pos + 1] & 0xFF) << 16) | ((data[pos + 2] & 0xFF) << 8) | (data[pos + 3] & 0xFF);
        pos += 4;
        return ret;
    }

    public byte[] readBytes() throws NoSuchElementException {
        int size = readUint16();

        return doReadBytes(size);
    }

    public byte[] readBytes32() throws NoSuchElementException {
        int size = readUint32();

        return doReadBytes(size);
    }

    public int readUint32() throws NoSuchElementException {
        if (pos + 4 > data.length) {
            throw new NoSuchElementException("Read uint32 failed");
        }

        int ret = ((data[pos] & 0xFF) << 24) | ((data[pos + 1] & 0xFF) << 16) | ((data[pos + 2] & 0xFF) << 8) | (data[pos + 3] & 0xFF);
        pos += 4;
        return ret;
    }

    public byte[] doReadBytes(int size) throws NoSuchElementException {
        if (pos + size > data.length) {
            throw new NoSuchElementException("Read bytes data failed");
        }

        byte[] ret = Arrays.copyOfRange(data, pos, pos + size);
        pos += size;
        return ret;
    }

    public String readString() throws NoSuchElementException {
        byte[] bytes = readBytes();
        if (bytes.length == 0) {
            throw new NoSuchElementException("Read string failed");
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public void skipString() throws NoSuchElementException {
        if (pos + 2 > data.length) {
            throw new NoSuchElementException("Read string header failed");
        }

        int size = readUint16();
        if (pos + size > data.length) {
            throw new NoSuchElementException("Read string data failed");
        }
        pos += size;
    }

    public void readBytesString(OutputStream writer) throws IOException, NoSuchElementException {
        byte[] bytes = this.readBytes();
        writer.write(bytes);
    }

    public int readUint16() throws NoSuchElementException {
        if (pos + 2 > data.length) {
            throw new NoSuchElementException("Read uint16 failed");
        }

        int ret = ((data[pos] & 0xFF) << 8) | (data[pos + 1] & 0xFF);
        pos += 2;
        return ret;
    }


    public short readInt16() throws NoSuchElementException {
        int ret = this.readUint16();
        return (short) ret;
    }

    public int readUint24() throws NoSuchElementException {
        if (pos + 3 > data.length) {
            throw new NoSuchElementException("Read uint24 failed");
        }

        int ret = ((data[pos] & 0xFF) << 16) | ((data[pos + 1] & 0xFF) << 8) | (data[pos + 2] & 0xFF);
        pos += 3;
        return ret;
    }

    public int readInt24() throws NoSuchElementException {
        int ret = this.readUint24();
        return ret;
    }


    public int readInt32() throws NoSuchElementException {
        long ret = this.readUint32();
        return (int) ret;
    }

    public long readUint64() throws NoSuchElementException {
        if (pos + 8 > data.length) {
            throw new NoSuchElementException("Read uint64 failed");
        }

        long ret = 0;
        for (int i = 0; i < 8; i++) {
            ret |= (data[pos + i] & 0xFFL) << (56 - (i * 8));
        }
        pos += 8;
        return ret;
    }

    public long readInt64() throws NoSuchElementException {
        return readUint64();
    }

    public float readFloat32() throws NoSuchElementException {
        int bits = this.readUint32();
        float ret = Float.intBitsToFloat(bits);
        if (Float.isNaN(ret) || Float.isInfinite(ret)) {
            return 0;
        }
        return ret;
    }

    public double readFloat64() throws NoSuchElementException {
        long bits = this.readUint64();
        double ret = Double.longBitsToDouble(bits);
        if (Double.isNaN(ret) || Double.isInfinite(ret)) {
            return 0;
        }
        return ret;
    }

}
