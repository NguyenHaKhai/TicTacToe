package vgu.hihi.ttt.basic.svclarchitecture;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Creates a PrintStream that redirects printed text to the client socket.
 */
public final class SocketPrintStreamFactory {
    private SocketPrintStreamFactory() {
    }

    public static PrintStream from(PrintWriter clientOut) {
        OutputStream outputStream = new OutputStream() {
            private final StringBuilder lineBuffer = new StringBuilder();

            @Override
            public void write(int b) {
                char c = (char) b;

                if (c == '\n') {
                    flushLine();
                } else if (c != '\r') {
                    lineBuffer.append(c);
                }
            }

            @Override
            public void flush() {
                flushLine();
            }

            private void flushLine() {
                if (!lineBuffer.isEmpty()) {
                    clientOut.println(lineBuffer.toString());
                    lineBuffer.setLength(0);
                }
            }
        };

        return new PrintStream(outputStream, true, StandardCharsets.UTF_8);
    }
}