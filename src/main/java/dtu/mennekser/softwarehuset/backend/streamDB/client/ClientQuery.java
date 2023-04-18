package dtu.mennekser.softwarehuset.backend.streamDB.client;

import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;
import dtu.mennekser.softwarehuset.backend.streamDB.data.Query;
import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.ConnType;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.Ping;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * Dette er ligesom en ClientListener, men den disconneter efter den får det første resultat.
 * Den kan bruges hvis man bare skal have et enkelt stykke data en enkelt gang. Se ClientListener for at
 * forså hvordan det virker.
 *
 * Det er meget vigtigt at DataLayers state IKKE MÅ OPDATERES GENNEM DENNE KLASSE. Hvis DataLayers state ændres
 *  * i den hers Query, vil ExecutionLayer ikke vide at den skal tjekke for opdateringer i eksisteren Listeners
 *  * query.
 *
 * @author Thor
 */
public class ClientQuery<Schema extends DataLayer,T extends Serializable> {
        public final int port = 7009;
        private final Function<Schema,T> query;
        final private Consumer<IOException> onError;
        public ClientQuery(Query<Schema,T> query, Consumer<IOException> onError) {
            this.query = query;
            this.onError = onError;
        }



        public T fetch(){
            try {
                Socket socket = new Socket(ClientSettings.remoteLocation, port);
                ConnInterface.send(ConnType.Subscribe,socket);
                ConnInterface.send((Function<AppBackend,T> & Serializable) query,socket);

                Ping ping = (Ping) ConnInterface.receive(socket);
                T response =  ConnInterface.receive(socket);
                socket.close();
                return response;

            } catch (IOException e) {
                onError.accept(e);
            }
            return null;
        }
}
