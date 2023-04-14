package dtu.mennekser.softwarehuset.backend.data;

import java.io.Serializable;

public final class TableID<T> implements Serializable {
    public final int index;
    public final int generation;

    String tableName;

    public TableID(int index, int generation, String tableName) {
        this.index = index;
        this.generation = generation;
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return tableName + " " + generation;
    }
}
