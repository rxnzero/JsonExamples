package com.dhlee.json.jackson;

import java.io.Serializable;
import java.util.ArrayList;

public class SimpleUser implements Serializable {
	private String name;
	private int age;
	Contact contact;
	private ArrayList<Contact> contactList = new ArrayList<Contact>();
	
	public SimpleUser() {

	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public void addContact(Contact contact) {
		contactList.add(contact);
//		this.contact = contact;
	}
	
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public SimpleUser(String name, int age, Contact contact) {
		super();
		this.name = name;
		this.age = age;
		this.contact = contact;
	}

	public ArrayList<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(ArrayList<Contact> contactList) {
		this.contactList = contactList;
	}

	@Override
	public String toString() {
		return "SimpleUser [name=" + name + ", age=" + age + "," + contact.toString() +"]";
	}
}
