package dtu.mennekser.softwarehuset.backend.tables;

import dtu.mennekser.softwarehuset.backend.data.TableData;

public final class Employee extends TableData<Employee> {
    public String username;
    public String realName;

    public Employee(String username, String realName) {
        this.username = username;
        this.realName = realName;
    }
}
