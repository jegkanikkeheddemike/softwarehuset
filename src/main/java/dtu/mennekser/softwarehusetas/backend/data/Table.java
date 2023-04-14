package dtu.mennekser.softwarehusetas.backend.data;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class Table<T extends TableData<T>> implements Iterable<T>,Serializable {
    int generation = 0;
    Stack<Integer> freeIndexes = new Stack<>();
    ArrayList<T> content = new ArrayList<>();

    public TableID<T> insert(T value) {
        generation += 1;
        if (freeIndexes.empty()) {
            TableID<T> id = new TableID<T>(content.size(), generation, value.getClass().getSimpleName());
            value.id = id;
            content.add(value);
            return id;
        }
        int index = freeIndexes.pop();
        TableID<T> id = new TableID<T>(index, generation, value.getClass().getSimpleName());
        value.id = id;
        content.set(index, value);
        return id;
    }


    public T get(TableID<T> id) {
        T found = content.get(id.index);
        if (found == null) {
            return null;
        }
        if (found.Id().generation == id.generation) {
            return found;
        }
        return null;

    }

    public <R extends TableData<R>> Stream<Join<R, T>> joinOnId(Stream<R> stream, Function<R,TableID<T>> idGetter) {
        return stream.map(received -> new Join<>(received,get(idGetter.apply(received))));
    }

    public <R extends TableData<R>> Stream<Join<R, T[]>> joinWhere(Stream<R> stream, BiFunction<R,T,Boolean> filter, T[] dummy) {
        return stream.map(received -> {
            return new Join<>(
                    received,
                    stream().filter(item-> filter.apply(received,item)).toList().toArray(dummy)
                );
        });
    }

    public Stream<T> stream() {
        return content.stream().filter(Objects::nonNull);
    }

    @Override
    public String toString() {
        return content.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int iteratorIndex = 0;
            @Override
            public boolean hasNext() {
                while (iteratorIndex < content.size() && content.get(iteratorIndex) == null) {
                    iteratorIndex +=1;
                }
                return iteratorIndex < content.size();
            }

            @Override
            public T next() {
                T next =  content.get(iteratorIndex);
                iteratorIndex+=1;
                return next;
            }
        };
    }
}
