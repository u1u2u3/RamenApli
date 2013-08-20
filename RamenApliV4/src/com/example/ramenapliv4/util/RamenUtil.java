package com.example.ramenapliv4.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;

public class RamenUtil {

	public static Bitmap getResizedBitmap(Uri imageUri, Activity activity) {

		Bitmap resizedBitmap = null;
		try {
			ContentResolver resolver = activity.getContentResolver();
			InputStream in = resolver.openInputStream(imageUri);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			in.close();

			// 画面サイズを取得する
			Matrix matrix = new Matrix();
			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			float screenWidth = (float) metrics.widthPixels;
			float screenHeight = (float) metrics.heightPixels;

			// 元画像のサイズを取得する
			int srcWidth = bitmap.getWidth();
			int srcHeight = bitmap.getHeight();

			// 画面に収まるように元画像を調整する
			float widthScale = screenWidth / srcWidth;
			float heightScale = screenHeight / srcHeight;
			if (widthScale > heightScale) {
				matrix.postScale(heightScale, heightScale);
			} else {
				matrix.postScale(widthScale, widthScale);
			}
			resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, srcWidth,
					srcHeight, matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resizedBitmap;
	}

	public static byte[] convertBitmapToByte(Bitmap shopbitmap) {
		ByteArrayOutputStream stream;
		int quality = 105;
		int MAX_IMAGE_SIZE = 100 * 1024;
		do {
			quality -= 5;
			stream = new ByteArrayOutputStream();
			shopbitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
		} while (stream.toByteArray().length >= MAX_IMAGE_SIZE);
		Log.i("ramenapli", "圧縮率:" + quality);
		Log.i("ramenapli", "画像サイズ:" + stream.toByteArray().length);
		return stream.toByteArray();
	}

}
