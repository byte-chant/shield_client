import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ServerSocketTest {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 9527);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeChars("hello server");
        socket.close();
    }

}