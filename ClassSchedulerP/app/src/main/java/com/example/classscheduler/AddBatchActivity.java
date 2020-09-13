package com.example.classscheduler;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class AddBatchActivity extends FragmentActivity {

	private static int day;
	private static int month;
	private static int year;
	private static int hours;
	private static int mins;
	private static TextView textStartDate;
	private static TextView textStartTime;
	private EditText editBatchcode,editCourse,editPeriod,editClasses,editClassesPerWeek, editRemarks; //lets you type text in this area

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addbatch);

		textStartDate = (TextView) this.findViewById(R.id.textStartDate);
		textStartTime = (TextView) this.findViewById(R.id.textStartTime);
		
		editBatchcode = (EditText) this.findViewById(R.id.editBatchCode) ;
		editCourse = (EditText) this.findViewById(R.id.editCourse) ;
		editPeriod = (EditText) this.findViewById(R.id.editPeriod) ;
		editClasses = (EditText) this.findViewById(R.id.editClasses) ;
		editClassesPerWeek = (EditText) this.findViewById(R.id.editClassesPerWeek) ;
		editRemarks = (EditText) this.findViewById(R.id.editRemarks) ;
		
		setDateToSysdate();
		updateDateDisplay();

	}

	private void setDateToSysdate() {
		Calendar c = Calendar.getInstance();
		day = c.get(Calendar.DAY_OF_MONTH);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);
	}

	public void showDatePicker(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	public static class DatePickerFragment extends DialogFragment
			implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int y, int m, int d) {
			// Do something with the date chosen by the user
			year = y;
			month = m;
			day = d;
			updateDateDisplay();
		}
	}


	public void showTimePicker(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	public static class TimePickerFragment extends DialogFragment
			implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
			hours = hourOfDay;
			mins = minute;
			updateTimeDisplay();

		}
	}

	private static void updateDateDisplay() {
		// Month is 0 based so add 1
		textStartDate.setText(String.format("%04d-%02d-%02d", year, month + 1,day));
	}
	
	private static void updateTimeDisplay() {
		textStartTime.setText(String.format("%02d:%02d", hours,mins));
	}


	public void addBatch(View v) {
		boolean done = Database.addBatch(this,
				editBatchcode.getText().toString(),
				editCourse.getText().toString(),
				textStartDate.getText().toString(),
				textStartTime.getText().toString(),
				editClasses.getText().toString(),
				editPeriod.getText().toString(),
				editClassesPerWeek.getText().toString(),
				editRemarks.getText().toString());

		if ( done )
			Toast.makeText(this,"Added batch successfully!", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this,"Sorry! Could not add batch!", Toast.LENGTH_LONG).show();
	}

}
