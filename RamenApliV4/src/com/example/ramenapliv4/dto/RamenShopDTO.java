package com.example.ramenapliv4.dto;

public class RamenShopDTO {

	private Integer id;
	private String name;
	private Double latitude;
	private Double longitude;
	private Integer rating;
	private String comment;
	private byte[] pict;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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

}
