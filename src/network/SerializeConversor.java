package network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class SerializeConversor <S extends Serializable> {

    public byte[] serialize (S object) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(); // Criando um buffer na memória, de forma que todos os objetos/dados enviados para o stream sejam armazenados no buffer
        ObjectOutputStream outputStream = new ObjectOutputStream(output);

        outputStream.writeObject(object);

        return output.toByteArray();
    }

    public S deserialize (byte[] object) {
       try {
           ByteArrayInputStream input = new ByteArrayInputStream(object);
           ObjectInputStream inputStream = new ObjectInputStream(input);

           return (S) inputStream.readObject();
       } catch (Exception e) {
           return null;
       }
    }

}