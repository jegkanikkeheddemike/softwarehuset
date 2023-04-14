package dtu.mennekser.softwarehusetas.backend.data;

import dtu.mennekser.softwarehusetas.backend.tables.Tables;

import java.io.Serializable;
import java.util.function.Function;

public interface DataFilter<T extends Serializable> extends Function<Tables, T>,Serializable {
}
