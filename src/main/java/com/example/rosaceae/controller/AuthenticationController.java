package com.example.rosaceae.controller;

import com.example.rosaceae.Format.HTMLFormat;
import com.example.rosaceae.auth.AuthenticationRequest;
import com.example.rosaceae.auth.AuthenticationResponse;
import com.example.rosaceae.auth.AuthenticationService;
import com.example.rosaceae.config.LogoutService;
import com.example.rosaceae.dto.Data.UserDTO;
import com.example.rosaceae.enums.Role;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.mail.internet.MimeMessage;
import com.example.rosaceae.dto.Request.UserRequest.CreateUserRequest;
import jakarta.persistence.criteria.Join;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

import static com.example.rosaceae.Util.StringHanlder.randomStringGenerator;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final LogoutService logoutService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final UserRepo userRepo;
    private final UserService userService;

    @Value("${spring.mail.username}")
    private String sender;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(authenticationService.createUser(request));
    }
//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody CreateUserRequest request) {
//        AuthenticationResponse response = authenticationService.createUser(request);
//        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
//    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody JsonNode request){
        try {
            User user = userRepo.findUserByEmail(request.get("email").asText()).get();
            String result = "";
            String randomPassword = randomStringGenerator(10);
            String[] arr = HTMLFormat.ForgotPasswordHTML.split("######");
            if (arr.length == 2) {
                result = arr[0] + randomPassword + arr[1];
            }
            System.out.println(result);

            MimeMessage mimeMessage;
            mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(request.get("email").asText());
            mimeMessageHelper.setSubject("temporary password");
            mimeMessageHelper.setText(result, true);
            String message = result;
            mimeMessage.setContent(message, "text/html; charset=utf-8");
            user.setPassword(passwordEncoder.encode(randomPassword));
            userRepo.save(user);

            javaMailSender.send(mimeMessage);
            return ResponseEntity.ok("a temporary password have been sent to this email, use it to login and change the password");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().body("something in the server went gone");
    }

    @PostMapping("/email-verify")
    public ResponseEntity<String> emailVerify(@RequestBody JsonNode request){
        try{
            String token = request.get("token").asText();
            if(userService.emailVerify(token)){
                return ResponseEntity.ok("Email have been verified");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error while verify email");
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request,response);

    }

}
