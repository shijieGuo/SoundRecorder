package recodfragment;

import java.util.Calendar;

public class CalendarDemo {

    public static CalendarDemo calendarDemo=new CalendarDemo();
    private Calendar calendar = null;
    private CalendarDemo() {
        this.calendar = Calendar.getInstance();
    }


    public String getDate() {

        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH) + 1;

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24小时表示

        int minute = calendar.get(Calendar.MINUTE);

        int second = calendar.get(Calendar.SECOND);

        int ms = calendar.get(Calendar.MILLISECOND);

        String time;
        time =year+"-"+month+"-"+day+"_"+ hour + "-" +minute + "-" + second;
        return time;
    }
}

