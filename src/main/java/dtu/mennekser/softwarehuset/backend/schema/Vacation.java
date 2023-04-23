package dtu.mennekser.softwarehuset.backend.schema;

public class Vacation extends Activity {
    public int startWeek;
    public int endWeek;

    public Vacation(int startWeek, int endWeek, int id){
        super("Vacation",(endWeek-startWeek)*24,startWeek,endWeek, id);
        this.startWeek = startWeek;
        this.endWeek = endWeek;
    }
}
