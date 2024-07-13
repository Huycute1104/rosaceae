package com.example.rosaceae.service;

import com.example.rosaceae.dto.Request.UserRequest.UserBankRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserBankResponse;

public interface UserBankService {
public UserBankResponse addUserBank(int userId,UserBankRequest userBankRequest);
public UserBankResponse updateUserBank(int bankId,UserBankRequest userBankRequest);
}
