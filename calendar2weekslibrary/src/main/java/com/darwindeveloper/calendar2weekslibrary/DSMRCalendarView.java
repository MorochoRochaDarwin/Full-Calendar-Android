package com.darwindeveloper.calendar2weekslibrary;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by DARWIN on 1/3/2017.
 */

public class DSMRCalendarView extends LinearLayout implements CalendarAdapter.DayOnClickListener, CalendarAdapter.HeaderOnClickListener {

    public static final int SCROLL_NONE = -1, SCROLL_UP = 1, SCROLL_DOWN = 0;
    public static final int SPANISH = 0, ENGLISH = 1;

    private AppCompatActivity mActivity;

    private LinearLayout mainContent;

    private TextView textViewD, textViewL, textViewM, textViewX, textViewJ, textViewV, textViewS;


    private int currentDay, month, year, startYear, endYear;


    private String enero = "Enero";
    private String febrero = "Febrero";
    private String marzo = "Marzo";
    private String abril = "Abril";
    private String mayo = "Mayo";
    private String junio = "Junio";
    private String julio = "Julio";
    private String agosto = "Agosto";
    private String septiembre = "Septiembre";
    private String octubre = "Octubre";
    private String noviembre = "Noviembre";
    private String diciembre = "Deciembre";

    private RecyclerView recyclerViewDays;
    private ArrayList<Day> days = new ArrayList<>();
    private CalendarAdapter calendarAdapter;
    private boolean loading, calendarReady;
    private ProgressBar progressBar;

    public DSMRCalendarView(Context context) {
        super(context);
        mActivity = (AppCompatActivity) context;
    }

    int topBackgroundColor = Color.parseColor("#0099cc");
    int backgroundColorDays = Color.parseColor("#F5F5F5");//olor por defecto del background de cada dia
    int backgroundColorCalendar = Color.parseColor("#FFFFFF");//color de fondo del calendario
    int textColorDays = Color.parseColor("#0099CC");
    int backgroundColorCurrentDay = Color.parseColor("#0099CC");
    int headerBackgroundColor = Color.parseColor("#0099CC");
    int paddingHeader = 0;

