package dtu.mennekser.softwarehusetas.backend.javadb.networking;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ConnInterface {
    public static void send(Serializable data, Socket socket) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(data);
        byte[] objectBin = bos.toByteArray();
        byte[] lenBin = ByteBuffer.allocate(4).putInt(objectBin.length).array();
        socket.getOutputStream().write(lenBin);
        socket.getOutputStream().write(objectBin);
    }

    public static <T> T receive(Socket socket) throws IOException {

        try {
            byte[] binLenRaw = socket.getInputStream().readNBytes(4);
            int binLen = ByteBuffer.wrap(binLenRaw).getInt();
            byte[] bin = socket.getInputStream().readNBytes(binLen);

            ByteArrayInputStream bis = new ByteArrayInputStream(bin);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object t = ois.readObject();
            return (T) t;
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new RuntimeException(e);
        }
    }
}
