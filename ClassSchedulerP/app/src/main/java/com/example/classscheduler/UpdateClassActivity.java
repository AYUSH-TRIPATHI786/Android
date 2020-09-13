package com.example.classscheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Calendar;

public class UpdateClassActivity extends FragmentActivity {
	
	private int hours, mins;
	private TextView textClassDate, textClassTime, textBatchCode;
	private EditText editPeriod,editRemarks, editTopics;
	
	private String classid;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
         setContentView(R.layout.updateclass);


		textClassDate = (TextView) this.findViewById(R.id.textClassDate);
		textClassTime = (TextView) this.findViewById(R.id.textClassTime);
		
		textBatchCode = (TextView) this.findViewById(R.id.textBatchCode);
		editPeriod = (EditText) this.findViewById(R.id.editPeriod) ;
		editTopics = (EditText) this.findViewById(R.id.editTopics) ;
		editRemarks = (EditText) this.findViewById(R.id.editRemarks) ;

		// store the classid passed from previous activity
		classid = getIntent().getStringExtra("classid");
	    Class clas = Database.getClass(this, classid);
		if ( clas == null)
		{
			// error
			Log.d("update","can't get this class from database");
		}
		else
		{
			textBatchCode.setText( clas.getBatchCode());
			textClassDate.setText( clas.getClassDate());
			textClassTime.setText( clas.getClassTime());
			editPeriod.setText( clas.getPeriod());
			editTopics.setText( clas.getTopics());
			editRemarks.setText( clas.getRemarks());
		}
    }

	public void showTimePicker(View v) {
		DialogFragment newFragment = new UpdateClassActivity.TimePickerFragment();
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

	private void updateTimeDisplay() {
		// Month is 0 based so add 1
		textClassTime.setText(String.format("%02d:%02d", hours,mins));
	}


	public void updateClass(View v) {
		boolean done = Database.updateClass(this,
				classid,
				textClassTime.getText().toString(),
				editPeriod.getText().toString(),
				editTopics.getText().toString(),
				editRemarks.getText().toString());

		if ( done )
			Toast.makeText(this,"Updated class successfully!", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this,"Sorry! Could not update class!", Toast.LENGTH_LONG).show();
	}

	public void deleteClass(View v) {
		DialogFragment newFragment = new UpdateClassActivity.deleteAlertDialogFragment();
		newFragment.show(getSupportFragmentManager(), "Alert Dialog");
	}

	public class deleteAlertDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.delete_class)
					.setPositiveButton(R.string.proceed_delete, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// delete class from database
							boolean done = Database.deleteClass(UpdateClassActivity.this,classid);
							if ( done ) {
								Toast.makeText(UpdateClassActivity.this,"Deleted class successfully!", Toast.LENGTH_LONG).show();
								UpdateClassActivity.this.finish();
								//ending the activity
								//The ActivityResult is propagated back to whoever launched you via onActivityResult()
							}
							else
								Toast.makeText(UpdateClassActivity.this,"Sorry! Could not delete class!", Toast.LENGTH_LONG).show();
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

	public void cancelClass(View v) {
		DialogFragment newFragment = new UpdateClassActivity.cancelAlertDialogFragment();
		newFragment.show(getSupportFragmentManager(), "Alert Dialog");
	}

	public class cancelAlertDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Do you want to delete current class and add another class?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// delete class from database
							boolean done = Database.cancelClass(UpdateClassActivity.this,textBatchCode.getText().toString(),classid);
							if ( done ) {
								Toast.makeText(UpdateClassActivity.this,"Cancelled current class and added new class successfully!", Toast.LENGTH_LONG).show();
								UpdateClassActivity.this.finish();
								//ending the activity
								//The ActivityResult is propagated back to whoever launched you via onActivityResult()
							}
							else
								Toast.makeText(UpdateClassActivity.this,"Sorry! Could not cancel class!", Toast.LENGTH_LONG).show();
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
						}
					});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}

	
}
