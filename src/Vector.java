import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;

public class Vector implements Serializable { // puse esto de serializable para que se pueda pasar a archivo
    private LinkedList<IndexesInImage> vector;

    Vector(byte[] mensaje, byte[] imagen) {
        vector = new LinkedList<IndexesInImage>();
        createVector(mensaje, imagen);
    }

    public void createVector(byte[] mensaje, byte[] imagen) {
        //System.out.println("Entro Recursivamente");

        IndexesInImage indexes = findSubSequence(imagen, mensaje);

        if (indexes.valido == true) {
            getVector().add(indexes); //vector tiene que ser global porque es recursvio el algoritmo
        }
        else
        {
            //divide el mensaje en dos
            createVector(Arrays.copyOfRange(mensaje,0, (mensaje.length)/2), imagen);
            createVector(Arrays.copyOfRange(mensaje, (mensaje.length)/2, mensaje.length), imagen);
        }

    }

    public IndexesInImage findSubSequence(byte[] outerArray, byte[] smallerArray) { //Encuentra los indices en la imagen
        int storeJValue=0;
        for(int i = 0; i < outerArray.length - smallerArray.length+1; ++i) {
            boolean found = true;
            for(int j = 0; j < smallerArray.length; ++j) {
                if (outerArray[i+j] != smallerArray[j]) {
                    found = false;
                    break;
                }
                storeJValue = j;
            }
            if (found) { //Si si encuentra la subcadena devuelve los indices y que es valido
                //System.out.println("found");
                return new IndexesInImage(i, i+storeJValue, true);


            }
        }
        return new IndexesInImage(false); // Si no la encuentra devuelve que no es valido
    }

    public LinkedList<IndexesInImage> getVector() {
        return vector;
    }

    public void setVector(LinkedList<IndexesInImage> vector) {
        this.vector = vector;
    }

    public void createFile(String fileName) {  // crea un archivo con nombre fileName con la info del vector
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Vector loadFromFile(String fileName) { // carga desde un archivo con nombre fileName la info de un vector
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Vector returnedVector = (Vector) ois.readObject();
            ois.close();
            return returnedVector;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] toByteArray() {
        createFile("temp0");
        File tempFile = new File("temp0");
        try {
            byte[] vectorByteArray = Files.readAllBytes(tempFile.toPath());
            tempFile.delete();
            return  vectorByteArray;
        } catch (IOException e) {
            System.out.println("No pudo generar arreglo de bytes del vector");
            return null;
        }

    }
}
