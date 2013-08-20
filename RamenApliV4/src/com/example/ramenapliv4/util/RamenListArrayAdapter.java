package com.example.ramenapliv4.util;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ramenapliv4.R;
import com.example.ramenapliv4.dto.RamenFoodDTO;

public class RamenListArrayAdapter extends ArrayAdapter<RamenFoodDTO> {

	private LayoutInflater inflater;
	private int textViewResourceId;
	private List<RamenFoodDTO> ramenfoodlist;

	public RamenListArrayAdapter(Context context, int textViewResourceId,
			List<RamenFoodDTO> objects) {
		super(context, textViewResourceId, objects);
		this.ramenfoodlist = objects;
		this.textViewResourceId = textViewResourceId;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView != null) {
			view = convertView;
		} else {
			view = inflater.inflate(textViewResourceId, null);
		}

		RamenFoodDTO dto = ramenfoodlist.get(position);
		Bitmap bitmap = BitmapFactory.decodeByteArray(dto.getPict(), 0,
				dto.getPict().length);
		((ImageView) view.findViewById(R.id.img_list_ramenpict))
				.setImageBitmap(bitmap);
		((TextView) view.findViewById(R.id.txt_list_ramenname)).setText(dto
				.getName());
		((RatingBar) view.findViewById(R.id.rat_list_ramenrating))
				.setRating(dto.getRating());
		((TextView) view.findViewById(R.id.txt_list_ramendate)).setText(dto
				.getDate());

		return view;
	}

}
