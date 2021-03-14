package de.wpavelev.calendar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Iterator;
import java.util.Set;


public class EventListFragment extends Fragment {

    private static final String TAG = "EventListFragment";
    public static final String ARG_CALENDAR_ARRAY = "calendar_array";
    EventListAdapter mAdapter;

    public EventListFragment() {
    }


    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
 /*       args.putIntArray(ARG_CALENDAR_ARRAY, calendar);
        *//*args.putInt(ARG_COLUMN_COUNT, columnCount);*/
        fragment.setArguments(args);
        return fragment;
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_event, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.rv_event_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        CalendarUtil calendarUtil = new CalendarUtil(getContext());
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mAdapter = new EventListAdapter();

        mainViewModel.getVisibleCalendars().observe(getViewLifecycleOwner(),integers -> {

            int[] calendarArray = new int[integers.size()];

            int i = 0;
            for (Iterator<Integer> iterator = integers.iterator(); iterator.hasNext(); ) {
                Integer integer = iterator.next();
                calendarArray[i] = integer;
                i++;
            }

            mAdapter.submitList(calendarUtil.getEventList(calendarArray));
        });




        recyclerView.setAdapter(mAdapter);
        layoutManager.scrollToPosition(mAdapter.getPositionToday());

        FloatingActionButton scrollToday = view.findViewById(R.id.scroll_today);
        scrollToday.setOnClickListener(v -> layoutManager.scrollToPositionWithOffset(mAdapter.getPositionToday(), 10));


        return view;
    }
}