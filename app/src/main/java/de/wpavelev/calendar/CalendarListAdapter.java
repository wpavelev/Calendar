package de.wpavelev.calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


public class CalendarListAdapter extends ListAdapter<CalendarUtil.CalInfo, CalendarListAdapter.ViewHolder> {
    private static final String TAG = "CalendarListAdapter";

    private final MainViewModel mViewModel;


    public static final DiffUtil.ItemCallback<CalendarUtil.CalInfo> DIFF_CALLBACK = new DiffUtil.ItemCallback<CalendarUtil.CalInfo>() {
        @Override
        public boolean areItemsTheSame(@NonNull CalendarUtil.CalInfo oldItem, @NonNull CalendarUtil.CalInfo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CalendarUtil.CalInfo oldItem, @NonNull CalendarUtil.CalInfo newItem) {
            return oldItem.getAccount().equals(newItem.getAccount()) &&
                    oldItem.getColor() == newItem.getColor() &&
                    oldItem.getMowner().equals(newItem.getMowner()) &&
                    oldItem.getName().equals(newItem.getName());

        }
    };

    public CalendarListAdapter(MainViewModel viewModel) {
        super(DIFF_CALLBACK);

        mViewModel = viewModel;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
        CalendarUtil.CalInfo item = getItem(position);

        holder.mCalInfo = item;

        holder.mIdView.setText(String.valueOf(item.getId()));
        holder.mContentView.setText(item.getName());
        holder.mView.setBackgroundColor(item.getColor());
        holder.mCheckBox.setChecked(mViewModel.isCalendarVisible((int) item.getId()));

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CalendarUtil.CalInfo mCalInfo;
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        final CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mView.setOnClickListener(this);
            mIdView = (TextView) mView.findViewById(R.id.item_calendar_list_id);

            mContentView = (TextView) mView.findViewById(R.id.item_calendar_list_calendar);

            mCheckBox = view.findViewById(R.id.item_calendar_list_checkBox);
            mCheckBox.setOnClickListener(this);
        }




        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: ");
            if (v.getId() != mCheckBox.getId()) {
                mCheckBox.setChecked(!mCheckBox.isChecked());
            }
            if (mCheckBox.isChecked()) {
                mViewModel.addVisibleCalendar((int) mCalInfo.getId());
            } else {
                mViewModel.removeVisibleCalendar((int) mCalInfo.getId());
            }
        }
    }
}