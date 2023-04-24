package dtu.mennekser.softwarehuset.backend.schema;

public class SickLeave extends Activity{
        int startWeek;
        int endWeek;

    public SickLeave(int startWeek,int endWeek, int id) {
        super("Sick Leave", 0, startWeek, endWeek, id);
        this.startWeek = startWeek;
        this.endWeek = endWeek;
    }
}
