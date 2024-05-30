package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Request.UserReportRequest.CreateUserReportRequest;
import com.example.rosaceae.dto.Response.UserResponse.UserResponse;
import com.example.rosaceae.model.Item;
import com.example.rosaceae.model.Report;
import com.example.rosaceae.model.User;
import com.example.rosaceae.model.UserReport;
import com.example.rosaceae.repository.ItemRepo;
import com.example.rosaceae.repository.ReportRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.repository.UserReportRepo;
import com.example.rosaceae.service.UserReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReportServiceImplement implements UserReportService {
    private final UserReportRepo userReportRepo;
    private final ReportRepo reportRepo;
    private final UserRepo userRepo;
    private final ItemRepo itemRepo;

    @Override
    public UserResponse createUserReport (CreateUserReportRequest userReportRequest) {
        if (userReportRequest.getUsersId().equals(userReportRequest.getShopId())) {
            return UserResponse.builder().status("Failure: User cannot report themselves").build();
        }

        User user = userRepo.findById(userReportRequest.getUsersId()).orElse(null);
        User shop = userRepo.findById(userReportRequest.getShopId()).orElse(null);
        Item item = itemRepo.findById(userReportRequest.getItemId()).orElse(null);
        Report report = reportRepo.findById(userReportRequest.getReportId()).orElse(null);

        if (user == null || shop == null || item == null || report == null) {
            return UserResponse.builder().status("Failure: Invalid user, shop, item, or report ID").build();
        }

        UserReport userReport = UserReport.builder()
                .user(user)
                .shop(shop)
                .item(item)
                .report(report)
                .description(userReportRequest.getDescription())
                .build();

        userReportRepo.save(userReport);
        return UserResponse.builder().status("Report created successfully").build();
    }
}
