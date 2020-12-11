package com.shop.service;

import java.util.List;

import com.shop.dto.NotificationDTO;
import com.shop.entity.Notification;

public interface NotificationService {

	void createNew(NotificationDTO notificationDTO);

	List<Notification> getAllNotSeen();

	void clearAll();

	void deleteByID(long parseLong);

}
