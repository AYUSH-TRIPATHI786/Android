package com.example.classscheduler;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.List;

public class ListClassesActivity extends Activity {

	    String batchcode;
	    TableLayout tableClasses;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.listclasses);
			
			// get batchcode using intent
			batchcode = getIntent().getStringExtra("batchcode");
			tableClasses = (TableLayout) this.findViewById( R.id.tableClasses);
		}
		
		@Override
		public void onStart() {
			super.onStart();
			//delete all entries
			deleteRowsFromTable();
			// enter all entries from database
			addRowsToTable(tableClasses,batchcode);
		}
		
		public void deleteRowsFromTable() {
			if ( tableClasses.getChildCount() > 2)
			    tableClasses.removeViews(2,tableClasses.getChildCount() - 2);
				//deleting rows except thee first two i.e. the heading row and the marcker below it
	    }
		
		private void addRowsToTable(TableLayout table, String batchcode) {

			 List<Class> classes = Database.getClasses(this, batchcode);
			 
			 TableRow tr = new TableRow(this);
             tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));//setting (width, heigth)
             
             int classno = 1;
             for(final Class c : classes) {
            	    TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.classrow, null);//add this class row layout inside the tablerow layout
            	    
            	    ((TextView)row.findViewById(R.id.textNo)).setText( String.valueOf(classno));//Si. no.

				 	//updating class info from database into tablerow view
            	    ((TextView)row.findViewById(R.id.textDate)).setText(c.getClassDate());
            	    ((TextView)row.findViewById(R.id.textTime)).setText(c.getClassTime());

            	    // handle update button 
            	    ImageButton btnUpdate = (ImageButton) row.findViewById(R.id.btnUpdate);
            	    btnUpdate.setOnClickListener( new OnClickListener() {
						@Override
						public void onClick(View v) {
							  //"this" refers to the most immediate class created therefore the outer class hass to  be specified
							  //opening the UpdateClassActivity
                              Intent intent = new Intent( ListClassesActivity.this, UpdateClassActivity.class);
                              intent.putExtra("classid", c.getClassId());
                              startActivity(intent);
						}
					}); 
            	    
            	    table.addView(row);

            	    //adding the red border line
            	    TableRow line = new TableRow(this);

            	    TextView tv = new TextView(this);
            	    tv.setBackgroundColor(Color.RED);
            	    TableRow.LayoutParams lp = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT,3);
            	    lp.span = 4;//one column will match the above four columns
            	    tv.setLayoutParams(lp);

            	    line.addView(tv);
            	    
            	    table.addView(line);
           	    
            	    classno ++;
             }
			
		}
		
		
}
