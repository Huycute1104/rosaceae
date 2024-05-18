package com.example.rosaceae;

import com.example.rosaceae.Format.HTMLFormat;
import com.example.rosaceae.auth.AuthenticationService;
import com.example.rosaceae.auth.RegisterRequest;
import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Request.ItemRequest.ItemRequest;
import com.example.rosaceae.dto.Request.RankMemberRequest.CreateRankRequet;
import com.example.rosaceae.dto.Response.ItemTypeResponse.ItemTypeRequest;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.*;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.rosaceae.enums.Role.*;

@SpringBootApplication
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/")
public class RosaceaeApplication {

    private final UserRepo userRepo;
    private final JavaMailSender javaMailSender;
    private final EmailService emailService;
	@Value("${spring.mail.username}")
	private String sender;

    public static void main(String[] args) {
        SpringApplication.run(RosaceaeApplication.class, args);
    }

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			RankMemberService memberService,
            CategoryService categoryService,
			ItemTypeService itemTypeService,
			ItemService itemService
	) {
		return args -> {
			// dummy data rank
			var rank = CreateRankRequet.builder()
					.rankName("Bronze")
					.build();
			var bronze = memberService.createRank(rank);

			var rank2 = CreateRankRequet.builder()
					.rankName("Silver")
					.build();
			memberService.createRank(rank2);
			var rank3 = CreateRankRequet.builder()
					.rankName("Gold")
					.build();
			memberService.createRank(rank3);
			//dummy data user
			//Supper admin
			var super_admin = RegisterRequest.builder()
					.name("SuperAdmin")
					.email("SuperAdmin@gmail.com")
					.password("123")
					.phone("0392272536")
					.status(true)
					.role(SUPPER_ADMIN)
					.build();
			System.out.println("Super Admin token :" + service.register(super_admin).getAccessToken());

			//admin
			var admin = RegisterRequest.builder()
					.name("Admin")
					.email("admin@gmail.com")
					.status(true)
					.password("123")
					.phone("0854512367")
					.role(ADMIN)
					.build();
			System.out.println("Admin token :" + service.register(admin).getAccessToken());

			//shop
			var shop = RegisterRequest.builder()
					.name("Rosaceae Shop")
					.email("rosaceae001@gmail.com")
					.status(true)
					.password("123")
					.phone("0854512367")
					.role(SHOP)
					.build();
			System.out.println("Shop token :" + service.register(shop).getAccessToken());

			//Customer
			var customer = RegisterRequest.builder()
					.name("Trần Huy")
					.email("huypt110402@gmail.com")
					.status(true)
					.password("123")
					.phone("0854512367")
					.role(CUSTOMER)
					.rankId(1)
					.build();
			System.out.println("Customer token :" + service.register(customer).getAccessToken());

			var customer2 = RegisterRequest.builder()
					.name("Huyền Trân")
					.email("tran123@gmail.com")
					.status(true)
					.password("123")
					.phone("0854512367")
					.role(CUSTOMER)
					.rankId(1)
					.build();
			service.register(customer2);

			//dummy data category
            var category = CreateCategoryRequest.builder()
                    .categoryName("Facial")
                    .build();
            categoryService.createCategory(category);

            var category1 = CreateCategoryRequest.builder()
                    .categoryName("BodyCare")
                    .build();
            categoryService.createCategory(category1);

            var category2 = CreateCategoryRequest.builder()
                    .categoryName("Nail")
                    .build();
            categoryService.createCategory(category2);

            var category3 = CreateCategoryRequest.builder()
                    .categoryName("HairCare")
                    .build();
            categoryService.createCategory(category3);
            var category4 = CreateCategoryRequest.builder()
                    .categoryName("Relaxation")
                    .build();
            categoryService.createCategory(category4);

			// dummy data itemType
			var itemType = ItemTypeRequest.builder()
					.name("Service")
					.build();
			itemTypeService.createItemType(itemType);
			var itemType1 = ItemTypeRequest.builder()
					.name("Product")
					.build();
			itemTypeService.createItemType(itemType1);

			// dummy data Item
			//service
			var item = ItemRequest.builder()
					.itemName("Service")
					.quantity(0)
					.price(10f)
					.description("hihi")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(1)
					.itemTypeId(1)
					.discount(10)
					.build();
			itemService.CreateItem(item);
			//product
			var item1 = ItemRequest.builder()
					.itemName("Product")
					.quantity(0)
					.price(10f)
					.description("hihi")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(1)
					.itemTypeId(1)
					.discount(10)
					.build();
			itemService.CreateItem(item1);


		};
	}

    @GetMapping("")
    public String greeting() {
        try {
            String result = "";
            String[] arr = HTMLFormat.MailHtml.split("@@@@@@@@######");
            if (arr.length == 2) {
                result = arr[0] + "lmao" + arr[1];
            }
            System.out.println(result);

            MimeMessage mimeMessage;
            mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo("trihuynh1811@gmail.com");
            mimeMessageHelper.setSubject("testing sending email");
            mimeMessageHelper.setText(result, true);
//
            String message = result;
//
            mimeMessage.setContent(message, "text/html; charset=utf-8");

//            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//
//            simpleMailMessage.setFrom("huypt110402@gmail.com");
//            simpleMailMessage.setTo("trihuynh1811@gmail.com");
//            simpleMailMessage.setSubject("test send mail ses");
//            simpleMailMessage.setText(message);

            javaMailSender.send(mimeMessage);
//            emailService.SendMail(simpleMailMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Wat sup my nigga !";
    }

}
