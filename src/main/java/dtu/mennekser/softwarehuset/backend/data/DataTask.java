package dtu.mennekser.softwarehuset.backend.data;

import dtu.mennekser.softwarehuset.backend.tables.Tables;

import java.io.Serializable;
import java.util.function.Consumer;

public interface DataTask extends Consumer<Tables>, Serializable {
}
