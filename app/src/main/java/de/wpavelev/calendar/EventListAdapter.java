package de.wpavelev.calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class EventListAdapter extends ListAdapter<CalendarUtil.EventInfo, EventListAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<CalendarUtil.EventInfo> DIFF_CALLBACK = new DiffUtil.ItemCallback<CalendarUtil.EventInfo>() {
        @Override
        public boolean areItemsTheSame(@NonNull CalendarUtil.EventInfo oldItem, @NonNull CalendarUtil.EventInfo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CalendarUtil.EventInfo oldItem, @NonNull CalendarUtil.EventInfo newItem) {
            return oldItem.getCalId() == newItem.getCalId() &&
                    ((oldItem.getTitle() == null && newItem.getTitle() == null) || (oldItem.getTitle().equals(newItem.getTitle()))) &&
                    oldItem.getStart() == newItem.getStart() &&
                    oldItem.getEnd() == newItem.getEnd();
        }
    };


    public EventListAdapter() {
        super(DIFF_CALLBACK);

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

        for (int i = 0; i < getItemCount(); i++) {
            if (getItem(i).getStart() > today.getTimeInMillis()) {
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
        CalendarUtil.EventInfo event = getItem(position);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(event.getStart());


        holder.mView.setBackgroundColor(event.getColor());

        holder.date.setText(getDate(cal));
        holder.content.setText(event.getTitle());
        holder.time.setText(getTime(cal));
        if (event.isAllDayEvent()) {
            holder.time.setVisibility(View.GONE);
        }

        int today = getPositionToday();

        if (position == today) {
            holder.mDivider.setVisibility(View.VISIBLE);
            holder.mDivider.setBackgroundColor(Color.RED);
        } else {
            holder.mDivider.setVisibility(View.INVISIBLE);
        }


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, content;

        View mView, mDivider;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDivider = view.findViewById(R.id.item_event_divider);
            date = view.findViewById(R.id.date);
            content = view.findViewById(R.id.item_calendar_list_calendar);
            time = view.findViewById(R.id.time);

        }


    }
}