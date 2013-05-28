package com.example.library.service;

import com.example.library.model.entity.Reply;

public interface ReplyService {
    // public List<Reply> findById();
    // public Reply save(Reply reply);
    // public void delete(Reply reply);
    public Reply addNew(Reply reply) throws Exception;
}