package com.shop.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dto.NotificationDTO;
import com.shop.entity.Notification;
import com.shop.respository.NotificationRepository;
import com.shop.service.NotificationService;

@Service
public class NotificationServiceImp implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public void createNew(NotificationDTO notificationDTO) {
		Notification notification = new Notification();

		notification.setType(notificationDTO.getType());
		notification.setEmail(notificationDTO.getEmail());
		notification.setDate(notificationDTO.getDate());

		notificationRepository.saveAndFlush(notification);
	}

	@Override
	public List<Notification> getAllNotSeen() {
		return notificationRepository.findAllNotSeen();
	}

	@Override
	public void clearAll() {
		notificationRepository.deleteAll();
	}

	@Override
	public void deleteByID(long id) {
		notificationRepository.deleteById(id);
	}

}
