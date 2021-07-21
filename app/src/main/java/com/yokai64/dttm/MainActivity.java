package com.yokai64.dttm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    Set up app on boot:
        Set Date as current date on the date line
        Set Time as current time on the time line
        Update the formatted text line
        Update the tag

     When the time is changed:
        Set the date line
        Set the time line
        Update the formatted text line
        Update the tag line
     */

    //List of class global variables
    Button btnDatePicker,
            btnTimePicker,
            btnCopyTag;
    TextView txtDate,
            txtTime,
            txtResultTag;
    long unixTime;

    //When the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up variables
        //Buttons
        btnDatePicker = findViewById(R.id.setDateButton);
        btnTimePicker =findViewById(R.id.setTimeButton);
        btnCopyTag = findViewById(R.id.copyTagButton);
        //TextViews
        txtDate = findViewById(R.id.dateLine);
        txtTime = findViewById(R.id.timeLine);
        txtResultTag = findViewById(R.id.resultantTagText);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        Spinner spinner = findViewById(R.id.displayModeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.displayModesArray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void updateTag(String date, String time) {
        TextView tag=(TextView)findViewById(R.id.resultantTagText);
        TextView formatted=(TextView)findViewById(R.id.formattedOutputText);

        Date myDate = parseDate(date, time);

        unixTime = myDate.getTime()/ 1000L;

        tag.setText("<t:" + unixTime + ">");

        LocalDateTime funny = myDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
        String formattedDate = funny.format(myFormatObj);
        formatted.setText(formattedDate);
    }

    public static Date parseDate(String date, String time) {
        try {
            return new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").parse(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onClick(View v) {

        int mMinute,
                mHour,
                mDay,
                mMonth,
                mYear;

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth)
                        {
                            Calendar mCalendar = new GregorianCalendar();
                            TimeZone mTimeZone = mCalendar.getTimeZone();
                            int mGMTOffset = mTimeZone.getRawOffset();
                            System.out.println("GMT offset is " + TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS) + " hours");

                            System.out.println(Instant.now());

                            DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
                            String sDate1=dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Date date1= null;
                            try {
                                date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            assert date1 != null;
                            txtDate.setText(dateFormatter.format(date1));
                            updateTag((String) txtDate.getText(), (String) txtTime.getText());

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
                            String sDate1=hourOfDay + ":" + minute + ":00";
                            txtTime.setText(Time.valueOf(sDate1).toString());

                            updateTag((String) txtDate.getText(), (String) txtTime.getText());
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == btnCopyTag) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Discord Timezone Tag", txtResultTag.getText());
            clipboard.setPrimaryClip(clip);
        }
    }
}