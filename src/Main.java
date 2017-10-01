import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.util.LinkedList;

public class Main {
    // ALT+SHIFT+F10, Right, E, Enter, Tab  : para poner comandos en el main en IntelliJ
    public static void main(String[] args) {
        byte [] bytes = urlToFinalByteArray("https://images-na.ssl-images-amazon.com/images/I/31e9Y7Ob5wL._SY300_.jpg");
        System.out.println(bytes.length);
    }

    public static byte[] urlToFinalByteArray(String url){
        byte[] finalByteArray = null;
        try {
            BufferedImage bImage = urlToBufferedImage(new URL(url));
            byte[] imagePixels = imageToByteArray(bImage);
            LinkedList missingBytes = getMissingBytes(imagePixels);
            finalByteArray = createFinalByteArray(imagePixels, missingBytes);
        } catch (MalformedURLException e) {
            System.out.println("URL no válida");
        }
        return finalByteArray;
    }

    public static BufferedImage urlToBufferedImage(URL url) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            System.out.println("No se pudo sacar la imagen del URL");
        }
        return image;
    }

    public static byte[] imageToByteArray(BufferedImage bufferedImage) {
        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
        return ( data.getData() );
    }

    // devuelve una lista con los bytes que faltan en el byteArray
    public static LinkedList getMissingBytes(byte[] byteArray) {
        LinkedList<Byte> missingByteList = new LinkedList<Byte>();
        for (byte b = 0; b < Byte.MAX_VALUE; b++) {
            missingByteList.add(b);
        }
        for (int i = 0; i < byteArray.length; i++) {
            //quita los bytes del array de la lista (porque no hacen falta)
            if (missingByteList.contains((Byte) byteArray[i])) {
                missingByteList.remove((Byte) byteArray[i]);
            }
        }
        return missingByteList;
    }

    public static byte[] createFinalByteArray(byte[] imageBytes, LinkedList missingBytes) {
        byte finalByteArray[] = new byte[imageBytes.length + missingBytes.size()];
        int i = 0;
        for (; i < imageBytes.length; i++) {
            finalByteArray[i] = imageBytes[i];
        }
        int missingBytesSize = missingBytes.size();
        for (int x = 0; x < missingBytesSize; x++) {
            finalByteArray[i] = (byte) missingBytes.remove();
            i++;
        }
        return finalByteArray;
    }
}
