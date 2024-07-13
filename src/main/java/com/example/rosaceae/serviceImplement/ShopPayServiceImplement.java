package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Data.ShopUserDTO;
import com.example.rosaceae.dto.Response.UserResponse.ShopPayResponse;
import com.example.rosaceae.model.ShopPay;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.ShopPayRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.ShopPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopPayServiceImplement implements ShopPayService {
    @Autowired
    private ShopPayRepo shopPayRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public Page<ShopUserDTO> getShopUsersByMonthAndYear(int month, int year, Pageable pageable) {
        Page<ShopPay> shopPays = shopPayRepo.findByMonthAndYear(month, year, pageable);

        return shopPays.map(shopPay -> {
            User user = shopPay.getUser();
            String bankName = user.getUserBanks().isEmpty() ? "" : user.getUserBanks().get(0).getBankName();
            String bankAccountNumber = user.getUserBanks().isEmpty() ? "" : user.getUserBanks().get(0).getBankAccountNumber();

            return new ShopUserDTO(
                    user.getUsersID(),
                    user.getEmail(),
                    user.getAccountName(),
                    bankName,
                    bankAccountNumber,
                    shopPay.getMoney(),
                    shopPay.isStatus()
            );
        });
    }

    @Override
    public ShopPayResponse confirmPayForShop(int shopPayId) {
        var shopPay = shopPayRepo.findById(shopPayId).orElse(null);
        if (shopPay == null) {
            return ShopPayResponse.builder()
                    .message("Shop Pay Not Found")
                    .status(404)
                    .build();
        }
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Tính số ngày của tháng hiện tại
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());
        int lastDayOfMonth = yearMonth.lengthOfMonth();

        // Kiểm tra nếu ngày hiện tại không phải là ngày cuối cùng của tháng
        if (today.getDayOfMonth() != lastDayOfMonth) {
            return ShopPayResponse.builder()
                    .message("Cannot confirm payment before the end of the month")
                    .status(400)
                    .build();
        }
        shopPay.setStatus(true);
        shopPayRepo.save(shopPay);

        return ShopPayResponse.builder()
                .message("Payment confirmed successfully")
                .status(200)
                .build();
    }

}
