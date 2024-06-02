package com.example.rosaceae;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rosaceae.Format.HTMLFormat;
import com.example.rosaceae.auth.AuthenticationService;
import com.example.rosaceae.auth.RegisterRequest;
import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Request.ItemRequest.ItemRequest;
import com.example.rosaceae.dto.Request.RankMemberRequest.CreateRankRequet;
import com.example.rosaceae.dto.Response.ItemTypeResponse.ItemTypeRequest;
import com.example.rosaceae.model.Report;
import com.example.rosaceae.repository.ReportRepo;
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
	private final ReportRepo reportRepo;

    public static void main(String[] args) {
        SpringApplication.run(RosaceaeApplication.class, args);
    }

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public Cloudinary cloudinary() {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", "dpxs39hkb",
				"api_key", "679575712278322",
				"api_secret", "KJfkzpiXRnmkPCeRwH6TUAmFGks"));
		return cloudinary;
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

	@Bean
	public CommandLineRunner commandLineRunner2(

	) {
		return args -> {
			// Dummy data for reports
			String[] reportNames = {"Shop fake", "Hàng kém chất lượng", "Lý do khác"};
			for (String reportName : reportNames) {
				Report report = Report.builder().reportName(reportName).build();
				reportRepo.save(report);
			}
		};}
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
					.enabled(true)
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
					.enabled(true)
					.build();
			System.out.println("Admin token :" + service.register(admin).getAccessToken());

			//shop
			var shop = RegisterRequest.builder()
					.name("Gà Spa")
					.email("gaspa@gmail.com")
					.status(true)
					.password("123")
					.phone("0854512367")
					.role(SHOP)
					.enabled(true)
					.images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717326740/uvojrs42h2rtw4lpkkiy.jpg")
					.build();
			System.out.println("Shop token :" + service.register(shop).getAccessToken());

			var shop2 = RegisterRequest.builder()
					.name("Seoul Center")
					.email("seoulcenter@gmail.com")
					.status(true)
					.password("123")
					.phone("0326541287")
					.role(SHOP)
					.enabled(true)
					.images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717326881/qxlbkep8pol7eb8w833i.png")
					.build();
			service.register(shop2);

			var shop3= RegisterRequest.builder()
					.name("Ocean Nail Quận 9")
					.email("oceannail@gmail.com")
					.status(true)
					.password("123")
					.phone("0912268183")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop3);

			var shop4= RegisterRequest.builder()
					.name("Serene Spa")
					.email("serensepa@gmail.com")
					.status(true)
					.password("123")
					.phone("0254859865")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop4);
			var shop5= RegisterRequest.builder()
					.name("HerbalSpa")
					.email("HerbalSpa11@gmail.com")
					.status(true)
					.password("123")
					.phone("0365412478")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop5);
			var shop6= RegisterRequest.builder()
					.name("Coral Spa")
					.email("CoralSpa@gmail.com")
					.status(true)
					.password("123")
					.phone("0912268183")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop6);
			var shop7= RegisterRequest.builder()
					.name("La Belle Vie Spa")
					.email("LaBelleVieSpa@gmail.com")
					.status(true)
					.password("123")
					.phone("0912268183")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop7);
			var shop8= RegisterRequest.builder()
					.name("Lily Nail")
					.email("LilyNail@gmail.com")
					.status(true)
					.password("123")
					.phone("0938070872")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop8);
			var shop9= RegisterRequest.builder()
					.name("Chang Beauty")
					.email("ChangBeauty@gmail.com")
					.status(true)
					.password("123")
					.phone("0935138782")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop9);
			var shop10= RegisterRequest.builder()
					.name("Shine Nails & Beauty")
					.email("ShineNailsBeauty@gmail.com")
					.status(true)
					.password("123")
					.phone("0909985329")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop10);
			var shop11= RegisterRequest.builder()
					.name(" Spa gội đầu dưỡng sinh Mộc Nhiên")
					.email("mocnhien@gmail.com")
					.status(true)
					.password("123")
					.phone(" 0906234226")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop11);
			var shop12= RegisterRequest.builder()
					.name("Hi Beauty Spa")
					.email("HiBeautySpa@gmail.com")
					.status(true)
					.password("123")
					.phone("0906234226")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop12);
