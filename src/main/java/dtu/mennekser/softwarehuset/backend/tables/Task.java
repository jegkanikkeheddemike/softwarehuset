package dtu.mennekser.softwarehuset.backend.tables;

import dtu.mennekser.softwarehuset.backend.data.TableData;
import dtu.mennekser.softwarehuset.backend.data.TableID;

public class Task extends TableData<Task> {
    public TableID<User> assigned;
    public Task(TableID<User> assigned) {
        this.assigned = assigned;
    }
}
