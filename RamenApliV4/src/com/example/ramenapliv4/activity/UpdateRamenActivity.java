package com.example.ramenapliv4.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.ramenapliv4.R;
import com.example.ramenapliv4.dto.RamenFoodDTO;
import com.example.ramenapliv4.entity.RamenDAO;
import com.example.ramenapliv4.entity.RamenOpenHelper;

public class UpdateRamenActivity extends Activity {

	private static final int REQUEST_GALLERY_CODE = 0;
	private String foodid;
	private RamenFoodDTO dto_before;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.activity_updateramen);

		Intent intent = getIntent();
		foodid = intent.getStringExtra("foodid");

		RamenOpenHelper helper = new RamenOpenHelper(getApplicationContext());
		SQLiteDatabase db = helper.getReadableDatabase();
		final RamenDAO dao = new RamenDAO(db);
		dto_before = dao.findRamenFoodByFoodId(foodid);

		((EditText) findViewById(R.id.txt_update_foodname)).setText(dto_before
				.getName());
		Bitmap bitmap = BitmapFactory.decodeByteArray(dto_before.getPict(), 0,
				dto_before.getPict().length);
		((ImageView) findViewById(R.id.img_update_foodpict))
				.setImageBitmap(bitmap);
		((EditText) findViewById(R.id.date_update_fooddate)).setText(dto_before
				.getDate());
		((RatingBar) findViewById(R.id.rat_update_foodrating))
				.setRating(dto_before.getRating());
		((EditText) findViewById(R.id.txt_update_foodcomment))
				.setText(dto_before.getComment());

		Button btn_selectpict = (Button) findViewById(R.id.btn_update_selectfoodpict);
		btn_selectpict.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, REQUEST_GALLERY_CODE);
			}
		});

		Button btn_rotate = (Button) findViewById(R.id.btn_update_foodrotate);
		btn_rotate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageView imageView = (ImageView) findViewById(R.id.img_update_foodpict);
				Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable())
						.getBitmap();
				Matrix mat = new Matrix();
				mat.postRotate(-90);
				imageView.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), mat, true));
			}
		});

		Button btn_submit = (Button) findViewById(R.id.btn_update_submit);
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String foodname = ((EditText) findViewById(R.id.txt_update_foodname))
						.getText().toString();
				Bitmap foodbitmap = ((BitmapDrawable) ((ImageView) findViewById(R.id.img_update_foodpict))
						.getDrawable()).getBitmap();
				String fooddate = ((EditText) findViewById(R.id.date_update_fooddate))
						.getText().toString();
				int foodrating = (int) ((RatingBar) findViewById(R.id.rat_update_foodrating))
						.getRating();
				String foodcomment = ((EditText) findViewById(R.id.txt_update_foodcomment))
						.getText().toString();

				boolean hasErrorInput = false;
				if ("".equals(foodname)) {
					((EditText) findViewById(R.id.txt_update_foodname))
							.setError("メニュー名は必須入力です。");
					hasErrorInput = true;
				}
				if ("".equals(fooddate)) {
					((EditText) findViewById(R.id.date_update_fooddate))
							.setError("日付は必須入力です。");
					hasErrorInput = true;
				}

				if (hasErrorInput) {
					Toast.makeText(getApplicationContext(), "入力情報に不備があります。",
							Toast.LENGTH_LONG).show();
				} else {
					RamenFoodDTO dto = new RamenFoodDTO();
					dto.setShopId(Integer.valueOf(dto_before.getShopId()));
					dto.setName(foodname);
					dto.setDate(fooddate);
					dto.setRating(foodrating);
					dto.setComment(foodcomment);
					dto.setId(dto_before.getId());

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					foodbitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
					dto.setPict(stream.toByteArray());

					RamenOpenHelper helper = new RamenOpenHelper(
							getApplicationContext());
					SQLiteDatabase db = helper.getWritableDatabase();
					RamenDAO dao = new RamenDAO(db);
					dao.updateRamenFood(dto);
					Toast.makeText(getApplicationContext(), "更新が完了しました。",
							Toast.LENGTH_LONG).show();

					Intent intent = new Intent(UpdateRamenActivity.this,
							RamenListActivity.class);
					intent.putExtra("shopid", dto_before.getShopId().toString());
					finish();
					startActivity(intent);
				}
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_GALLERY_CODE) {
			try {
				ExifInterface exifInterface = null;
				ContentResolver resolver = getContentResolver();
				InputStream in = resolver.openInputStream(data.getData());
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				in.close();

				// 画面サイズを取得する
				Matrix matrix = new Matrix();
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				int srcWidth = bitmap.getWidth(); // 元画像のwidth
				int srcHeight = bitmap.getHeight(); // 元画像のheight
				float screenWidth = (float) metrics.widthPixels;
				float screenHeight = (float) metrics.heightPixels;
				float widthScale = screenWidth / srcWidth;
				float heightScale = screenHeight / srcHeight;
				if (widthScale > heightScale) {
					matrix.postScale(heightScale, heightScale);
				} else {
					matrix.postScale(widthScale, widthScale);
				}
				Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
						srcWidth, srcHeight, matrix, true);

				((ImageView) findViewById(R.id.img_update_foodpict))
						.setImageBitmap(resizedBitmap);

				Cursor cursor = resolver.query(data.getData(), null, null,
						null, null);
				cursor.moveToFirst();
				String filepath = cursor.getString(cursor
						.getColumnIndex(MediaStore.MediaColumns.DATA));

				exifInterface = new ExifInterface(filepath);

				String dateStr = exifInterface
						.getAttribute(ExifInterface.TAG_DATETIME);
				if (dateStr != null) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy:MM:dd HH:mm:ss");
					Date date = dateFormat.parse(dateStr);
					SimpleDateFormat dateFormat2 = new SimpleDateFormat(
							"yyyy-MM-dd");
					((EditText) findViewById(R.id.date_update_fooddate))
							.setText(dateFormat2.format(date));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
