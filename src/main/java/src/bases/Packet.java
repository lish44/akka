package src.bases;

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

    public Packet getPacket() {
        try {
            return pool.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Packet() {
        this.pos = 0;
        this.data = new byte[0];
    }

    public Packet reader(byte[] data) {
        Packet packet = getPacket();
        packet.data = Arrays.copyOf(data, data.length);
        return packet;
    }

    public Packet writer() {
        return getPacket();
    }

    // 返还对象
    public void returnPacket() {
        reset();
        pool.returnObj(this);
    }

    public void reset() {
        this.pos = 0;
        Arrays.fill(this.data, (byte) 0);
        // data = new byte[0];
    }

    public void rewind(int pos) {
        this.pos = pos;
    }

    public byte[] getData() {
        return this.data;
    }

    public byte[] getRemainData() {
        return Arrays.copyOfRange(this.data, this.pos, this.data.length);
    }

    public int length() {
        return this.data.length;
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void seek(int n) {
        this.pos += n;
    }

    public boolean readBool() throws NoSuchElementException {
        byte b = readByte();

        return b == 1;
    }

    public int readInt8() throws NoSuchElementException {
        int ret = this.data[this.pos];
        this.pos++;
        return ret;
    }

    public byte readByte() throws NoSuchElementException {
        if (this.pos >= this.data.length) {
            throw new NoSuchElementException("Read byte failed");
        }

        byte ret = this.data[this.pos];
        this.pos++;
        return ret;
    }
    // 读取一个字节
    public int nextBytesSize() throws NoSuchElementException {
        if (this.pos + 2 > this.data.length) {
            throw new NoSuchElementException("Read bytes header failed");
        }

        int ret = ((this.data[this.pos] & 0xFF) << 8) | (this.data[this.pos + 1] & 0xFF);
        this.pos += 2;
        return ret;
    }

    // 读取四个字节
    public int nextBytesSize32() throws NoSuchElementException {
        if (this.pos + 4 > this.data.length) {
            throw new NoSuchElementException("Read bytes header failed");
        }

        int ret = ((this.data[this.pos] & 0xFF) << 24) | ((this.data[this.pos + 1] & 0xFF) << 16) | ((this.data[this.pos + 2] & 0xFF) << 8) | (this.data[this.pos + 3] & 0xFF);
        this.pos += 4;
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

    public byte[] doReadBytes(int size) throws NoSuchElementException {
        if (this.pos + size > this.data.length) {
            throw new NoSuchElementException("Read bytes data failed");
        }

        byte[] ret = Arrays.copyOfRange(this.data, this.pos, this.pos + size);
        this.pos += size;
        return ret;
    }

    public int readUint16() throws NoSuchElementException {
        if (this.pos + 2 > this.data.length) {
            throw new NoSuchElementException("Read uint16 failed");
        }

        int ret = ((this.data[this.pos] & 0xFF) << 8) | (this.data[this.pos + 1] & 0xFF);
        this.pos += 2;
        return ret;
    }

    public int readUint32() throws NoSuchElementException {
        if (this.pos + 4 > this.data.length) {
            throw new NoSuchElementException("Read uint32 failed");
        }

        int ret = ((data[pos] & 0xFF) << 24) | ((data[pos + 1] & 0xFF) << 16) | ((data[pos + 2] & 0xFF) << 8) | (data[pos + 3] & 0xFF);
        this.pos += 4;
        return ret;
    }
}
