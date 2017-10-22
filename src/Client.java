import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

/**
 * Created by feroj_000 on 16/10/2017.
 */
public class Client {

    // ALT+SHIFT+F10, Right, E, Enter, Tab  : para poner comandos en el main en IntelliJ
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        String message = args[0];
        String url = args[1];
        String key = args[2];
        String host = (args.length == 3) ? "127.0.0.1" : args[3]; //si no especifica ip, usa localhost

        byte[] image = CommonMethods.urlToFinalByteArray(url);
        Vector sentVector = new Vector(CommonMethods.stringToByteArray(message), image);

        socket = new Socket(host, 4444);
        sentVector.createFile("desencriptado");
        File inputFile = new File("desencriptado");
        File file = new File("encriptado");
        encryptVector(key, inputFile, file);
        // Get the size of the file
        //long length = file.length();
        //byte[] bytes = new byte[16 * 1024];
        byte[] bytes = Files.readAllBytes(file.toPath());
        //byte[] bytes = sentVector.toByteArray();
        InputStream in = new FileInputStream(file);
        OutputStream out = socket.getOutputStream();

        int count;
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
        }

        out.close();
        in.close();
        socket.close();
        inputFile.delete();
        file.delete();
    }

    public static void encryptVector(String key, File inputFile, File encryptedFile) {
        try {
            Crypto.encrypt(key, inputFile, encryptedFile); //Encripta
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}

