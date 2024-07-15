package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.UserRequest.UserBankRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserBankResponse;
import com.example.rosaceae.model.User;
import com.example.rosaceae.model.UserBank;

import java.util.List;

public interface UserBankService {
public UserBankResponse addUserBank(int userId,UserBankRequest userBankRequest);
public UserBankResponse updateUserBank(int bankId,UserBankRequest userBankRequest);
List<UserBank> getUserBank(int userId);
}
