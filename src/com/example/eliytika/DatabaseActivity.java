package com.example.eliytika;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DatabaseActivity extends Activity {

	static SQLiteDatabase mydb;
	private Integer[] data;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);

		/***

		Button button = (Button)this.findViewById(R.id.button01);
		view();
		button.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				add();
				view();
			}
		});

		***/
	}

	private void add() throws SQLException{
		//EditText editText = (EditText)findViewById(R.id.editText01);
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
	//	String created = Calendar.getInstance().getTime().toString();
	//	String comment = editText.getText().toString();
		String score = null;
		int score_int = Integer.parseInt(score);
		ContentValues values = new ContentValues();
		values.put("SCORE", score_int);

		long rowID = db.insert("bbs", "", values);
	//	editText.setText("");
		if(rowID == -1){
			db.close();
			throw new SQLException("Failed to insert row");
		}db.close();
	}

	private void view(){
		DatabaseHelper hlpr = new
		DatabaseHelper(getApplicationContext());
		mydb = hlpr.getWritableDatabase();
		ListView listView = (ListView)findViewById(R.id.listView01);
		try{
			Cursor cr = mydb.rawQuery("Select * From bbs Order By id desc", null);
			cr.moveToFirst();
			if(cr.getCount() > 0){
				data = new Integer[cr.getCount()];
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
				for(int cnt = 0; cnt < cr.getCount(); cnt++){
					data = new Integer[cr.getCount()];
					data[cnt] = cr.getInt(0);
					adapter.add("ID:" + cr.getString(0) + ", \ncreated:" + cr.getString(1) + ",\ncomment:" + cr.getString(2));
					cr.moveToNext();
					listView.setAdapter(adapter);
				}
			}else listView.setAdapter(null);
		}finally{mydb.close();}

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
				delete(data[(int)id]);
				return false;
			}
		});

	}

	private void delete(int id){
		DatabaseHelper dbHelper = new DatabaseHelper(this);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			final Toast toast_s = Toast.makeText(this, "削除成功ＩＤ＝" + id, Toast.LENGTH_LONG);
			final Toast toast_f = Toast.makeText(this, "削除失敗ＩＤ＝" + id, Toast.LENGTH_LONG);
			int ret;
			try{
				ret = db.delete("bbs", "id = "+ id, null);
				view();
			}finally{
				db.close();
			}
			if(ret == 1){
				toast_s.show();
			}else{
				toast_f.show();
			}

	}






	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.database, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}




