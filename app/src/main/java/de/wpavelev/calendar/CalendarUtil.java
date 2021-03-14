package de.wpavelev.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarUtil {

    private static final String TAG = "CalendarUtil";
    private Context mContext;

    String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID,
            CalendarContract.Events.CALENDAR_ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.CALENDAR_COLOR,
            CalendarContract.Events.ALL_DAY
    };

    public static final int EVENT_PROJECTION_ID = 0;
    public static final int EVENT_PROJECTION_CALENDAR_ID = 1;
    public static final int EVENT_PROJECTION_TITLE = 2;
    public static final int EVENT_PROJECTION_DTSTART = 3;
    public static final int EVENT_PROJECTION_DTEND = 4;
    public static final int EVENT_PROJECTION_CALENDAR_COLOR = 5;
    public static final int EVENT_PROJECTION_ALLDAYEVENT = 6;

    public CalendarUtil(Context context) {
        mContext = context;
    }


    public List<CalInfo> getCalendarList() {

        Cursor cur = null;
        ContentResolver cr = mContext.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        /*String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"hera@example.com", "com.example",
                "hera@example.com"};*/

        String[] CAL_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.OWNER_ACCOUNT,
                CalendarContract.Calendars.CALENDAR_COLOR
        };
        cur = cr.query(uri, CAL_PROJECTION, null, null, null);


        List<CalInfo> calInfoList = new ArrayList<>();
        while (cur.moveToNext()) {
            long calID = 0;
            String displayName, accountName, ownerName;
            int color;


            int PROJECTION_ID_INDEX = 0;
            int PROJECTION_ACCOUNT_NAME_INDEX = 1;
            int PROJECTION_DISPLAY_NAME_INDEX = 2;
            int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
            int PROJECTION_COLOR = 4;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);

            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            color = cur.getInt(PROJECTION_COLOR);



            CalInfo calInfo = new CalInfo(calID, displayName, accountName, ownerName, color);

            calInfoList.add(calInfo);

        }

        cur.close();
        return calInfoList;
    }

    public List<EventInfo> getEventList(int[] calendarId) {
        //run query
        Cursor cur = null;


        ContentResolver cr = mContext.getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        StringBuilder selectionStringBuilder = new StringBuilder();

        selectionStringBuilder.append("(");

        String[] selectionArgs = new String[calendarId.length];
        for (int i = 0; i < calendarId.length; i++) {

            selectionArgs[i] = String.valueOf(calendarId[i]);

            selectionStringBuilder.append(CalendarContract.Events.CALENDAR_ID + " =?");

            if (i < calendarId.length - 1) { //if item is not the last one: add divider
                selectionStringBuilder.append(" OR ");
            } else { //else finalize string
                selectionStringBuilder.append(")");
            }
        }


        String selection = selectionStringBuilder.toString();

        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, CalendarContract.Events.DTSTART);


        // Use the cursor to step through the returned records
        List<EventInfo> eventInfoList = new ArrayList<>();
        while (cur.moveToNext()) eventInfoList.add(getEventInfosFromCursor(cur));

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
        while (cur.moveToNext()) eventInfoList.add(getEventInfosFromCursor(cur));

        cur.close();
        return eventInfoList;
    }

    private EventInfo getEventInfosFromCursor(Cursor cur) {
        // Get the field values
        long id = cur.getLong(EVENT_PROJECTION_ID);
        String title = cur.getString(EVENT_PROJECTION_TITLE);
        long start = cur.getLong(EVENT_PROJECTION_DTSTART);
        long end = cur.getLong(EVENT_PROJECTION_DTEND);
        int calId = cur.getInt(EVENT_PROJECTION_CALENDAR_ID);
        int color = cur.getInt(EVENT_PROJECTION_CALENDAR_COLOR);
        boolean allDayEvent = cur.getInt(EVENT_PROJECTION_ALLDAYEVENT) == 1;

        EventInfo eventInfo = new EventInfo(id, calId, title, start, end, color, allDayEvent);

        return eventInfo;
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


    public class EventInfo {

        private String mTitle;
        private long mStart, mEnd, mId;
        private int mCalId, mColor;
        private boolean mIsAllDayEvent;

        public EventInfo(long id, int cal_id, String title, long start, long end, int color, boolean isAllDayEvent) {
            this.mId = id;
            mTitle = title;
            mStart = start;
            mEnd = end;
            mCalId = cal_id;
            mColor = color;
            mIsAllDayEvent = isAllDayEvent;

        }


        public String getTitle() {
            return mTitle;
        }

        public long getStart() {
            return mStart;
        }

        public long getEnd() {
            return mEnd;
        }

        public long getId() {
            return mId;
        }

        public int getCalId() {
            return mCalId;
        }

        public int getColor() {
            return mColor;
        }

        public boolean isAllDayEvent() {
            return mIsAllDayEvent;
        }
    }

    public class CalInfo {



        private long mId;
        private String mName, mAccount, mOwner;
        private int mColor;


        public CalInfo(long id, String name, String account, String owner, int color) {
            mId = id;
            mName = name;
            mAccount = account;
            mOwner = owner;
            mColor = color;
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

        public int getColor() {
            return mColor;
        }
    }

}

