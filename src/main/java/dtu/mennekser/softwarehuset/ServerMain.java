package dtu.mennekser.softwarehuset;

import dtu.mennekser.softwarehuset.backend.schema.AppBackend;
import dtu.mennekser.softwarehuset.backend.streamDB.SocketLayer;
/**
 * @Author Thor
 */
public class ServerMain {
    public static void main(String[] args) {
        //Bemærk at man skal give en (Schema extends DataLayer) database instans
        // dette er fordi at hvis den fejler i at hente en instans fra disken, har den brug
        // for en nye den kan fallback på. Og hvis man vælger ikke at bruge disken skal den
        // få en nye Schema instans. Den kan ikke selv lave det siden streamDB bruger en
        // generic Schema extends DataLayer, så den kan ikke selv constructe den.



        SocketLayer<AppBackend> server = new SocketLayer<>(true, new AppBackend());
        server.start();
    }
}
