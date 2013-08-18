package com.example.ramenapliv4.dto;

public class RamenFoodDTO {

	private Integer id;
	private Integer shopId;
	private String name;
	private Integer rating;
	private String comment;
	private String date;
	private byte[] pict;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public byte[] getPict() {
		return pict;
	}

	public void setPict(byte[] pict) {
		this.pict = pict;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
