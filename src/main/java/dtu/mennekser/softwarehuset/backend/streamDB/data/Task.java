package dtu.mennekser.softwarehuset.backend.streamDB.data;

import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Interface for en Serializeable lambda funktion til at opdatere state i DataLayer
 * @author Thor
 */
public interface Task<Schema extends DataLayer> extends Consumer<Schema>, Serializable {
}
