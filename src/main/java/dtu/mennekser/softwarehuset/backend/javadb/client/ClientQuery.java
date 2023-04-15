package dtu.mennekser.softwarehuset.backend.javadb.client;

import dtu.mennekser.softwarehuset.backend.data.DataQuery;
import dtu.mennekser.softwarehuset.backend.db.Database;
import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnType;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClientQuery<T extends Serializable> {
        public final int port = 7009;
        private final Function<Database,T> query;
        final private Consumer<IOException> onError;
        public ClientQuery(DataQuery<T> query, Consumer<IOException> onError) {
            this.query = query;
            this.onError = onError;
        }



        public T fetch(){
            try {
                Socket socket = new Socket(ClientSettings.remoteLocation, port);
                ConnInterface.send(ConnType.Subscribe,socket);
                ConnInterface.send((Function<Database,T> & Serializable) query,socket);
                T response =  ConnInterface.receive(socket);
                socket.close();
                return response;

            } catch (IOException e) {
                onError.accept(e);
            }
            return null;
        }
}
