import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.Objects;

public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectInputStream reader;
    private final ObjectOutputStream writer;
    public boolean closed;

    public Connection(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            this.writer = createWriter();
            this.reader = createReader();
            this.closed = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection(ServerSocket server) {
        try {
            this.socket = server.accept();
            this.writer = createWriter();
            this.reader = createReader();
            this.closed = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ObjectInputStream createReader() throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

    private ObjectOutputStream createWriter() throws IOException {
        ObjectOutputStream res = new ObjectOutputStream(socket.getOutputStream());
        res.flush();
        return res;
    }

    public String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "Unable to get ip";
    }



    @Override
    public String toString() {
        return "Online.Connection{" + "ip=" + getIp() + '}';
    }


    @Override
    public void close() throws IOException {
        if (!closed) {
            closed = true;
            reader.close();
            if (!socket.isClosed()) {
                writer.close();
                socket.close();
            }
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(socket, reader, writer, closed);
    }

    public boolean equals(Object x) {
        if (x == null || x.getClass() != this.getClass()) return false;
        if (x == this) return true;
        Connection cur = (Connection) x;
        return cur.socket == this.socket && cur.getIp().equals(this.getIp());
    }
}
