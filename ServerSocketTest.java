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
        //网络传输字节流是大端序，即低位存储在高地址；高位存储在低地址。
        //例如0x12345678，大端序为：0x12| 0x34 | 0x56 | 0x78 （低地址 --> 高地址）
        //参考博客：https://blog.csdn.net/iteye_18053/article/details/82681103
        //参考博客：https://www.yiibai.com/java/java_networking.html
        byte[] uuidBytes = UUID.randomUUID().toString().replace("-", "").getBytes(StandardCharsets.UTF_8);
        byte[] bodyBytes = "hello".getBytes(StandardCharsets.UTF_8);
        Integer contentLength = 2/*魔数*/ + 1/*协议版本*/ + 1/*上行下行*/ + uuidBytes.length + bodyBytes.length;
        Integer totalLength = 4/*用来存储contentLength的整型，这个整型本身占用的长度*/ + contentLength;
        dataOutputStream.writeInt(contentLength);
        dataOutputStream.writeShort(9527);
        dataOutputStream.writeShort(1);
        dataOutputStream.write(uuidBytes);
        dataOutputStream.write(bodyBytes);
        socket.close();
    }

}