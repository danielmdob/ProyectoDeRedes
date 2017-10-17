import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by feroj_000 on 16/10/2017.
 */
public class Server {
    public static void main(String[] args) throws IOException {
        String decryptedFileName = "desencriptado.txt";
        String key = "RedesDeComputado"; //Debe ser de 16 bytes la llave
        byte [] image = Main.urlToFinalByteArray("https://i.kinja-img.com/gawker-media/image/upload/s--2wKOFE_v--/c_scale,fl_progressive,q_80,w_800/iwpzjy3ggdpapoagr8av.jpg");
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
            out = new FileOutputStream("VectorEncriptado2.txt"); //Cambiarlo para su folder
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

        File encryptedVector = new File("VectorEncriptado2.txt");
        decryptVector(encryptedVector, key, decryptedFileName);
        System.out.println(Main.interpretVector(Vector.loadFromFile(decryptedFileName), image));
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
