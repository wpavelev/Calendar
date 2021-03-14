package de.wpavelev.calendar;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";
    MutableLiveData<Set<Integer>> mVisibleCalendars = new MutableLiveData<>();


    public MainViewModel(@NonNull Application application) {
        super(application);

        mVisibleCalendars.setValue(new HashSet<Integer>());


    }

    public MutableLiveData<Set<Integer>> getVisibleCalendars() {
        return mVisibleCalendars;
    }

    public void setVisibleCalendars(Set<Integer> visibleCalendars) {
        Log.d(TAG, "setVisibleCalendars() called with: visibleCalendars.size = [" + visibleCalendars.size() + "]");
        this.mVisibleCalendars.setValue(visibleCalendars);
    }

    public void addVisibleCalendar(int calId) {
        Log.d(TAG, "addVisibleCalendar() called with: calId = [" + calId + "]");
        Set<Integer> ids = mVisibleCalendars.getValue();
        ids.add(calId);
        mVisibleCalendars.setValue(ids);


    }

    public void removeVisibleCalendar(int calId) {
        Log.d(TAG, "removeVisibleCalendar() called with: calId = [" + calId + "]");
        Set<Integer> ids = mVisibleCalendars.getValue();
        ids.remove(calId);
        mVisibleCalendars.setValue(ids);

    }

    public boolean isCalendarVisible(int id) {
        return mVisibleCalendars.getValue().contains(id);
    }


}
