package com.example.classscheduler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import java.util.Calendar;

public class UpdateBatchActivity extends FragmentActivity {
	private int day, month, year, hours, mins;
	private TextView textStartDate, textStartTime,textClasses,textClassesPerWeek;
	private static EditText editBatchcode;
	private EditText editCourse;
	private EditText editPeriod;
	private EditText editRemarks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatebatch);

		textStartDate = (TextView) this.findViewById(R.id.textStartDate);
		textStartTime = (TextView) this.findViewById(R.id.textStartTime);
		
		editBatchcode = (EditText) this.findViewById(R.id.editBatchCode) ;
		editCourse = (EditText) this.findViewById(R.id.editCourse) ;
		editPeriod = (EditText) this.findViewById(R.id.editPeriod) ;
		textClasses = (TextView) this.findViewById(R.id.textClasses) ;
		textClassesPerWeek = (TextView) this.findViewById(R.id.textClassesPerWeek) ;
		editRemarks = (EditText) this.findViewById(R.id.editRemarks) ;
		
		// store the batchcode passed from previous activity
		String batchcode = getIntent().getStringExtra("batchcode");

		Batch batch = Database.getBatch(this, batchcode);
		if ( batch == null)
		{
			// error
			Log.d("update","can't get this batch from database");
		}
		else
		{
			editBatchcode.setText( batch.getCode());
			editCourse.setText( batch.getCourse());
			textStartDate.setText( batch.getStartdate());
			textStartTime.setText( batch.getStarttime());
			editPeriod.setText( batch.getPeriod());
			textClasses.setText( batch.getClasses());
			textClassesPerWeek.setText( batch.getClassesperweek());
			editRemarks.setText( batch.getRemarks());

			setDateToStartDate( batch.getStartdate());
			setTimeToStartTime( batch.getStarttime());
		}
    }


	private void setDateToStartDate(String startdate) {
		String [] parts = startdate.split("-");
		day = Integer.parseInt( parts[2]);
		month =Integer.parseInt( parts[1]);
		year = Integer.parseInt( parts[0]);
	}
	
	private void setTimeToStartTime(String starttime) {
		String [] parts = starttime.split(":");
		hours = Integer.parseInt( parts[0]);
		mins =Integer.parseInt( parts[1]);
	}

	public void showDatePicker(View v) {
		DialogFragment newFragment = new UpdateBatchActivity.DatePickerFragment();
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
		DialogFragment newFragment = new UpdateBatchActivity.TimePickerFragment();
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
		textStartDate.setText(String.format("%04d-%02d-%02d", year, month + 1,day));
	}
	
	private void updateTimeDisplay() {
		// Month is 0 based so add 1
		textStartTime.setText(String.format("%02d:%02d", hours,mins));
	}


	public void updateBatch(View v) {

		boolean done = Database.updateBatch(this,
				editBatchcode.getText().toString(),
				editCourse.getText().toString(),
				textStartTime.getText().toString(),
				editPeriod.getText().toString(),
				editRemarks.getText().toString());

		if ( done )
			Toast.makeText(this,"Updated batch successfully!", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this,"Sorry! Could not update batch!", Toast.LENGTH_LONG).show();

	}

	public void deleteBatch(View v) {
		DialogFragment newFragment = new UpdateBatchActivity.AlertDialogFragment();
		newFragment.show(getSupportFragmentManager(), "Alert Dialog");
	}

	protected class AlertDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.delete_batch)
					.setPositiveButton(R.string.proceed_delete, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// delete batch from database
							boolean done = Database.deleteBatch(UpdateBatchActivity.this, editBatchcode.getText().toString());
							if ( done ) {
									 Toast.makeText(UpdateBatchActivity.this,"Deleted batch successfully!", Toast.LENGTH_LONG).show();
									 UpdateBatchActivity.this.finish();
									 //ending the activity
									 //The ActivityResult is propagated back to whoever launched you via onActivityResult()
							}
							else
								Toast.makeText(UpdateBatchActivity.this,"Sorry! Could not delete batch!", Toast.LENGTH_LONG).show();
						}
					})
					.setNegativeButton(R.string.cancel_delete, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
						}
					});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}



}
