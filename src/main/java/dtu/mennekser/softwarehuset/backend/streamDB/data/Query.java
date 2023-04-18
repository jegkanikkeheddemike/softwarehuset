package dtu.mennekser.softwarehuset.backend.streamDB.data;

import dtu.mennekser.softwarehuset.backend.streamDB.DataLayer;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Interface for en Serializeable lambda funktion til at finde data i DataLayer
 * @author Thor
 */
public interface Query<Schema extends DataLayer,T extends Serializable> extends Function<Schema, T>,Serializable {
}
