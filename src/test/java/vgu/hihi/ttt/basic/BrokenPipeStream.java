package vgu.hihi.ttt.basic;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BrokenPipeStream extends PrintStream {
    public BrokenPipeStream() {
        // Direct output to a dummy stream
        super(new ByteArrayOutputStream());
    }

    @Override
    public void write(int b) {
        throw new RuntimeException("Broken Pipe Simulation");
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        throw new RuntimeException("Broken Pipe Simulation");
    }
}
