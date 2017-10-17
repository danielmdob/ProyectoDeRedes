import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

/**
 * Created by feroj_000 on 16/10/2017.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        String host = "127.0.0.1";

        socket = new Socket(host, 4444);

        File file = new File("VectorEncriptado.txt");
        // Get the size of the file
        //long length = file.length();
        //byte[] bytes = new byte[16 * 1024];
        byte[] bytes = Files.readAllBytes(file.toPath());
        InputStream in = new FileInputStream(file);
        OutputStream out = socket.getOutputStream();

        int count;
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
        }

        out.close();
        in.close();
        socket.close();
    }
}

