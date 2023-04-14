package dtu.mennekser.softwarehuset.backend.tables;

import dtu.mennekser.softwarehuset.backend.data.TableData;
import dtu.mennekser.softwarehuset.backend.data.TableID;

public class Task extends TableData<Task> {
    public TableID<Employee> assigned;
    public Task(TableID<Employee> assigned) {
        this.assigned = assigned;
    }
}
