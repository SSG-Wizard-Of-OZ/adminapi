package org.oz.adminapi.adminlogin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.oz.adminapi.adminlogin.domain.AdminLoginEntity;
import org.oz.adminapi.adminlogin.dto.AdminLoginDTO;
import org.oz.adminapi.adminlogin.exception.AdminLoginException;
import org.oz.adminapi.adminlogin.repository.AdminLoginRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class AdminLoginService {

    private final AdminLoginRepository adminLoginRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminLoginDTO authenticate(String adminId, String adminPw){

        Optional<AdminLoginEntity> result = adminLoginRepository.findById(adminId);

        AdminLoginEntity admin = result.orElseThrow(() -> AdminLoginException.BAD_AUTH.getException());

        String enterPw = admin.getAdminPw();
        boolean match = passwordEncoder.matches(adminPw, enterPw);

        if(!match){
            log.info("Admin ID and Admin PW do not match");
            throw AdminLoginException.BAD_AUTH.getException();
        }

        AdminLoginDTO adminLoginDTO = new AdminLoginDTO();
        adminLoginDTO.setAdminID(adminId);
        adminLoginDTO.setAdminPw(adminPw);
        adminLoginDTO.setAdminName(admin.getAdminName());

        return adminLoginDTO;
    }
}