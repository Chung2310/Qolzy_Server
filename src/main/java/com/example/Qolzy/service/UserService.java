package com.example.Qolzy.service;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.Messages;
import com.example.Qolzy.model.ResponseHandler;
import com.example.Qolzy.model.auth.UserEntity;
import com.example.Qolzy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public UserEntity findUserByUserId(Long id) {
        log.info("[findUserById] Tìm người dùng với ID: {}", id);
        return userRepository.findUserById(id);
    }

    public UserEntity findUserByUsername(String firstName, String lastName) {
        log.info("[findUserByUsername] Tìm người dùng với username: {}, {}", firstName, lastName);
        return userRepository.findUserByFirstNameAndLastNameContainingIgnoreCase(firstName, lastName);
    }

    public UserEntity findUserByEmail(String email) {
        log.info("[findUserByEmail] Tìm người dùng với email: {}", email);
        return userRepository.findUserByEmail(email);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        log.info("[saveUser] Lưu người dùng với email: {}", userEntity.getEmail());
        return userRepository.save(userEntity);
    }

    public boolean checkUserNameIsUsed(String userName){
        log.info("[checkUserNameIsUsed] Kiểm tra tên người dùng có tồn tại {}", userName);
        return userRepository.existsByUserName(userName);
    }

    public UserEntity findUserByPhone(String phone) {
        log.info("[findUserByPhone] Tìm người dùng với sdt: {}", phone);
        return userRepository.findUserByPhone(phone);
    }
    public UserEntity findUserByProviderId(String  providerId) {
        log.info("[findUserByPhone] Tìm người dùng với providerId: {}", providerId);
        return userRepository.findUserByProviderId(providerId);
    }


    public ResponseEntity<ApiResponse<String>> saveUserName(String userName, Long userId) {
        log.info(" Nhận request cập nhật tên người dùng. userId={}, userName={}", userId, userName);

        if (userName == null || userId == null) {
            log.warn("️ Thiếu thông tin bắt buộc: userId={}, userName={}", userId, userName);
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        if (checkUserNameIsUsed(userName)) {
            log.warn(" Tên người dùng '{}' đã tồn tại trong hệ thống.", userName);
            return ResponseHandler.generateResponse(Messages.USER_NAME_USED, HttpStatus.BAD_REQUEST, null);
        }

        UserEntity userEntity = userRepository.findUserById(userId);
        if (userEntity == null) {
            log.error(" Không tìm thấy người dùng với id={}", userId);
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        log.debug(" Người dùng tìm thấy trong DB: {}", userEntity);
        userEntity.setUserName(userName);
        userRepository.save(userEntity);
        log.info(" Cập nhật tên người dùng thành công. userId={}, newUserName={}", userId, userName);

        return ResponseHandler.generateResponse(Messages.USER_UPDATED, HttpStatus.OK, null);
    }

}
