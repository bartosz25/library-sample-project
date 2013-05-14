package library.service.impl;

import library.model.entity.Admin;
import library.model.repository.AdminRepository;
import library.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("adminService")
@Repository
@Transactional
public class AdminServiceImpl implements AdminService {
    final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    @Autowired
    private AdminRepository adminRepository;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Admin save(Admin admin) {
        Admin result = null;
        try {
            result = adminRepository.save(admin);
        } catch (Exception e) {
            // TODO : do rollback even if result is not null !
            result = null;
        }
        return result;
    }
    
    @Override
    public Admin loadByUsername(String login) {
        return adminRepository.loadByUsername(login);
    }
}