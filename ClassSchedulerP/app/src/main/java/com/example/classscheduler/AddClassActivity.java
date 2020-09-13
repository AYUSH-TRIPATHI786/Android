package com.example.classscheduler;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import java.util.Calendar;

public class AddClassActivity extends FragmentActivity {
	private int day, month, year, hours, mins;
	private TextView textClassDate, textClassTime, textBatchCode;
	private EditText editPeriod,editRemarks, editTopics;
	private CheckBox chkAdjust;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addclass);

		textBatchCode = (TextView) this.findViewById(R.id.textBatchCode);
		textClassDate = (TextView) this.findViewById(R.id.textClassDate);
		textClassTime = (TextView) this.findViewById(R.id.textClassTime);

		editPeriod = (EditText) this.findViewById(R.id.editPeriod) ;
		editRemarks = (EditText) this.findViewById(R.id.editRemarks) ;
		editTopics = (EditText) this.findViewById(R.id.editTopics) ;
		
		chkAdjust = (CheckBox) this.findViewById(R.id.chkAdjust);
		
		textBatchCode.setText(   getIntent().getStringExtra("batchcode"));
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
		DialogFragment newFragment = new AddClassActivity.DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	public class DatePickerFragment extends DialogFragment
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
		DialogFragment newFragment = new AddClassActivity.TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	public class TimePickerFragment extends DialogFragment
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
	

	private void updateDateDisplay() {
		// Month is 0 based so add 1
		textClassDate.setText(String.format("%04d-%02d-%02d", year, month + 1,day));
	}
	
	private void updateTimeDisplay() {
		// Month is 0 based so add 1
		textClassTime.setText(String.format("%02d:%02d", hours,mins));
	}

	//add this new class to Database
	public void addClass(View v) {
		boolean done = Database.addClass(this,
				textBatchCode.getText().toString(),
				textClassDate.getText().toString(),
				textClassTime.getText().toString(),
				editPeriod.getText().toString(),
				editTopics.getText().toString(),
				editRemarks.getText().toString(),
				chkAdjust.isChecked());

		if ( done )
			Toast.makeText(this,"Added com.example.classscheduler.Class Successfully!", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this,"Sorry! Could not add class!", Toast.LENGTH_LONG).show();
	}

}
