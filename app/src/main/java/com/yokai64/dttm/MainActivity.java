package com.yokai64.dttm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    Button btnDatePicker, btnTimePicker, copyTag;
    TextView txtDate, resultTag;
    TextView txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    long unixTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        copyTag=(Button)findViewById(R.id.copyTag);
        txtDate=(TextView)findViewById(R.id.in_date);
        txtTime=(TextView)findViewById(R.id.in_time);
        resultTag=(TextView)findViewById(R.id.result_tag);

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
        String sDate1=mDay + "/" + (mMonth + 1) + "/" + mYear;
        Date date1= null;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert date1 != null;
        txtDate.setText(dateFormatter.format(date1));

        // Get Current Time
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        txtTime.setText(mHour + ":" + mMinute);

        //updateTag((String) txtDate.getText(), (String) txtTime.getText());

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        copyTag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Discord Timezone Tag", resultTag.getText());
                clipboard.setPrimaryClip(clip);
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void updateTag(String date, String time) {
        TextView tag=(TextView)findViewById(R.id.result_tag);
        TextView formatted=(TextView)findViewById(R.id.formatted_output);

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
                                              int monthOfYear, int dayOfMonth) {

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
    }
}