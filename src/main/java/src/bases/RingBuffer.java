package src.bases;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.io.OutputStream;

/**
 * {@code @author:} wh
 * {@code @date:} 2024/5/3 01:21
 */
public class RingBuffer {

    // 读取位置
    private int readPos = 0;
    // 写入位置
    private int writePos = 0;
    // 最后一次写入的大小
    private int lastWriteSize = 0;

    // 消息数量
    private int msgAmount = 0;
    // 条数限制
    private int msgLimit;

    // 字符数量
    private int byteAmount = 0;
    // 内存限制
    private int byteLimit;

    // 消息头缓冲
    private final byte[] head = new byte[2];
    // 消息体缓冲
    private byte[] buf;

    public RingBuffer(int msgLimit, int byteLimit) {
        this.msgLimit = msgLimit;
        this.byteLimit = byteLimit;
        this.buf = new byte[byteLimit];
    }

    public void reset() {
        this.readPos = 0;
        this.writePos = 0;
        this.lastWriteSize = 0;
        this.msgAmount = 0;
        this.byteAmount = 0;
    }

    public void resetAll() {
        reset();
        this.msgLimit = 0;
        this.byteLimit = 0;
    }

    public int length() {
        return this.msgAmount;
    }

    public boolean lastMsg(OutputStream writer) throws IOException {
        if (this.byteAmount == 0) {
            return false;
        }
        int readPos = this.readPos;
        int byteAmount = this.byteAmount;
        int msgAmount = this.msgAmount;
        if (this.writePos < this.lastWriteSize) {
            this.readPos = this.writePos + this.buf.length - this.lastWriteSize;
        } else {
            this.readPos = this.writePos - this.lastWriteSize;
        }
        boolean ok = this.read(writer);
        if (ok) {
            this.readPos = readPos;
            this.byteAmount = byteAmount;
            this.msgAmount = msgAmount;
        }
        return ok;
    }
    public void write(byte[] msg) throws IOException {
        checkMsgLimit();
        checkByteLimit(msg.length);
        ByteBuffer.wrap(head).putShort((short) msg.length);
        writeBytes(head);
        writeBytes(msg);
        this.lastWriteSize = head.length + msg.length;
        this.msgAmount++;
    }

    public boolean read(OutputStream writer) throws IOException {
        if (this.byteAmount == 0) {
            return false;
        }
        int size = readSize();
        return readBytes(size, writer);
    }

    private void writeBytes(byte[] data) {
        if (this.writePos + data.length <= this.buf.length) {
            System.arraycopy(data, 0, this.buf, this.writePos, data.length);
        } else {
            int firstPart = this.buf.length - this.writePos;
            int secondPart = data.length - firstPart;
            System.arraycopy(data, 0, this.buf, this.writePos, firstPart);
            System.arraycopy(data, firstPart, this.buf, 0, secondPart);
        }
        this.byteAmount += data.length;
        this.writePos = (this.writePos + data.length) % this.buf.length;
    }

    private int readSize() {
        this.head[0] = this.buf[this.readPos];
        this.readPos = (this.readPos + 1) % this.buf.length;
        this.head[1] = this.buf[this.readPos];
        this.readPos = (this.readPos + 1) % this.buf.length;

        int size = ((this.head[0] & 0xFF) << 8) | (this.head[1] & 0xFF);
        this.byteAmount -= 2; // Assuming headSize is 2
        return size;
    }

    private boolean readBytes(int size, OutputStream writer) throws IOException {
        if (writer != null) {
            if (this.readPos + size <= this.buf.length) {
                writer.write(this.buf, this.readPos, size);
            } else {
                writer.write(this.buf, this.readPos, this.buf.length - this.readPos);
                writer.write(this.buf, 0, size - (this.buf.length - this.readPos));
            }
        }

        this.readPos = (this.readPos + size) % this.buf.length;
        this.byteAmount -= size;
        this.msgAmount--;
        return true;
    }

    private void checkMsgLimit() {
        if (this.msgLimit > 0 && this.msgAmount == this.msgLimit) {
            try {
                read(null);
            } catch (IOException ignored) {
            }
        }
    }

    private void checkByteLimit(int msgSize) throws IOException {
        int size = msgSize + 2;
        // 扩容缓冲器
        if (this.byteLimit == 0 && byteAmount + size > buf.length) {
            extendBuf(size);
            return;
        }

        // 检查是否超出限制
        if (this.byteLimit > 0 && size > this.byteLimit) {
            throw new IOException("msg exceed byte limit");
        }

        // 丢弃超出限制的消息
        if (this.byteLimit > 0 && this.byteAmount + size > this.byteLimit) {
            do {
                read(null);
            } while (this.byteAmount + size > this.byteLimit);
        }
    }

    private void extendBuf(int msgSize) {
        int bufLen = this.buf.length;
        if (this.byteAmount + msgSize > bufLen) {
            int nLen = this.buf.length * 2;
            if (this.byteAmount + msgSize > nLen) {
                nLen = this.buf.length + msgSize;
            }
            byte[] nbuf = new byte[nLen];
            if (this.readPos + this.byteAmount > bufLen) {
                System.arraycopy(this.buf, this.readPos, nbuf, 0, bufLen - this.readPos);
                System.arraycopy(this.buf, 0, nbuf, bufLen - this.readPos, this.byteAmount - (bufLen - this.readPos));
            } else {
                System.arraycopy(this.buf, this.readPos, nbuf, 0, this.byteAmount);
            }
            this.buf = nbuf;
            this.readPos = 0;
            this.writePos = this.readPos + this.byteAmount;
        }
    }
}
