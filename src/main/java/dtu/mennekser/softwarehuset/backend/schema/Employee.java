package dtu.mennekser.softwarehuset.backend.schema;

import java.io.Serializable;
import java.util.ArrayList;


/**
* @Author Katinka
*/
public final class Employee implements Serializable {
    public String name;
    public String realName;
    public final int id;
    public ArrayList<Vacation> vacations = new ArrayList<>();
    public ArrayList<SickLeave> sickLeave = new ArrayList<>();

    public Employee(String name, String realName, int id) {
        this.name = name; this.id = id; this.realName = realName;
    }
}
