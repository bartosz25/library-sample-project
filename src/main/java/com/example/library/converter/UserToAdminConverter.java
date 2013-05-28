package com.example.library.converter;


import com.example.library.model.entity.Admin;
import com.example.library.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.User;

public class UserToAdminConverter implements Converter<User, Admin> {
    @Autowired
    private AdminService adminService;

    public Admin convert(User user) {
        return adminService.loadByUsername(user.getUsername());
    }
}