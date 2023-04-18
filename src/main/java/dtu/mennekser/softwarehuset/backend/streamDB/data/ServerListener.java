package dtu.mennekser.softwarehuset.backend.streamDB.data;

import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.schema.Database;
import dtu.mennekser.softwarehuset.backend.streamDB.networking.Ping;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Objects;
import java.util.Random;

/**
 * En ServerListener er serverens representation af en listener. Det er den her som har ownership og dens
 *  specifikke tcp forbindelse.
 *  En ServerListener har fået en query over forbindelsen (parset og konstructet i SocketLayer), det
 *  er ServerListenerens ansvar at:
 *
 *  1. Tjekke om resultatet af queryen har ændret sig når ExecutorLayer har eksikveret ændriger i DataLayer
 *  2. Hvis der er ændringer sendes den nye data over tcp forbindelsen.
 *  3. Hvis tcp forbindelsen er død skal ExecutorLayer notificeres så ServerListener instancen kan blive droppet.
 *
 *  Bemærk at ServerListener IKKE har sin egen thread. ExecutorLayer kalder ServerListener::Update(DataLayer),
 *  når der skal tjekkes om der er nyt resulat af queryen.
 *
 * @author Thor
 */
public class ServerListener<Schema extends DataLayer,T extends Serializable> {
    boolean prevDataExists = false;
    int prevHash = 0;
    final Query<Schema,T> query;
    final Socket client;

    public ServerListener(Query<Schema,T> filter, Socket client) {
        this.query = filter;
        this.client = client;
    }

    /**
     * @return Returns the success status.
     * True is ok status.
     * False means the connection has been severed and the subscriber needs to be killed
     */
    public boolean update(Schema tables) {
        //Ping to check if still alive
        try {
            ConnInterface.send(new Ping(new Random().nextInt()),client);
        } catch (IOException e) {
            return false;
        }


        T newData = query.apply(tables);
        int newHash = customHash(newData);

        if (prevDataExists) {
            if (newHash==prevHash) {
                return true;
            }
        }

        prevHash = newHash;
        prevDataExists = true;

        try {
            ConnInterface.send(newData, client);
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    //Hele pointen med den her klasse er at den skal kunne sammenligne resultatet af en query med
    // det tidligere resultat af samme query. Det første man tænker at man kan gøre er man kan bruge
    // Object.equals(). Men det virker ikke fordi resultatet af en query godt kan være en reference til et objekt
    // i databasen. Så hvis den underliggende data i objektet ændres vil den også ændres i den gamle reference,
    // hvilket betyder at sammenligningen vil sige at der ikke er nogen forskel, selvom objektet har ændret sig.
    // Det betyder at klienten ikke får den nyeste version af objektet.

    // Min løsning til dette var at brug Objects.hash(...). Det her virkede bedre, men stadig ikke godt nok.
    // For hvem villede havde gætte at hvis man ændre en primitivt felt i et objekt, FORBLIVER DETS HASH DET SAMME.

    // Hele pointen med hashes er at kunne sammenligne to ting, men hvis den siger de er ens selvom de ikke er
    // ødelægger det jo hele pointen.

    //Min løsning til det her er at skrive min egen hash funktion, denne hash funktion er lig summen af
    // objektets hash + hashet af elle dens felter, hvilket den gør ved javas reflektions bibliotek.

    //Jeg tror ikke engang at det er i sig selv er godt nok, for hvad nu hvis et felt i et felt ændrer sig?
    //Dette kan løses ved at gøre funktionen rekursiv men hvad nu hvis et felt i et objekt i en liste i et objekt
    // ændrer sig. Så aner jeg ikke hvordan man skal kunne fange det.

    static int customHash(Object object) {
        int sum = Objects.hash(object);

        if (object == null) {
            return sum;
        }

        for (Field field : object.getClass().getFields()) {
            try {
                field.setAccessible(true);
                sum += Objects.hash(field.get(object));
            } catch ( Exception e) {
                System.out.println("Illegal hash lol. Failed at: " + object.getClass().getName() + "." + field.getName());
            }
        }

        return sum;
    }
}
