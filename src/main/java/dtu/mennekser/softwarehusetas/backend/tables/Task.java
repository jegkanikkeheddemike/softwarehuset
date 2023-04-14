package dtu.mennekser.softwarehusetas.backend.tables;

import dtu.mennekser.softwarehusetas.backend.data.TableData;
import dtu.mennekser.softwarehusetas.backend.data.TableID;

public class Task extends TableData<Task> {
    public TableID<User> assigned;
    public Task(TableID<User> assigned) {
        this.assigned = assigned;
    }
}
