import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by feroj_000 on 16/10/2017.
 */
public class Server {

    // ALT+SHIFT+F10, Right, E, Enter, Tab  : para poner comandos en el main en IntelliJ
    public static void main(String[] args) throws IOException {
        String decryptedFileName = "desencriptado1";
        String url = args[0];
        String key = args[1]; //Debe ser de 16 bytes la llave
        System.out.println(args.length);
        String host = (args.length == 2) ? "127.0.0.1" : args[2]; //si no especifica ip, usa localhost
        byte [] image = CommonMethods.urlToFinalByteArray(url);
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException ex) {
            System.out.println("Can't setup server on this port number. ");
        }

        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;

        try {
            socket = serverSocket.accept();
        } catch (IOException ex) {
            System.out.println("Can't accept client connection. ");
        }

        try {
            in = socket.getInputStream();
        } catch (IOException ex) {
            System.out.println("Can't get socket input stream. ");
        }

        try {
            out = new FileOutputStream("encriptado1"); //Cambiarlo para su folder
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. ");
        }

        byte[] bytes = new byte[16*1024];

        int count;
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
            //count++;
        }

        out.close();
        in.close();
        socket.close();
        serverSocket.close();

        File encryptedVector = new File("encriptado1");
        decryptVector(encryptedVector, key, decryptedFileName);
        encryptedVector.delete();
        System.out.println(CommonMethods.interpretVector(Vector.loadFromFile(decryptedFileName), image));
        File file = new File(decryptedFileName);
        file.delete();
    }

    public static void decryptVector(File encryptedFile, String key, String decryptedFileName) {
        File decryptedFile = new File(decryptedFileName);
        try {
            decryptedFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Crypto.decrypt(key, encryptedFile, decryptedFile);//Desencripta
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
