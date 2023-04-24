package dtu.mennekser.softwarehuset.backend.schema;

public class SickLeave extends Activity{
        int startWeek;
        int endWeek;

    public SickLeave(int startWeek, int id) {
        super("Sick Leave", 0, startWeek, startWeek+1, id);
        this.startWeek = startWeek;
        this.endWeek = startWeek +1;
    }
}
