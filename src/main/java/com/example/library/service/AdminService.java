package com.example.library.service;

import com.example.library.model.entity.Admin;

public interface AdminService {
    // public List<Admin> findById();
    public Admin loadByUsername(String login);
    public Admin save(Admin admin);
    // public void delete(Admin admin);
}