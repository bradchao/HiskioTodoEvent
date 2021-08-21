package com.bradchao.hiskiotodoevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
            == PackageManager.PERMISSION_GRANTED){
            init();
        }else{
            requestPermissions(new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
            }, 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            init();
        }else{
            finish();
        }
    }

    private void init(){

    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public void queryCalendar(View view) {
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        // SELECT * FROM uri
//        cur = cr.query(
//                uri, null, null, null, "account_name, lastDate");

        // SELECT * FROM uri WHERE lastDate >= ? AND lastDate <= ? AND account_name = ? ORDER BY lastDate
        String sel = "lastDate >= ? AND lastDate <= ? AND account_name = ?";
        Calendar startDate = Calendar.getInstance();
        startDate.set(2021, Calendar.AUGUST, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2021, Calendar.AUGUST, 31);

        String[] args = { ""+startDate.getTimeInMillis(), ""+endDate.getTimeInMillis(), "brad@brad.tw"};
        cur = cr.query(
                uri, null, sel, args, "account_name, lastDate");

        //Log.v("bradlog", "count = " + cur.getCount());

//        String[] fields = cur.getColumnNames();
//        for (String field : fields){
//            Log.v("bradlog", field);
//        }

        int lastDateIndex = cur.getColumnIndex("lastDate");
        int titleIndex = cur.getColumnIndex("title");
        int accountIndex = cur.getColumnIndex("account_name");
        while (cur.moveToNext()){
            long lastDate = cur.getLong(lastDateIndex);
            String title = cur.getString(titleIndex);
            String account = cur.getString(accountIndex);
            Log.v("bradlog", account + ":" + sdf.format(lastDate) + ":" + title);
        }


    }
}