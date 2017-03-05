package com.darwindeveloper.calendar2weekslibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DARWIN on 1/3/2017.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewDayHolder> {

    private Context context;
    private ArrayList<Day> dias;
    private int HEADER = 0, DAY = 1;


    public CalendarAdapter(Context context, ArrayList<Day> dias) {
        this.context = context;
        this.dias = dias;
    }

    @Override
    public ViewDayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendar, parent, false);
        return new ViewDayHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewDayHolder holder, int position) {

        final Day dia = dias.get(position);

        if (dia.isHeader()) {
            holder.header.setVisibility(View.VISIBLE);
            holder.dia.setVisibility(View.GONE);
            holder.header.setPadding(dia.getPaddingHeader(), dia.getPaddingHeader(), dia.getPaddingHeader(), dia.getPaddingHeader());
            holder.eventos.setVisibility(View.GONE);
            holder.monthYear.setText(dia.getTextHeader());
            holder.header.setBackgroundColor(dia.getHeaderBackgroundColor());
            holder.header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    headerOnClickListener.headerOnClick(dia.getMonth(), dia.getYear(), dia.getTextHeader());
                }
            });

        } else {
            holder.header.setVisibility(View.GONE);
            if (dia.isValid()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dia.getDate());
                int nday = cal.get(Calendar.DAY_OF_MONTH);
                holder.dia.setText(nday + "");
                holder.dia.setVisibility(View.VISIBLE);
                holder.dia.setTextColor(dia.getTextColor());
                holder.itemView.setBackgroundColor(dia.getBackgroundColor());
                holder.eventos.setSolidColor(dia.getColorBadge());
                if (dia.isShowBadge()) {
                    holder.eventos.setVisibility(View.VISIBLE);
                    holder.eventos.setText(dia.getNumEvents()+"");
                } else {
                    holder.eventos.setVisibility(View.GONE);
                }

                holder.dia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dayOnClickListener.dayOnClick(dia);
                    }
                });
            } else {
                holder.dia.setVisibility(View.GONE);
                holder.eventos.setVisibility(View.GONE);
                holder.itemView.setBackgroundColor(dia.getBackgroundColor());

            }


        }
    }

    @Override
    public int getItemCount() {
        return dias.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (dias.get(position).isHeader()) {
            return HEADER;
        } else {
            return DAY;
        }
    }

    public class ViewDayHolder extends RecyclerView.ViewHolder {

        Button dia;
        CircularTextView eventos;
        TextView monthYear;
        LinearLayout header;

        public ViewDayHolder(View itemView) {
            super(itemView);
            dia = (Button) itemView.findViewById(R.id.textViewDay);
            eventos = (CircularTextView) itemView.findViewById(R.id.numEvents);
            header = (LinearLayout) itemView.findViewById(R.id.header);
            monthYear = (TextView) itemView.findViewById(R.id.textViewHeader);
        }
    }


    public interface DayOnClickListener {
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
        void dayOnClick(Day day);
    }

    public interface HeaderOnClickListener {
        /**
         * @param month      mes del hader
         * @param year       año del header
         * @param textHeader texto mostrado en el header
         */
        void headerOnClick(int month, int year, String textHeader);

    }

    private DayOnClickListener dayOnClickListener;
    private HeaderOnClickListener headerOnClickListener;

    public void setDayOnClickListener(DayOnClickListener dayOnClickListener) {
        this.dayOnClickListener = dayOnClickListener;
    }

    public void setHeaderOnClickListener(HeaderOnClickListener headerOnClickListener) {
        this.headerOnClickListener = headerOnClickListener;
    }
}
