package vgu.hihi.ttt.basic.svclarchitecture;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Adapter between socket line messages and an InputStream.
 *
 * HumanPlayer already reads from InputStream using Scanner.
 * This class lets the server push socket messages into a buffer,
 * and HumanPlayer can read them as if they came from System.in.
 */
public class SocketInputBuffer extends InputStream {
    private static final int END_OF_STREAM = -1;

    private final BlockingQueue<Integer> buffer = new LinkedBlockingQueue<>();
    private volatile boolean closed = false;

    public void submitLine(String line) {
        if (closed || line == null) {
            closeBuffer();
            return;
        }

        byte[] bytes = (line + System.lineSeparator())
                .getBytes(StandardCharsets.UTF_8);

        for (byte b : bytes) {
            buffer.offer(b & 0xFF);
        }
    }

    public void closeBuffer() {
        closed = true;
        buffer.offer(END_OF_STREAM);
    }

    @Override
    public int read() throws IOException {
        try {
            int value = buffer.take();

            if (value == END_OF_STREAM) {
                return -1;
            }

            return value;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while reading socket input buffer", e);
        }
    }
}