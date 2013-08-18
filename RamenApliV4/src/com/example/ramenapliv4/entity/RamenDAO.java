package com.example.ramenapliv4.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.ramenapliv4.dto.RamenFoodDTO;
import com.example.ramenapliv4.dto.RamenShopDTO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RamenDAO {

	private SQLiteDatabase db;

	public RamenDAO(SQLiteDatabase db) {
		this.db = db;
	}

	public List<RamenShopDTO> findAllRamenShop() {
		List<RamenShopDTO> list = new ArrayList<RamenShopDTO>();
		Cursor cursor = db.query("ramen_shop", null, null, null, null, null,
				null);
		while (cursor.moveToNext()) {
			RamenShopDTO dto = new RamenShopDTO();
			dto.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			dto.setName(cursor.getString(cursor.getColumnIndex("name")));
			dto.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
			dto.setLongitude(cursor.getDouble(cursor
					.getColumnIndex("longitude")));
			dto.setRating(cursor.getInt(cursor.getColumnIndex("rating")));
			dto.setComment(cursor.getString(cursor.getColumnIndex("comment")));
			list.add(dto);
		}
		return list;
	}

	public void insertRamenShop(RamenShopDTO dto) {
		ContentValues values = new ContentValues();
		values.put("name", dto.getName());
		values.put("latitude", dto.getLatitude());
		values.put("longitude", dto.getLongitude());
		values.put("rating", dto.getRating());
		values.put("comment", dto.getComment());
		values.put("pict", dto.getPict());
		db.insert("ramen_shop", null, values);
	}

	public void updateRamenShop(RamenShopDTO dto) {
		ContentValues values = new ContentValues();
		values.put("name", dto.getName());
		values.put("latitude", dto.getLatitude());
		values.put("longitude", dto.getLongitude());
		values.put("rating", dto.getRating());
		values.put("comment", dto.getComment());
		values.put("pict", dto.getPict());
		db.update("ramen_shop", values, "_id=?", new String[]{dto.getId().toString()});
	}

	public RamenShopDTO findRamenShopById(String idstr) {
		RamenShopDTO dto = null;
		Cursor cursor = db.query("ramen_shop", null, "_id = ?",
				new String[] { idstr }, null, null, null);
		if (cursor.moveToFirst()) {
			dto = new RamenShopDTO();
			dto.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			dto.setName(cursor.getString(cursor.getColumnIndex("name")));
			dto.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
			dto.setLongitude(cursor.getDouble(cursor
					.getColumnIndex("longitude")));
			dto.setRating(cursor.getInt(cursor.getColumnIndex("rating")));
			dto.setComment(cursor.getString(cursor.getColumnIndex("comment")));
			dto.setPict(cursor.getBlob(cursor.getColumnIndex("pict")));
		}
		return dto;
	}

	public void deleteRamenShop(String idstr) {
		db.delete("ramen_shop", "_id = " + idstr, null);
	}

	public List<RamenFoodDTO> findRamenFoodByShopId(String idstr) {
		List<RamenFoodDTO> list = new ArrayList<RamenFoodDTO>();
		Cursor cursor = db.query("ramen_food", null, "shopid = ?",
				new String[] { idstr }, null, null, null);
		while (cursor.moveToNext()) {
			RamenFoodDTO dto = new RamenFoodDTO();
			dto.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			dto.setShopId(cursor.getInt(cursor.getColumnIndex("shopid")));
			dto.setName(cursor.getString(cursor.getColumnIndex("name")));
			dto.setRating(cursor.getInt(cursor.getColumnIndex("rating")));
			dto.setComment(cursor.getString(cursor.getColumnIndex("comment")));
			dto.setDate(cursor.getString(cursor.getColumnIndex("date")));
			dto.setPict(cursor.getBlob(cursor.getColumnIndex("pict")));
			list.add(dto);
		}
		return list;
	}

	public RamenFoodDTO findRamenFoodByFoodId(String idstr) {
		RamenFoodDTO dto = null;
		Cursor cursor = db.query("ramen_food", null, "_id = ?",
				new String[] { idstr }, null, null, null);
		if (cursor.moveToFirst()) {
			dto = new RamenFoodDTO();
			dto.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			dto.setShopId(cursor.getInt(cursor.getColumnIndex("shopid")));
			dto.setName(cursor.getString(cursor.getColumnIndex("name")));
			dto.setRating(cursor.getInt(cursor.getColumnIndex("rating")));
			dto.setComment(cursor.getString(cursor.getColumnIndex("comment")));
			dto.setDate(cursor.getString(cursor.getColumnIndex("date")));
			dto.setPict(cursor.getBlob(cursor.getColumnIndex("pict")));
		}
		return dto;
	}

	public void insertRamenFood(RamenFoodDTO dto) {
		ContentValues values = new ContentValues();
		values.put("shopid", dto.getShopId());
		values.put("name", dto.getName());
		values.put("date", dto.getDate());
		values.put("rating", dto.getRating());
		values.put("comment", dto.getComment());
		values.put("pict", dto.getPict());
		long result = db.insert("ramen_food", null, values);
		if (result == -1) {
			throw new RuntimeException("miss insertion");
		}
	}

	public void updateRamenFood(RamenFoodDTO dto) {
		ContentValues values = new ContentValues();
		values.put("shopid", dto.getShopId());
		values.put("name", dto.getName());
		values.put("date", dto.getDate());
		values.put("rating", dto.getRating());
		values.put("comment", dto.getComment());
		values.put("pict", dto.getPict());
		db.update("ramen_food", values, "_id=?", new String[]{dto.getId().toString()});
	}

	public void deleteRamenFood(String idstr) {
		db.delete("ramen_food", "_id = " + idstr, null);
	}

}
