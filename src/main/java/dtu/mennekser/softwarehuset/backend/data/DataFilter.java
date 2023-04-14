package dtu.mennekser.softwarehuset.backend.data;

import dtu.mennekser.softwarehuset.backend.db.Database;

import java.io.Serializable;
import java.util.function.Function;

public interface DataFilter<T extends Serializable> extends Function<Database, T>,Serializable {
}
