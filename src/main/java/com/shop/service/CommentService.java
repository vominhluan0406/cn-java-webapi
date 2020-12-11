package com.shop.service;

import java.util.List;

import com.shop.dto.CommentDTO;
import com.shop.entity.Comment;

public interface CommentService {

	void addNew(CommentDTO comment);

	List<Comment> getAll();

	List<Comment> getByProductID(long id);

	void upadteComment(long id, CommentDTO comment);

}