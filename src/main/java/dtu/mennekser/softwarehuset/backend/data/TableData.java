package dtu.mennekser.softwarehuset.backend.data;

import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;

public abstract class TableData<T extends Serializable> implements Serializable {
    TableID<T> id;
    public TableID<T> Id() {
        return id;
    }

    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        writer.append("{ ");
        for (Field field : getAllFields(null, getClass())) {
            writer.append(field.getName());
            writer.append(": ");
            try {
                writer.append(field.get(this).toString());
            } catch (IllegalAccessException e) {
                writer.append("FAILED TO ACCESS");
            }
            writer.append(", ");
        }
        writer.append(" }");
        return writer.toString();
    }

    private LinkedList<Field> getAllFields(LinkedList<Field> fields, Class<?> type) {
        if (fields == null) {
            fields = new LinkedList<>();
        }
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }
}
