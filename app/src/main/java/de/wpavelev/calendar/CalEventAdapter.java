package de.wpavelev.calendar;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.Calendar;
import java.util.List;


public class CalEventAdapter extends RecyclerView.Adapter<CalEventAdapter.ViewHolder> {

    private final List<CalendarUtil.EventInfo> mValues;

    public CalEventAdapter(List<CalendarUtil.EventInfo> items) {
        mValues = items;
    }

    private String getDate(Calendar calendar) {
        String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ?
                "0" + calendar.get(Calendar.DAY_OF_MONTH) :
                "" + calendar.get(Calendar.DAY_OF_MONTH);
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);

        String year = String.valueOf(calendar.get(Calendar.YEAR));

        return day + "." + month + "." + year;

    }

    private String getTime(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String sHour = hour < 10 ? "0" + hour : String.valueOf(hour);
        String sMinute = minute < 10 ? "0" + minute : String.valueOf(minute);

        return sHour + ":" + sMinute;
    }

    public int getPositionToday() {
        Calendar today = Calendar.getInstance();

        for (int i = 0; i < mValues.size(); i++) {
            if (mValues.get(i).getStart() > today.getTimeInMillis()) {
                return i - 1;
            }
        }
        return 0;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CalendarUtil.EventInfo event = mValues.get(position);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(event.getStart());


        holder.date.setText(getDate(cal));
        holder.content.setText(event.getTitle());
        holder.time.setText(getTime(cal));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, content;
        public ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            content = view.findViewById(R.id.content);
            time = view.findViewById(R.id.time);

        }


    }
}