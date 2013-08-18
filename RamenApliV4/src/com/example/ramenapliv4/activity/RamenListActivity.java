package com.example.ramenapliv4.activity;

import java.util.List;

import com.example.ramenapliv4.R;
import com.example.ramenapliv4.R.layout;
import com.example.ramenapliv4.dto.RamenFoodDTO;
import com.example.ramenapliv4.entity.RamenDAO;
import com.example.ramenapliv4.entity.RamenOpenHelper;
import com.example.ramenapliv4.util.RamenListArrayAdapter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class RamenListActivity extends ListActivity {
	
	// 0=表示, 1=編集, 2=削除
	private int mode = 0;

	private String shopid;
	private List<RamenFoodDTO> items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		shopid = intent.getStringExtra("shopid");

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.Tab tab1 = actionBar.newTab();
		tab1.setText("表示").setTabListener(new TabListener() {

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				
			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				mode = 0;
				getListView().setBackgroundColor(Color.parseColor("#FFFFFF"));
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				
			}
		});
		actionBar.addTab(tab1);
		ActionBar.Tab tab2 = actionBar.newTab();
		tab2.setText("追加").setTabListener(new TabListener() {

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				
			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				Intent intent = new Intent(RamenListActivity.this,
						CreateRamenActivity.class);
				intent.putExtra("shopid", shopid);
				startActivity(intent);
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				
			}
		});
		actionBar.addTab(tab2);
		ActionBar.Tab tab3 = actionBar.newTab();
		tab3.setText("編集").setTabListener(new TabListener() {

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				
			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				mode = 1;
				getListView().setBackgroundColor(Color.parseColor("#B0E0E6"));
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				
			}
		});
		actionBar.addTab(tab3);
		ActionBar.Tab tab4 = actionBar.newTab();
		tab4.setText("削除").setTabListener(new TabListener() {

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				
			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				mode = 2;
				getListView().setBackgroundColor(Color.parseColor("#D3D3D3"));
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				
			}
		});
		actionBar.addTab(tab4);

		RamenOpenHelper helper = new RamenOpenHelper(getApplicationContext());
		SQLiteDatabase db = helper.getReadableDatabase();
		final RamenDAO dao = new RamenDAO(db);

		items = dao.findRamenFoodByShopId(shopid);

		ListAdapter adapter = new RamenListArrayAdapter(this,
				R.layout.listitem_ramenfood, items);
		setListAdapter(adapter);

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (mode == 1) {
			RamenFoodDTO dto = items.get(position);
			Intent intent = new Intent(RamenListActivity.this, UpdateRamenActivity.class);
			intent.putExtra("foodid", dto.getId().toString());
			startActivity(intent);
			
		} else if (mode == 2) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("削除しますか？");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					RamenFoodDTO dto = items.get(position);
					RamenOpenHelper helper = new RamenOpenHelper(getApplicationContext());
					SQLiteDatabase db = helper.getReadableDatabase();
					final RamenDAO dao = new RamenDAO(db);
					dao.deleteRamenFood(dto.getId().toString());
					Intent intent = new Intent(RamenListActivity.this, RamenListActivity.class);
					intent.putExtra("shopid", shopid);
					finish();
					startActivity(intent);
					Toast.makeText(getApplicationContext(), "削除しました。", Toast.LENGTH_SHORT).show();
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder.create().show();
		}
	}

}
