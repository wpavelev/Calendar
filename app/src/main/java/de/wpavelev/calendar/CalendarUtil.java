package de.wpavelev.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarUtil {

    private static final String TAG = "CalendarUtil";
    private Context mContext;

    public CalendarUtil(Context context) {
        mContext = context;
    }


    public static final String[] CAL_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };


    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID,
            CalendarContract.Events.CALENDAR_ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
    };



    public List<CalInfo> getAllCalendars() {

        Cursor cur = null;
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        /*String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"hera@example.com", "com.example",
                "hera@example.com"};*/


        cur = cr.query(uri, CAL_PROJECTION, null, null, null);



        List<CalInfo> calInfoList = new ArrayList<>();
        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);

            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            CalInfo calInfo = new CalInfo(calID, displayName, accountName, ownerName);

            calInfoList.add(calInfo);

        }

        cur.close();
        return calInfoList;
    }

    public List<EventInfo> getEventList(long calendarId) {
        //run query
        Cursor cur = null;

        String calendarIdString = "" + calendarId;
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        String selection = "(" + CalendarContract.Events.CALENDAR_ID + " =?)";
        String[] selectionArgs = new String[]{
                calendarIdString
        };

        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);


        // Use the cursor to step through the returned records

        List<EventInfo> eventInfoList = new ArrayList<>();
        while (cur.moveToNext()) {

            // Get the field values
            long id = cur.getLong(cur.getColumnIndex(CalendarContract.Events._ID));
            String title = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
            long start = cur.getLong(cur.getColumnIndex(CalendarContract.Events.DTSTART));
            long end = cur.getLong(cur.getColumnIndex(CalendarContract.Events.DTEND));

            EventInfo eventInfo = new EventInfo(id, title, start, end);

            eventInfoList.add(eventInfo);

        }

        cur.close();
        return eventInfoList;
    }

    public List<EventInfo> getEventList() {
        //run query
        Cursor cur = null;


        ContentResolver cr = mContext.getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        cur = cr.query(uri, EVENT_PROJECTION, null, null, CalendarContract.Events.DTSTART);


        // Use the cursor to step through the returned records
        List<EventInfo> eventInfoList = new ArrayList<>();
        while (cur.moveToNext()) {

            // Get the field values
            long id = cur.getLong(cur.getColumnIndex(CalendarContract.Events._ID));
            String title = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
            long start = cur.getLong(cur.getColumnIndex(CalendarContract.Events.DTSTART));
            long end = cur.getLong(cur.getColumnIndex(CalendarContract.Events.DTEND));

            EventInfo eventInfo = new EventInfo(id, title, start, end);

            eventInfoList.add(eventInfo);

        }

        cur.close();
        return eventInfoList;
    }

    private void updateEventTitle(long eventId, String title) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.TITLE, title);
        updateEvent(eventId, contentValues);
    }

    public int updateEvent(long eventId, ContentValues contentValues) {
        ContentResolver cr = mContext.getContentResolver();
        Uri updateUri;
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
        int rows = cr.update(updateUri, contentValues, null, null);
        Log.d(TAG, "updateEvent: Row upated: " + rows);
        return rows;

    }


    protected class EventInfo {

        private String mTitle;
        private long start, end, id;

        public EventInfo(long id, String title, long start, long end) {
            this.id = id;
            mTitle = title;
            this.start = start;
            this.end = end;
        }

        public String getTitle() {
            return mTitle;
        }

        public long getStart() {
            return start;
        }

        public long getEnd() {
            return end;
        }

        public long getId() {
            return id;
        }
    }

    protected class CalInfo {
        long mId;
        String mName, mAccount, mOwner;

        public CalInfo(long id, String name, String account, String owner) {
            mId = id;
            mName = name;
            mAccount = account;
            this.mOwner = owner;
        }

        public long getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }

        public String getAccount() {
            return mAccount;
        }

        public String getMowner() {
            return mOwner;
        }
    }

}

