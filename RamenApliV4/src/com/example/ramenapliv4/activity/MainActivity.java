package com.example.ramenapliv4.activity;

import com.example.ramenapliv4.R;
import com.example.ramenapliv4.R.menu;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		String[] items = { "Map表示", "店登録" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch (position) {
		case 0:
			Intent intent0 = new Intent(MainActivity.this,
					RamenMapActivity.class);
			startActivity(intent0);
			break;
		case 1:
			Intent intent1 = new Intent(MainActivity.this,
					CreateShopActivity.class);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

}
