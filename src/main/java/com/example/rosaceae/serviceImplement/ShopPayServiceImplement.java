package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Data.ShopUserDTO;
import com.example.rosaceae.model.ShopPay;
import com.example.rosaceae.model.User;
import com.example.rosaceae.repository.ShopPayRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.ShopPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopPayServiceImplement implements ShopPayService {
    @Autowired
    private ShopPayRepo shopPayRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<ShopUserDTO> getShopUsersByMonthAndYear(int month, int year) {
        List<ShopPay> shopPays = shopPayRepo.findByMonthAndYear(month, year);

        return shopPays.stream()
                .map(shopPay -> {
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
                })
                .collect(Collectors.toList());
    }
}