    public DSMRCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (AppCompatActivity) context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DSMRCalendarView, 0, 0);
        topBackgroundColor = Color.parseColor(a.getString(R.styleable.DSMRCalendarView_topBackgroundColor));
        backgroundColorDays = Color.parseColor(a.getString(R.styleable.DSMRCalendarView_backgroundColorDays));
        backgroundColorCalendar = Color.parseColor(a.getString(R.styleable.DSMRCalendarView_backgroundColorCalendar));
        backgroundColorCurrentDay = Color.parseColor(a.getString(R.styleable.DSMRCalendarView_backgroundColorCurrentDay));
        headerBackgroundColor = Color.parseColor(a.getString(R.styleable.DSMRCalendarView_headerBackgroundColor));
        textColorDays = Color.parseColor(a.getString(R.styleable.DSMRCalendarView_labelDaysColor));
        paddingHeader = a.getDimensionPixelSize(R.styleable.DSMRCalendarView_paddingHeader, 0);
        //años para el calendar
        startYear = a.getInt(R.styleable.DSMRCalendarView_startYear, 2017);
        endYear = a.getInt(R.styleable.DSMRCalendarView_endYear, 2017);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_view, this, true);

    }


    private void init() {


        //inicializamos las vistas
        mainContent = (LinearLayout) findViewById(R.id.mainContent);
        textViewD = (TextView) findViewById(R.id.textViewD);
        textViewL = (TextView) findViewById(R.id.textViewL);
        textViewM = (TextView) findViewById(R.id.textViewM);
        textViewX = (TextView) findViewById(R.id.textViewX);
        textViewJ = (TextView) findViewById(R.id.textViewJ);
        textViewV = (TextView) findViewById(R.id.textViewV);
        textViewS = (TextView) findViewById(R.id.textViewS);

        recyclerViewDays = (RecyclerView) findViewById(R.id.recyclerview_2weeks);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        //inicialixamos el mes actual y el mes posterior , lo mismo con los años
        month = getCurrentMonth();
        year = getCurrentYear();

        currentDay = getCurrentDayMonth();


        calendarAdapter = new CalendarAdapter(mActivity, days);
        calendarAdapter.setDayOnClickListener(this);
        calendarAdapter.setHeaderOnClickListener(this);

        final GridLayoutManager glm = new GridLayoutManager(mActivity, 7);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (days.get(position).isHeader()) ? 7 : 1;
            }
        });
        recyclerViewDays.setLayoutManager(glm);
        recyclerViewDays.setAdapter(calendarAdapter);


        recyclerViewDays.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    calendarOnScrollListener.calendarOnScrolled(SCROLL_NONE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    calendarOnScrollListener.calendarOnScrolled(SCROLL_UP);

                    int visibleItemCount = glm.getChildCount();
                    int totalItemCount = glm.getItemCount();
                    int pastVisiblesItems = glm.findFirstVisibleItemPosition();

                    if (!loading) {//si no hay una tarea pendiente
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {


                            int posLastDay = getLastDayCalendar();
                            if (posLastDay != -1) {
                                loading = true;
                                int tmp_y = days.get(posLastDay).getDate().getYear();

                                new AddMonths(tmp_y + 1).execute();

                            }

                        }
                    }

                }
                if (dy < 0) {
                    calendarOnScrollListener.calendarOnScrolled(SCROLL_DOWN);
                }
            }
        });


    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();

        recyclerViewDays.setBackgroundColor(backgroundColorCalendar);
        mainContent.setBackgroundColor(topBackgroundColor);


    }

    private void fillUpCalendar() {

        for (int year = startYear; year <= endYear; year++) {
            for (int month = 0; month < 12; month++) {
                fillUpMonth(month, year);
            }
        }

    }


    private void fillUpMonth(int month, int year) {
        //almaceno el nombre del primer  dia del mes y año en cuestion
        String nameFirstDay = getNameDay(1, month, year);

        //textViewMonth.setText(nameFirstDay);/** end after 1 month from now */

        int blankSpaces = 0;
        switch (nameFirstDay) {
            case "Monday":
                blankSpaces = 1;
                break;
            case "Tuesday":
                blankSpaces = 2;
                break;
            case "Wednesday":
                blankSpaces = 3;
                break;
            case "Thursday":
                blankSpaces = 4;
                break;
            case "Friday":
                blankSpaces = 5;
                break;
            case "Saturday":
                blankSpaces = 6;
                break;
        }

        days.add(new Day(true, year, month, getStringMonth(month) + " " + year, headerBackgroundColor, paddingHeader));
        int space = 0;
        for (int i = 0; i < blankSpaces; i++) {
            days.add(new Day(backgroundColorDays));
            space++;
        }

        int numberOfDaysMonthYear = getNumberOfDaysMonthYear(year, month);
        for (int i = 1; i <= numberOfDaysMonthYear; i++) {

            if (this.year == year && this.month == month && this.currentDay == i) {
                days.add(new Day(new Date(year, month, i), backgroundColorCurrentDay, Color.parseColor("#FFFFFF")));
            } else {
                days.add(new Day(new Date(year, month, i), false, 0, textColorDays, backgroundColorDays));
            }


            space++;
            if (space == 7)
                space = 0;
        }

        if (space != 0) {
            for (int i = 0; i < 7 - space; i++) {
                days.add(new Day(backgroundColorDays));
            }
        }

    }


    /**
     * permite cambiar el lenguaje de como se visualizan los meses y dias del calendario
     *
     * @param language SPANISH=0, ENGLISH=1;
     */
    public void setLanguage(int language) {
        if (language == 1) {//si el idioma es el ingles
            textViewL.setText("M");
            textViewM.setText("T");
            textViewX.setText("W");
            textViewJ.setText("T");
            textViewV.setText("F");
            textViewS.setText("S");
            textViewD.setText("S");

            enero = "January";
            febrero = "February";
            marzo = "March";
            abril = "April";
            mayo = "May";
            junio = "June";
            julio = "July";
            agosto = "August";
            septiembre = "September";
            octubre = "October";
            noviembre = "November";
            diciembre = "December";

        } else {

            textViewL.setText("L");
            textViewM.setText("M");
            textViewX.setText("X");
            textViewJ.setText("J");
            textViewV.setText("V");
            textViewS.setText("S");
            textViewD.setText("D");

            enero = "Enero";
            febrero = "Febrero";
            marzo = "Marzo";
            abril = "Abril";
            mayo = "Mayo";
            junio = "Junio";
            julio = "Julio";
            agosto = "Agosto";
            septiembre = "Septiembre";
            octubre = "Octubre";
            noviembre = "Noviembre";
            diciembre = "Deciembre";
        }

        if (calendarAdapter.getItemCount() > 0) {
            calendarAdapter.notifyItemRangeRemoved(0, calendarAdapter.getItemCount() - 1);
            calendarAdapter.notifyDataSetChanged();
            days.clear();
        }


        new Fill().execute();
    }

    private String getStringMonth(int numMonth) {
        switch (numMonth) {
            case 0:
                return enero;

            case 1:
                return febrero;

            case 2:
                return marzo;

            case 3:
                return abril;

            case 4:
                return mayo;

            case 5:
                return junio;

            case 6:
                return julio;

            case 7:
                return agosto;

            case 8:
                return septiembre;

            case 9:
                return octubre;

            case 10:
                return noviembre;

            case 11:
                return diciembre;

        }
        return enero;
    }


    public void setBackgroundColorCalendar2Weeks(int color) {
        //separator.setBackgroundColor(color);
    }


    /**
     * retorna el mes actual iniciando desde 0=enero
     *
     * @return
     */
    public int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }


    /**
     * retorna el año actual
     *
     * @return
     */
    public int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * retorna el dia del mes actual
     *
     * @return
     */
    public int getCurrentDayMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * calcula el numero de dias que tiene un mes de una año especifico
     *
     * @param year
     * @param month
     * @return
     */
    public int getNumberOfDaysMonthYear(int year, int month) {
        Calendar mycal = new GregorianCalendar(year, month, 1);
        return mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     * nos retorna el nombre de un dia especifico de una año (en ingles o español segun la configuracion)
     *
     * @param day
     * @param month
     * @param year
     * @return nombre del dia
     */
    public String getNameDay(int day, int month, int year) {
        Date date1 = (new GregorianCalendar(year, month, day)).getTime();
        // Then get the day of week from the Date based on specific locale.
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date1);
    }


    private int posDay(Date date) {
        int pos = -1;
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).isHeader()) {
                if (days.get(i).getYear() == date.getYear() && days.get(i).getMonth() == date.getMonth()) {
                    pos = i;
                    break;
                }
            }

        }

        return pos;
    }


    /**
     * retorna el ultimo dia en el calendario actual
     *
     * @return
     */
    private int getLastDayCalendar() {
        int pos = -1;
        for (int i = days.size() - 1; i >= 0; i--) {
            if (days.get(i).isValid()) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    /**
     * un objeto de tipo day para obtener la fecha (año,mes,dia) con un objeto calendar
     * atraves del metrodo getDate() de la clase Day y el numero de eventos con el metodo
     * getNumEvents(), mostrar le badge de eventos isShowBadge(),setShowBadge(boolean showBadge)
     * color del badge de eventos getColorBadge(), setColorBadge(int color)
     * <p>
     * otros metodos como setBackgroundColor(int backgroundColor) y getBackgroundColor() color del fondo del numero de dia del mes
     * setTextColor(int textColor) y getTextColor() color del texto numro de dia del mes
     *
     * @param day
     */
    @Override
    public void dayOnClick(Day day) {
        calendarOnClickListener.calendarOnClickDay(day);
    }

    /**
     * @param month      mes del hader
     * @param year       año del header
     * @param textHeader texto mostrado en el header
     */
    @Override
    public void headerOnClick(int month, int year, String textHeader) {
        calendarOnClickListener.calendarOnClickHeader(month, year, textHeader);
    }


    public interface CalendarOnScrollListener {
        /**
         * retorna el estado (scroll) del calendar
         *
         * @param state SCROLL_NONE=-1, SCROLL_UP=1,SCROLL_DOWN=0
         */
        void calendarOnScrolled(int state);
    }

    private CalendarOnScrollListener calendarOnScrollListener;

    public void setCalendarOnScrollListener(CalendarOnScrollListener calendarOnScrollListener) {
        this.calendarOnScrollListener = calendarOnScrollListener;
    }


    private class Fill extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            fillUpCalendar();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            calendarReady = false;
            progressBar.setVisibility(VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(GONE);
            calendarReady = true;
            if (days.size() > 0) {
                calendarAdapter.notifyItemRangeInserted(0, days.size() - 1);
                calendarAdapter.notifyDataSetChanged();
            }

        }
    }


    private class AddMonths extends AsyncTask<Void, Void, Void> {

        int year;
        int lastSize;

        public AddMonths(int year) {
            this.year = year;
            lastSize = days.size();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            calendarReady = false;
            progressBar.setVisibility(VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //agregamos todos los meses del año
            for (int i = 0; i < 12; i++) {
                fillUpMonth(i, year);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(GONE);
            calendarReady = true;
            calendarAdapter.notifyItemRangeInserted(lastSize - 1, days.size() - 1);
            calendarAdapter.notifyDataSetChanged();
            loading = false;
        }
    }

    public interface CalendarOnClickListener {
        /**
         * @param month      mes del hader
         * @param year       año del header
         * @param textHeader texto mostrado en el header
         */
        void calendarOnClickHeader(int month, int year, String textHeader);

        /**
         * un objeto de tipo day para obtener la fecha (año,mes,dia) con un objeto calendar
         * atraves del metrodo getDate() de la clase Day y el numero de eventos con el metodo
         * getNumEvents(), mostrar le badge de eventos isShowBadge(),setShowBadge(boolean showBadge)
         * color del badge de eventos getColorBadge(), setColorBadge(int color)
         * <p>
         * otros metodos como setBackgroundColor(int backgroundColor) y getBackgroundColor() color del fondo del numero de dia del mes
         * setTextColor(int textColor) y getTextColor() color del texto numro de dia del mes
         *
         * @param day
         */
        void calendarOnClickDay(Day day);
    }


    private CalendarOnClickListener calendarOnClickListener;

    public void setCalendarOnClickListener(CalendarOnClickListener calendarOnClickListener) {
        this.calendarOnClickListener = calendarOnClickListener;
    }

    /**
     * muestra un badge con un numero el la parte superior derecha de un dia en el calendario
     *
     * @param position posicion del dia en  el calendario
     * @param show     si se muestra o se esconde el badge
     */
    public void isShowDayBadge(int position, boolean show) {
        if (days.get(position).isValid()) {
            days.get(position).setShowBadge(show);
            calendarAdapter.notifyItemChanged(position);
            calendarAdapter.notifyDataSetChanged();
        }

    }

    /**
     * muestra un badge con un numero el la parte superior derecha de un dia en el calen
     *
     * @param position   posicion del dia en  el calendario
     * @param show       si se muestra o se esconde el badge
     * @param badgeColor color de fondo para el badge
     */
    public void isShowDayBadge(int position, boolean show, int badgeColor) {
        if (days.get(position).isValid()) {
            days.get(position).setShowBadge(show);
            days.get(position).setColorBadge(badgeColor);
            calendarAdapter.notifyItemChanged(position);
            calendarAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param date pasamos una fecha determinada (ejemplo: new Date(2017, Calendar.MAY, 13) )
     * @return retorna la posicion en el calendar (NOTA si no se encontro el dia en el calendar retornara -1)
     */
    public int dayPosition(Date date) {
        int pos = -1;

        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).isValid()) {
                if (date.getTime() == days.get(i).getDate().getTime()) {
                    pos = i;
                    break;
                }
            }
        }
        return pos;
    }


    /**
     * cambia el numero del badge de un dia en el calendario
     *
     * @param position posicion del badge del dia
     * @param number   numero a mostrar en el badge
     */
    public void setNumberDayBadge(int position, int number) {
        if (days.get(position).isValid()) {
            days.get(position).setNumEvents(number);
            calendarAdapter.notifyItemChanged(position);
            calendarAdapter.notifyDataSetChanged();
        }
    }


    /**
     * es importante antes de realizar cualquier accion en el calendar
     * (como mostrar o modificar un badge o realizar un scroll a una fecha determinada) verificar el estado del
     * calendario
     *
     * @return retorna true si el calendario esta listo para ser mostrato o para realizar cualquier accion
     */
    public boolean isCalendarReady() {
        return calendarReady;
    }


    /**
     * scroll hacia el dia mes y año desdeados
     *
     * @param year
     * @param month
     * @param currentDay
     */
    public void scrollToDay(int year, int month, int currentDay) {

        int posScroll = posDay(new Date(year, month, currentDay));
        if (posScroll != -1) {
            recyclerViewDays.scrollToPosition(posScroll);
        }
    }

    /**
     * scroll hacia una fecha desada atravez de un objeto Date
     *
     * @param date
     */
    public void scrollToDay(Date date) {
        int year = date.getYear();
        int month = date.getMonth();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int nday = cal.get(Calendar.DAY_OF_MONTH);

        int posScroll = posDay(new Date(year, month, nday));
        if (posScroll != -1) {
            recyclerViewDays.scrollToPosition(posScroll);
        }
    }


}
