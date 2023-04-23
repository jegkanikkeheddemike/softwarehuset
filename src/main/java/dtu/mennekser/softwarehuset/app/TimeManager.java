package dtu.mennekser.softwarehuset.app;

import java.util.Calendar;

public class TimeManager {
    public static int getWeek() {
        return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    }
}
