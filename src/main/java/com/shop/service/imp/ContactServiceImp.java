package com.shop.service.imp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entity.Contact;
import com.shop.respository.ContactRepository;
import com.shop.service.ContactService;

@Service
public class ContactServiceImp implements ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Override
	public List<Contact> getAll() {
		return contactRepository.findAll();
	}

	@Override
	public void createNew(Contact contactInp) {
		Contact contact = new Contact();
		Date date = new Date();
		contact.setDate(date.toString());
		contact.setEmail(contactInp.getEmail());
		contact.setMessage(contactInp.getMessage());
		contact.setReply(false);
		contact.setPhone(contactInp.getPhone());
		contactRepository.save(contact);
	}

	@Override
	public void updateReply(long id) {
		Contact contact = contactRepository.findById(id).get();
		contact.setReply(true);
		contactRepository.saveAndFlush(contact);
	}

}
