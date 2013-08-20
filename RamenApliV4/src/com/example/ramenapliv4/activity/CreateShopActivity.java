package com.example.ramenapliv4.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.ramenapliv4.R;
import com.example.ramenapliv4.dto.RamenShopDTO;
import com.example.ramenapliv4.entity.RamenDAO;
import com.example.ramenapliv4.entity.RamenOpenHelper;
import com.example.ramenapliv4.util.RamenUtil;

public class CreateShopActivity extends Activity implements LocationListener {

	private static final int REQUEST_GALLERY_CODE = 0;
	private static final int REQUEST_CAMERA_CODE = 1;
	private LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.activity_createshop);

		Button btn_takepict = (Button) findViewById(R.id.btn_takepict);
		btn_takepict.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				if (locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					Toast.makeText(getApplicationContext(),
							"GPSを利用して現在位置を取得中...", Toast.LENGTH_LONG).show();
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, 10000, 0,
							CreateShopActivity.this);
				} else if (locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
					Toast.makeText(getApplicationContext(),
							"GPSが利用できない為Networkを利用して現在位置を取得中...",
							Toast.LENGTH_LONG).show();
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER, 10000, 0,
							CreateShopActivity.this);
				} else {
					Toast.makeText(getApplicationContext(),
							"GPS及びNetworkが利用出来ない為現在位置の反映なし", Toast.LENGTH_LONG)
							.show();
					Intent intent = new Intent();
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, REQUEST_GALLERY_CODE);
				}
			}
		});

		Button btn_selectpict = (Button) findViewById(R.id.btn_selectpict);
		btn_selectpict.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, REQUEST_GALLERY_CODE);
			}
		});

		Button btn_rotate = (Button) findViewById(R.id.btn_rotate);
		btn_rotate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageView imageView = (ImageView) findViewById(R.id.img_ramenpict);
				Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable())
						.getBitmap();
				Matrix mat = new Matrix();
				mat.postRotate(-90);
				imageView.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), mat, true));
			}
		});

		Button btn_inputmap = (Button) findViewById(R.id.btn_inputmap);
		btn_inputmap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "未実装",
						Toast.LENGTH_SHORT).show();
			}
		});

		Button btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String shopname = ((EditText) findViewById(R.id.txt_shopname))
						.getText().toString();
				Bitmap shopbitmap = ((BitmapDrawable) ((ImageView) findViewById(R.id.img_ramenpict))
						.getDrawable()).getBitmap();
				String shoplat = ((EditText) findViewById(R.id.txt_lat))
						.getText().toString();
				String shoplon = ((EditText) findViewById(R.id.txt_lon))
						.getText().toString();
				int shoprating = (int) ((RatingBar) findViewById(R.id.rat_rating))
						.getRating();
				String shopcomment = ((EditText) findViewById(R.id.txt_comment))
						.getText().toString();

				boolean hasErrorInput = false;
				if ("".equals(shopname)) {
					((EditText) findViewById(R.id.txt_shopname))
							.setError("店名は必須入力です。");
					hasErrorInput = true;
				}
				if ("".equals(shoplat)) {
					((EditText) findViewById(R.id.txt_lat))
							.setError("経度は必須入力です。");
					hasErrorInput = true;
				}
				if ("".equals(shoplon)) {
					((EditText) findViewById(R.id.txt_lon))
							.setError("緯度は必須入力です。");
					hasErrorInput = true;
				}

				if (hasErrorInput) {
					Toast.makeText(getApplicationContext(), "入力情報に不備があります。",
							Toast.LENGTH_LONG).show();
				} else {
					RamenShopDTO dto = new RamenShopDTO();
					dto.setName(shopname);
					dto.setLatitude(Double.valueOf(shoplat));
					dto.setLongitude(Double.valueOf(shoplon));
					dto.setRating(shoprating);
					dto.setComment(shopcomment);
					dto.setPict(RamenUtil.convertBitmapToByte(shopbitmap));

					RamenOpenHelper helper = new RamenOpenHelper(
							getApplicationContext());
					SQLiteDatabase db = helper.getWritableDatabase();
					RamenDAO dao = new RamenDAO(db);
					dao.insertRamenShop(dto);
					Toast.makeText(getApplicationContext(), "登録が完了しました。",
							Toast.LENGTH_LONG).show();

					Intent intent = new Intent(CreateShopActivity.this,
							RamenMapActivity.class);
					intent.putExtra("lat", dto.getLatitude());
					intent.putExtra("lon", dto.getLongitude());
					finish();
					startActivity(intent);
				}
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_GALLERY_CODE && resultCode == -1) {
			setInfoFromPict(data.getData());
		} else if (requestCode == REQUEST_CAMERA_CODE && resultCode == -1) {
			setInfoFromPict(data.getData());
		}
	}

	private void setInfoFromPict(Uri imageUri) {

		Bitmap resizedBitmap = RamenUtil.getResizedBitmap(imageUri, this);
		((ImageView) findViewById(R.id.img_ramenpict))
				.setImageBitmap(resizedBitmap);

		ExifInterface exifInterface = null;
		try {
			ContentResolver resolver = getContentResolver();
			Cursor cursor = resolver.query(imageUri, null, null, null, null);
			cursor.moveToFirst();
			String filepath = cursor.getString(cursor
					.getColumnIndex(MediaStore.MediaColumns.DATA));

			exifInterface = new ExifInterface(filepath);

		} catch (Exception e) {
			e.printStackTrace();
		}

		float[] latLong = new float[2];
		exifInterface.getLatLong(latLong);

		if (latLong[0] != 0.0f || latLong[1] != 0.0f) {
			((EditText) findViewById(R.id.txt_lat)).setText(String
					.valueOf(latLong[0]));
			((EditText) findViewById(R.id.txt_lon)).setText(String
					.valueOf(latLong[1]));
			Toast.makeText(this, "写真の位置情報を自動入力しました", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "写真の位置情報が含まれていません。経度緯度を入力してください。",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		Toast.makeText(this, "位置情報取得に成功", Toast.LENGTH_SHORT).show();
		locationManager.removeUpdates(this);
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQUEST_GALLERY_CODE);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
