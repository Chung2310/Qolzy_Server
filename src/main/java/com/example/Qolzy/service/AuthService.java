package com.example.Qolzy.service;

import com.example.Qolzy.dto.UserEntityDTO;
import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.Messages;
import com.example.Qolzy.model.ResponseHandler;
import com.example.Qolzy.model.auth.*;
import com.example.Qolzy.mapper.UserMapper;
import com.example.Qolzy.repository.UserRepository;
import com.example.Qolzy.security.JwtTokenProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.protobuf.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired(required = false)
    private UserMapper userMapper;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("🔍 [loadUserByUsername] Đang tìm người dùng theo email: {}", email);
        if(email == null ){
            return null;
        }

        UserEntity userEntity = userService.findUserByEmail(email);

        if (userEntity == null) {
            log.warn(" [loadUserByUsername] Không tìm thấy người dùng với email: {}", email);
            throw new UsernameNotFoundException("Không tìm thấy người dùng!");
        }

        log.debug(" [loadUserByUsername] Tìm thấy người dùng: {}", userEntity);

        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                new ArrayList<>()
        );
    }

    public ResponseEntity<ApiResponse<LoginResponse>> loginOrRegisterWithFirebase(LoginRequestFirebase loginRequestFirebase) {
        String idToken = loginRequestFirebase.getIdToken();
        if (idToken == null) {
            log.warn("[Firebase Login] Missing idToken in request");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        try {
            log.info("[Firebase Login] Verifying idToken...");

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = (String) decodedToken.getClaims().get("name");
            String url = decodedToken.getPicture();

            log.info("[Firebase Login] Token verified successfully. uid={}, email={}, name={}, picture={}", uid, email, name, url);

            UserEntity user = userService.findUserByProviderId(uid);

            if (user == null) {
                log.info("[Firebase Login] User not found in DB. Creating new user with uid={}", uid);

                user = new UserEntity();
                user.setProviderId(uid);
                user.setProvider(UserEntity.AuthProviderEnum.GOOGLE);
                user.setEmail(email);
                user.setLastName(name);
                user.setAvatarUrl(url);

                user = userService.saveUser(user);

                log.info("[Firebase Login] New user created with id={} and email={}", user.getId(), user.getEmail());
            } else {
                log.info("[Firebase Login] Existing user found: id={}, email={}", user.getId(), user.getEmail());
            }

            String accessToken = jwtTokenProvider.generateAccessToken(user);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user);

            log.info("[Firebase Login] Generated JWT tokens for user id={}", user.getId());

            LoginResponse loginResponse = userMapper.toLoginResponse(user);
            loginResponse.setToken(accessToken);
            loginResponse.setRefreshToken(refreshToken);

            log.info("[Firebase Login] Login process completed successfully for user id={}", user.getId());

            return ResponseHandler.generateResponse(Messages.LOGIN_SUCCESS, HttpStatus.OK, loginResponse);

        } catch (Exception e) {
            log.error("[Firebase Login] Invalid Firebase token. Error={}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.INVALID_FIREBASE_TOKEN, HttpStatus.UNAUTHORIZED, null);
        }
    }



    public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest loginRequest) {
        log.info(" [login] Bắt đầu xử lý đăng nhập cho: {}", loginRequest.getEmail());
        if(loginRequest.getEmail() == null || loginRequest.getPassword() == null){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST,null);
        }

        UserEntity userEntity = userService.findUserByEmail(loginRequest.getEmail());
        if (userEntity == null) {
            log.warn(" [login] Không tìm thấy người dùng với email: {}", loginRequest.getEmail());
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND,HttpStatus.NOT_FOUND,null);
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            log.warn(" [login] Mật khẩu không đúng cho email: {}", loginRequest.getEmail());
            return ResponseHandler.generateResponse(Messages.INVALID_CREDENTIALS,HttpStatus.UNAUTHORIZED,null);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(userEntity);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userEntity);

        log.info(" [login] Access Token: {}", accessToken);
        log.info(" [login] Refresh Token: {}", refreshToken);

        LoginResponse loginResponse = userMapper.toLoginResponse(userEntity);
        loginResponse.setToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);

        log.info("[login] token: {}",accessToken);

        log.info(" [login] Đăng nhập thành công cho: {}", loginRequest.getEmail());

        return ResponseHandler.generateResponse(Messages.LOGIN_SUCCESS,HttpStatus.OK, loginResponse);
    }

    public ResponseEntity<ApiResponse<String>> register(RegisterRequest registerRequest) {

        String email = registerRequest.getEmail();
        String firstName = registerRequest.getFirstName();
        String lastName = registerRequest.getLastName();
        String userName = registerRequest.getUserName();

        log.info("Yêu cầu đăng ký với email: {}", registerRequest.getEmail());
        if(email == null || registerRequest.getPassword() == null || firstName == null || lastName == null){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
        }

        if(isValidUsername(userName)){
            return ResponseHandler.generateResponse(Messages.USER_NAME_NOT_FORMAT, HttpStatus.BAD_REQUEST, null);
        }

        if(userService.checkUserNameIsUsed(userName)){
            return ResponseHandler.generateResponse(Messages.USER_NAME_USED, HttpStatus.BAD_REQUEST, null);
        }

        // Kiểm tra nếu email đã tồn tại
        if (userService.findUserByEmail(email) != null) {
            log.warn("Email {} đã tồn tại trong hệ thống.", email);
            return ResponseHandler.generateResponse(Messages.USER_ALREADY_EXISTS,HttpStatus.CONFLICT,null);
        }

        if (!isValidEmail(email)){
            log.warn("Email {} sai định dạng", email);
            return ResponseHandler.generateResponse(Messages.EMAIL_NOT_FORMAT,HttpStatus.BAD_REQUEST,null);
        }

        // Mã hóa mật khẩu
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserEntity user = new UserEntity();
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);


        log.debug("Mật khẩu đã được mã hóa cho email: {}", email);

        UserEntity savedUserEntity = userService.saveUser(user);
        log.info("Tạo mới người dùng thành công: ID = {}, Email = {}", savedUserEntity.getId(), savedUserEntity.getEmail());

        return ResponseHandler.generateResponse(Messages.USER_CREATED,HttpStatus.CREATED, null);
    }

    public ResponseEntity<ApiResponse<RefreshTokenRequest>> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            if(refreshTokenRequest.getAccessToken() == null || refreshTokenRequest.getRefreshToken() == null){
                return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO,HttpStatus.BAD_REQUEST,null);
            }

            if (!jwtTokenProvider.validateToken(refreshTokenRequest.getRefreshToken())) {
                log.warn("Refresh token không hợp lệ hoặc đã hết hạn");
                return ResponseHandler.generateResponse(Messages.INVALID_INPUT,HttpStatus.UNAUTHORIZED,null);
            }

            // 2. Trích xuất username từ token cũ
            String email = jwtTokenProvider.extractUsername(refreshTokenRequest.getRefreshToken());
            log.info("Đang làm mới token cho user: {}", email);

            // 3. Tìm user và tạo token mới
            UserEntity userEntity = userService.findUserByEmail(email);

            RefreshTokenRequest response = new RefreshTokenRequest();
            response.setAccessToken(jwtTokenProvider.generateAccessToken(userEntity));
            response.setRefreshToken(jwtTokenProvider.generateRefreshToken(userEntity));

            log.info("Token mới được tạo cho user: {}", email);

            return ResponseHandler.generateResponse(Messages.REFRESH_TOKEN_SUCCESS,HttpStatus.OK,response);

        } catch (Exception e) {
            log.error("Lỗi khi làm mới token: {}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR,HttpStatus.INTERNAL_SERVER_ERROR,null);
        }
    }

    public boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(regex);
    }

    private boolean isValidUsername(String username) {
        // regex: chỉ a-z, A-Z, 0-9, . và _
        String regex = "^(?=.{3,30}$)(?![._])(?!.*[._]{2})[a-zA-Z0-9._]+(?<![._])$";
        return username.matches(regex);
    }

    public ResponseEntity<ApiResponse<UserEntityDTO>> getUserDetail(Long userId) {
        log.info("Start getUserDetail with userId: {}", userId);

        if (userId == null) {
            log.warn("getUserDetail failed: userId is null");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        UserEntity userEntity = userRepository.findUserById(userId);
        if (userEntity == null) {
            log.warn("getUserDetail failed: User not found for userId {}", userId);
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        UserEntityDTO userEntityDTO = userMapper.toUserEntityDTO(userEntity);
        log.info("getUserDetail success: returning data for userId {}", userId);
        return ResponseHandler.generateResponse(Messages.DATA_FETCH_SUCCESS, HttpStatus.OK, userEntityDTO);
    }
}
