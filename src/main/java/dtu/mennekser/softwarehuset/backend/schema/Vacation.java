package dtu.mennekser.softwarehuset.backend.schema;

public class Vacation extends Activity {
    public Vacation(int startWeek, int endWeek, int id){
        super("Vacation",0,startWeek,endWeek, id);
    }
}
