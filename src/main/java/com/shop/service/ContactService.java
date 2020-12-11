package com.shop.service;

import java.util.List;

import com.shop.entity.Contact;

public interface ContactService {

	List<Contact> getAll();

	void createNew(Contact contact);

	void updateReply(long parseLong);

}
