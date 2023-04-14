package dtu.mennekser.softwarehuset.backend.data;

import dtu.mennekser.softwarehuset.backend.db.Database;

import java.io.Serializable;
import java.util.function.Consumer;

public interface DataTask extends Consumer<Database>, Serializable {
}
