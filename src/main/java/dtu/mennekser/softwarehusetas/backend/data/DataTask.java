package dtu.mennekser.softwarehusetas.backend.data;

import dtu.mennekser.softwarehusetas.backend.tables.Tables;

import java.io.Serializable;
import java.util.function.Consumer;

public interface DataTask extends Consumer<Tables>, Serializable {
}
