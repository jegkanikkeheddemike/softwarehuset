package dtu.mennekser.softwarehuset.backend.schema;

public class SickLeave extends Activity{
    public SickLeave(int startWeek,int endWeek, int id) {
        super("Sick Leave", 0, startWeek, endWeek, id);
    }
}
