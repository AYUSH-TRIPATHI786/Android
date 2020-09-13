package com.example.classscheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class BatchesAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Batch> batches;

	//Obtains the layout Inflater from the context ctx i.e. the ListView
	public BatchesAdapter(Context ctx) {
		inflater = LayoutInflater.from(ctx);
		batches = Database.getBatches(ctx);
	}

	//How many items are in the data set represented by this Adapter.
	@Override
	public int getCount() {
		return batches.size();
	}

	//Get the data item associated with the specified position in the data set.
	@Override
	public Batch getItem(int pos) {
		return batches.get(pos);
	}

	//Get the row id associated with the specified position in the list.
	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.batch, null,false);//add the batch layout to the batch ListView
			Button btnClasses =  convertView.findViewById(R.id.btnClasses);
			Button btnUpdate =  convertView.findViewById(R.id.btnUpdate);
			Button btnAddClass =  convertView.findViewById(R.id.btnAddClass);

			//Take a batch at certain position in the database and put it at same position in ListView
            final Batch batch = batches.get(position);

            TextView textCode = (TextView) convertView.findViewById(R.id.textCode);
			textCode.setText( batch.getCode());
			
			TextView textCourse = (TextView) convertView.findViewById(R.id.textCourse);
			textCourse.setText( batch.getCourse());
			
			TextView textStartDate = (TextView) convertView.findViewById(R.id.textStartDate);
			textStartDate.setText(batch.getStartdate());
			
			TextView textEndDate = (TextView) convertView.findViewById(R.id.textEndDate);
			textEndDate.setText(batch.getEnddate());
			
			//add action on clicking btnClasses
			btnClasses.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
				    Context context = view.getContext();
					Intent intent = new Intent(context, ListClassesActivity.class);
					// passing the BatchCode to provide which batch's list of class shoulld be displayed
					intent.putExtra("batchcode",batch.getCode());
					context.startActivity(intent);
				}
			});

			//add action on clicking btnAddClass
			btnAddClass.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
				    Context context = view.getContext();
					Intent intent = new Intent(context, AddClassActivity.class);
					// passing the BatchCode to provide which batch the new class should be added
					intent.putExtra("batchcode",batch.getCode());
					context.startActivity(intent);
				}
			});


			//add action on clicking btnUpdate
			btnUpdate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					
					Context context = view.getContext();
					Intent intent = new Intent(context, UpdateBatchActivity.class);
					// passing the BatchCode to provide which batch to update
					intent.putExtra("batchcode",batch.getCode());
					context.startActivity(intent);
				}
			});
		}
		return convertView;
	}
}
