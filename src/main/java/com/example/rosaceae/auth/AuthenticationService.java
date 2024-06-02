package com.example.rosaceae.auth;

import com.example.rosaceae.Format.HTMLFormat;
import com.example.rosaceae.config.JwtService;
import com.example.rosaceae.dto.Request.UserRequest.CreateUserRequest;
import com.example.rosaceae.enums.Role;
import com.example.rosaceae.enums.TokenType;
import com.example.rosaceae.model.Token;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.RankMemberRepo;
import com.example.rosaceae.repository.TokeRepo;
import com.example.rosaceae.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.rosaceae.Util.StringHanlder.randomStringGenerator;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokeRepo tokeRepo;
    private final JavaMailSender javaMailSender;
    private final RankMemberRepo rankMemberRepo;

    @Value("${spring.mail.username}")
    private String sender;

    public AuthenticationResponse createUser(CreateUserRequest request) {
        String email = request.getEmail();
        String result = "";
        String randomEmailToken = randomStringGenerator(64);
        if(!isValidEmail(email)){
            return AuthenticationResponse.builder()
                    .msg("Invalid email format.")
                    .status(400)
                    .accessToken(null)
                    .refreshToken(null)
                    .build();
        }
        if(userRepo.findUserByEmail(request.getEmail()).isPresent()){
            return AuthenticationResponse.builder()
                    .msg("There already a user with this email.")
                    .status(400)
                    .accessToken(null)
                    .refreshToken(null)
                    .build();
        }
        if (!isValidPassword(request.getPassword())) {
            return AuthenticationResponse.builder()
                    .msg("The password must be at least 6 characters long and should not contain any special characters.")
                    .status(400)
                    .accessToken(null)
                    .refreshToken(null)
                    .build();
        }
        var user = User.builder()
                .email(request.getEmail())
                .accountName(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .userStatus(true)
                .role(Role.CUSTOMER)
                .verificationCode(randomEmailToken)
                .build();
        var save = userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(save, jwtToken);
        try{
            String[] arr = HTMLFormat.EmailVerificationHTML.split("######");
            String verificationURL = "http://localhost:5173/verify?token=" + randomEmailToken;
            if (arr.length == 2) {
                result = arr[0] + verificationURL + arr[1];
            }
            System.out.println(result);

            MimeMessage mimeMessage;
            mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject("Email Verification");
            mimeMessageHelper.setText(result, true);
            String message = result;
            mimeMessage.setContent(message, "text/html; charset=utf-8");

            javaMailSender.send(mimeMessage);

        }catch (Exception e){
            e.printStackTrace();
        }

        return AuthenticationResponse.builder()
                .msg("You have successfully registered, please check your mail for verification mail.")
                .status(200)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userInfo(user)
                .build();
    }

    private boolean isValidPassword(String password) {
        //Check validate password
        return password != null && password.length() >= 6 && !password.matches(".*[^a-zA-Z0-9].*");
    }
    private boolean isValidEmail(String email) {
        // check valid email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var rank = rankMemberRepo.findByRankMemberID(request.getRankId()).orElse(null);
        var user = User.builder()
                .email(request.getEmail())
                .accountName(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .userStatus(request.isStatus())
                .role(request.getRole())
                .rankMember(rank)
                .enabled(request.isEnabled())
                .coverImages(request.getImages())
                .build();
        var save = userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(save, jwtToken);
        return AuthenticationResponse.builder()
                .msg("You have successfully registered.")
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validToken = tokeRepo.findAllValidTokensByUser(user.getUsersID());
        if (validToken.isEmpty())
            return;
        validToken.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokeRepo.saveAll(validToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokeRepo.save(token);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
        var user = userRepo.findByEmail(request.getEmail()).orElseThrow();
        System.out.println(user);
        if (!user.isUserStatus() || !user.isEnabled()) {
            return AuthenticationResponse.builder()
                    .msg("User is ban or is not verified")
                    .status(400)
                    .build();
        }
        if(user == null || (user != null && !passwordEncoder.matches(request.getPassword(), user.getPassword()))){
            System.out.println(passwordEncoder.matches(request.getPassword(), user.getPassword()));
            return AuthenticationResponse.builder()
                    .msg("Wrong email or password")
                    .status(400)
                    .build();
        }
        else {
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse.builder()
                    .msg("Login successfully")
                    .status(200)
//                .userInfo(userRepo.findUserByEmail(request.getEmail()).orElseThrow())
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .userInfo(user)
                    .build();
        }

    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);// todo extract the userEmail from JWT Token
        if (userEmail != null) {
            var user = this.userRepo.findUserByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
