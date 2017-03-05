package com.darwindeveloper.calendar2weeksview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.darwindeveloper.calendar2weekslibrary.DSMRCalendarView;
import com.darwindeveloper.calendar2weekslibrary.Day;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DSMRCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = (DSMRCalendarView) findViewById(R.id.calendar);
        calendarView.setLanguage(DSMRCalendarView.ENGLISH);
        calendarView.setBackgroundColorCalendar2Weeks(getResources().getColor(android.R.color.holo_green_dark));

        calendarView.setCalendarOnScrollListener(new DSMRCalendarView.CalendarOnScrollListener() {
            /**
             * retorna el estado (scroll) del calendar
             * @param state SCROLL_NONE=-1, SCROLL_UP=1,SCROLL_DOWN=0
             */
            @Override
            public void calendarOnScrolled(int state) {
                Log.i("scroll", "" + state);
            }
        });


        calendarView.setCalendarOnClickListener(new DSMRCalendarView.CalendarOnClickListener() {
            @Override
            public void calendarOnClickHeader(int month, int year, String textHeader) {
                Toast.makeText(MainActivity.this, textHeader, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void calendarOnClickDay(Day day) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(day.getDate());
                int nday = cal.get(Calendar.DAY_OF_MONTH);
                //NOTA  use el metodo .getDate().getMonth() para obtener el mes
                //NOTA  use el metodo .getDate().getYear() para obtener el año
                //no olvide que los meses en java inician desde 0=enero,1=febrero,2,3,4,5,6,7,8,9,10,11
                Toast.makeText(MainActivity.this, nday + "/" + (day.getDate().getMonth() + 1) + "/" + day.getDate().getYear(), Toast.LENGTH_SHORT).show();
            }
        });

        mostrarBadge();
        scrollToCurrentDay();

    }

    /**
     * este metodo usa una tarea pues el calendario talvez aun no este listo
     */
    private synchronized void mostrarBadge() {
        new Thread(new Runnable() {
            public void run() {
                 /*
       es importante antes de realizar cualquier accion en el calendar
       (como mostrar o modificar un badge o realizar un scroll a una fecha determinada) verificar el estado del
       calendario
       */
                boolean status = calendarView.isCalendarReady();
                while (!status) {
                    status = calendarView.isCalendarReady();
                }

                //el metodo dayPosition(date) nos devuelve la posicion de la fecha (date) en el calendar
                int myBirthday = calendarView.dayPosition(new Date(2017, Calendar.SEPTEMBER, 18));
                if (myBirthday != 1) {
                    //mostramos el badge (con numer 2) en el dia 18 de septiembre del 2017
                    calendarView.setNumberDayBadge(myBirthday, 2);
                    //calendarView.isShowDayBadge(myBirthday, true);
                    calendarView.isShowDayBadge(myBirthday, true, Color.parseColor("#ff0000"));
                }
            }
        }).start();


    }


    private synchronized void scrollToCurrentDay() {
        new Thread(new Runnable() {
            public void run() {
                 /*
       es importante antes de realizar cualquier accion en el calendar
       (como mostrar o modificar un badge o realizar un scroll a una fecha determinada) verificar el estado del
       calendario
       */
                boolean status = calendarView.isCalendarReady();
                while (!status) {
                    status = calendarView.isCalendarReady();
                }
                //scrollToDay(año,mes, dia_del_mes)
                calendarView.scrollToDay(calendarView.getCurrentYear(), calendarView.getCurrentMonth(), calendarView.getCurrentDayMonth());

            }
        }).start();


    }
}
