package dtu.mennekser.softwarehuset.backend.data;

import dtu.mennekser.softwarehuset.backend.javadb.networking.ConnInterface;
import dtu.mennekser.softwarehuset.backend.db.Database;
import dtu.mennekser.softwarehuset.backend.javadb.networking.Ping;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author Thor
 */
public class Subscriber<T extends Serializable> {
    boolean prevDataExists = false;
    int prevHash = 0;
    final DataQuery<T> query;
    final Socket client;

    public Subscriber(DataQuery<T> filter, Socket client) {
        this.query = filter;
        this.client = client;
    }

    /**
     * @return Returns the success status.
     * True is ok status.
     * False means the connection has been severed and the subscriber needs to be killed
     */
    public boolean update(Database tables) {
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
    //Okay wtf. Jeg (Thor) er mega sur
    //Nu skal du bare høre om hvor mega besværlidt det er at sammenligne to objekter i java.
    //Okay så, hele pointen med den her klasse er at den skal kunne sammenligne resultatet af en query med
    // det tidligere resultat af samme query. Det første man tænker at man kan gøre er man kan bruge
    // Object.equals(). Men det virker ikke fordi resultatet af en query godt kan være en reference til et objekt
    // i databasen. Så hvis den underliggende data i objektet ændres vil den også ændres i den gamle reference,
    // hvilket betyder at sammenligningen vil sige at der ikke er nogen forskel, selvom objektet har ændret sig.
    // Det betyder at klienten ikke får den nyeste version af objektet.

    // Min løsning til dette var at brug Objects.hash(...). Det her virkede bedre, men stadig ikke godt nok.
    // For hvem villede havde gætte at hvis man ændre en primitivt felt i et objekt, FORBLIVER DETS HASH DET SAMME.

    // WWWWWWWWTTTTTTTTFFFFFFFF
    // Hele pointen med hashes er at kunne sammenligne to ting, men hvis den siger de er ens selvom de ikke er
    // ødelægger det jo hele pointen.

    //Min løsning til det her er at skrive min egen hash funktion, denne hash funktion er lig summen af
    // objektets hash + hashet af elle dens felter, hvilket den gør ved javas reflektions bibliotek.

    //Ikke ok. Jeg tror ikke engang at det er i sig selv er godt nok, for hvad nu hvis et felt i et felt ændrer sig?
    //Dette kan løses ved at gøre funktionen rekursiv men hvad nu hvis et felt i et objekt i en liste i et objekt
    // ændrer sig. Så aner jeg ikke hvordan man skal kunne fange det.

    static int customHash(Object object) {
        int sum = Objects.hash(object);

        if (object == null) {
            return sum;
        }

        for (Field field : object.getClass().getFields()) {
            try {
                sum += Objects.hash(field.get(object));
            } catch ( Exception e) {
                System.out.println("Illegal hash lol.");
            }
        }

        return sum;
    }
}
