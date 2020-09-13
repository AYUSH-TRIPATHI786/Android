package com.example.classscheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ListBatchesActivity extends Activity {

	ListView listBatches;//ref to list view
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //Lets the Activity to restore itself to a previous state using the data stored in this bundle.
		setContentView(R.layout.listbatches);
	}
	
	@Override 
	public void onStart() {
		super.onStart();
		listBatches  = this.findViewById(R.id.listBatches);//initializing list view which have to use adapter
		BatchesAdapter adapter  = new BatchesAdapter(this);//initializing an adapter
		listBatches.setAdapter(adapter);
	}
	
	public void addBatch(View v) {
		Intent intent = new Intent(this, AddBatchActivity.class);
		startActivity(intent);
	}
	
	
}