//			var shop8= RegisterRequest.builder()
//					.name("Lily Nail")
//					.email("LilyNail@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0938070872")
//					.role(SHOP)
//					.enabled(true)
//					.images("")
//					.build();
//			service.register(shop8);
//			var shop9= RegisterRequest.builder()
//					.name("Chang Beauty")
//					.email("ChangBeauty@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0935138782")
//					.role(SHOP)
//					.enabled(true)
//					.images("")
//					.build();
//			service.register(shop9);
//			var shop10= RegisterRequest.builder()
//					.name("Shine Nails & Beauty")
//					.email("ShineNailsBeauty@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0909985329")
//					.role(SHOP)
//					.enabled(true)
//					.images("")
//					.build();
//			service.register(shop10);
//			service.register(shop5);
//			var shop6= RegisterRequest.builder()
//					.name("Coral Spa")
//					.email("CoralSpa@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0912268183")
//					.role(SHOP)
//					.enabled(true)
//					.images("")
//					.build();
//			service.register(shop6);
//			var shop7= RegisterRequest.builder()
//					.name("La Belle Vie Spa")
//					.email("LaBelleVieSpa@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0912268183")
//					.role(SHOP)
//					.enabled(true)
//					.images("")
//					.build();
//			service.register(shop7);
//			var shop8= RegisterRequest.builder()
//					.name("Lily Nail")
//					.email("LilyNail@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0938070872")
//					.role(SHOP)
//					.enabled(true)
//					.images("")
//					.build();
//			service.register(shop8);
//			var shop9= RegisterRequest.builder()
//					.name("Chang Beauty")
//					.email("ChangBeauty@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0935138782")
//					.role(SHOP)
//					.enabled(true)
//					.images("")
//					.build();
//			service.register(shop9);
//			var shop10= RegisterRequest.builder()
//					.name("Shine Nails & Beauty")
//					.email("ShineNailsBeauty@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0909985329")
//					.role(SHOP)
//					.enabled(true)
//					.images("")
//					.build();
//			service.register(shop10);

			//Customer
			var customer = RegisterRequest.builder()
					.name("Trần Huy")
					.email("huypt110402@gmail.com")
					.status(true)
					.password("123")
					.phone("0854512367")
					.role(CUSTOMER)
					.rankId(1)
					.enabled(true)
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
					.enabled(true)
					.build();
			service.register(customer2);

			var customer3 = RegisterRequest.builder()
					.name("Hữu Phước")
					.email("phuchuu@gmail.com")
					.status(true)
					.password("123")
					.phone("0326514587")
					.role(CUSTOMER)
					.rankId(1)
					.enabled(true)
					.build();
			service.register(customer3);

			var customer4 = RegisterRequest.builder()
					.name("Phụng Vi")
					.email("vivi@gmail.com")
					.status(true)
					.password("123")
					.phone("0365216987")
					.role(CUSTOMER)
					.rankId(1)
					.enabled(true)
					.build();
			service.register(customer4);

			//dummy data category
			var category = CreateCategoryRequest.builder()
					.categoryName("Chăm sóc da")
					.build();
			categoryService.createCategory(category);

			var category1 = CreateCategoryRequest.builder()
					.categoryName("Chăm sóc body")
					.build();
			categoryService.createCategory(category1);

			var category2 = CreateCategoryRequest.builder()
					.categoryName("Làm Móng")
					.build();
			categoryService.createCategory(category2);

			var category3 = CreateCategoryRequest.builder()
					.categoryName("Gội đầu")
					.build();
			categoryService.createCategory(category3);
			var category4 = CreateCategoryRequest.builder()
					.categoryName("Massage")
					.build();
			categoryService.createCategory(category4);

			// dummy data itemType
			var itemType = ItemTypeRequest.builder()
					.name("Dịch vụ")
					.build();
			itemTypeService.createItemType(itemType);
			var itemType1 = ItemTypeRequest.builder()
					.name("Sản phẩm")
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
}
