package com.dhlee.json.jackson;

import java.io.Serializable;

public class Contact implements Serializable {
	private String type;
	private String info;
	
	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	@Override
	public String toString() {
		return "Contact [type=" + type + ", info=" + info + "]";
	}


	public Contact(String type, String info) {
		super();
		this.type = type;
		this.info = info;
	}


	public Contact() {

	}

}
