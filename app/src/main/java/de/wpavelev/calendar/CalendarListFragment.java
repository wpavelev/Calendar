package de.wpavelev.calendar;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CalendarListFragment extends Fragment {

    MainViewModel mViewModel;

    public CalendarListFragment() {
    }


    public static CalendarListFragment newInstance() {
        CalendarListFragment fragment = new CalendarListFragment();
        Bundle args = new Bundle();
        /*args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_calendar, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            CalendarUtil calendarUtil = new CalendarUtil(getContext());
            CalendarListAdapter adapter = new CalendarListAdapter(mViewModel);
            adapter.submitList(calendarUtil.getCalendarList());
            recyclerView.setAdapter(adapter);

        }
        return view;
    }
}