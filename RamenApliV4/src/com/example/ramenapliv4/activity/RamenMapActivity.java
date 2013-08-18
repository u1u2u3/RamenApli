package com.example.ramenapliv4.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ramenapliv4.R;
import com.example.ramenapliv4.R.id;
import com.example.ramenapliv4.R.layout;
import com.example.ramenapliv4.dto.RamenShopDTO;
import com.example.ramenapliv4.entity.RamenDAO;
import com.example.ramenapliv4.entity.RamenOpenHelper;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RamenMapActivity extends FragmentActivity implements
		OnMarkerClickListener {

	private GoogleMap map;
	private LatLng initlatlng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ramenmap);

		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.my_map)).getMap();

		try {
			MapsInitializer.initialize(getApplicationContext());
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}

		// map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
		map.setOnMarkerClickListener((OnMarkerClickListener) this);

		RamenOpenHelper helper = new RamenOpenHelper(getApplicationContext());
		SQLiteDatabase db = helper.getReadableDatabase();
		RamenDAO dao = new RamenDAO(db);
		List<RamenShopDTO> list = dao.findAllRamenShop();
		for (RamenShopDTO ramenShopDTO : list) {
			MarkerOptions options = new MarkerOptions();
			options.position(new LatLng(ramenShopDTO.getLatitude(),
					ramenShopDTO.getLongitude()));
			options.title(String.valueOf(ramenShopDTO.getId()));
			switch (ramenShopDTO.getRating()) {
			case 5:
				options.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				break;
			case 4:
				options.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
				break;
			case 3:
				options.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
				break;
			case 2:
				options.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
				break;
			case 1:
				options.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
				break;
			default:
				break;
			}
			map.addMarker(options);
		}

		Intent intent = getIntent();
		if (intent.getDoubleExtra("lat", 0) == 0
				&& intent.getDoubleExtra("lon", 0) == 0) {
			initlatlng = new LatLng(35.698501, 139.414136);
		} else {
			initlatlng = new LatLng(intent.getDoubleExtra("lat", 0),
					intent.getDoubleExtra("lon", 0));
		}
		CameraPosition position = new CameraPosition.Builder()
				.target(initlatlng).zoom(15.5f).bearing(0).tilt(25).build();
		map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

	}

	// private class CustomInfoWindowAdapter implements InfoWindowAdapter {
	//
	// private final View window;
	//
	// public CustomInfoWindowAdapter() {
	// window = getLayoutInflater().inflate(R.layout.window_custominfo, null);
	// }
	//
	// @Override
	// public View getInfoContents(Marker arg0) {
	// return null;
	// }
	//
	// @Override
	// public View getInfoWindow(Marker marker) {
	// String id = marker.getTitle();
	// RamenOpenHelper helper = new RamenOpenHelper(getApplicationContext());
	// SQLiteDatabase db = helper.getReadableDatabase();
	// final RamenDAO dao = new RamenDAO(db);
	// final RamenShopDTO dto = dao.findRamenShopById(id);
	// ((TextView)
	// window.findViewById(R.id.window_name)).setText(dto.getName());
	// if (dto.getPict() != null) {
	// Bitmap bitmap = BitmapFactory.decodeByteArray(dto.getPict(), 0,
	// dto.getPict().length);
	// ((ImageView)
	// window.findViewById(R.id.window_pict)).setImageBitmap(bitmap);
	// } else {
	// ((ImageView) window.findViewById(R.id.window_pict)).setImageBitmap(null);
	// }
	// ((Button)
	// window.findViewById(R.id.window_btn_delete)).setOnClickListener(new
	// OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dao.deleteRamenShop(String.valueOf(dto.getId()));
	// }
	// });
	// return window;
	// }
	//
	// }

	@Override
	public boolean onMarkerClick(Marker marker) {

		final String id = marker.getTitle();
		RamenOpenHelper helper = new RamenOpenHelper(getApplicationContext());
		SQLiteDatabase db = helper.getReadableDatabase();
		final RamenDAO dao = new RamenDAO(db);
		final RamenShopDTO dto = dao.findRamenShopById(id);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.window_custominfo,
				(ViewGroup) findViewById(R.id.dialog_root));

		builder.setTitle(dto.getName());
		builder.setView(layout);
		if (dto.getPict() != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(dto.getPict(), 0,
					dto.getPict().length);
			((ImageView) layout.findViewById(R.id.window_pict))
					.setImageBitmap(bitmap);
		} else {
			((ImageView) layout.findViewById(R.id.window_pict))
					.setImageBitmap(null);
		}

		builder.setPositiveButton("店削除", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.setNeutralButton("店情報",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(RamenMapActivity.this, UpdateShopActivity.class);
						intent.putExtra("shopid", id);
						startActivity(intent);
					}
				});
		builder.setNegativeButton("メニュー",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(RamenMapActivity.this,
								RamenListActivity.class);
						intent.putExtra("shopid", id);
						startActivity(intent);
					}
				});

		builder.create().show();
		return true;
	}

}
