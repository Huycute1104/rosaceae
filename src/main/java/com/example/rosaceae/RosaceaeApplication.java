package com.example.rosaceae;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rosaceae.Format.HTMLFormat;
import com.example.rosaceae.auth.AuthenticationService;
import com.example.rosaceae.auth.RegisterRequest;
import com.example.rosaceae.dto.Data.DummyDataIImages;
import com.example.rosaceae.dto.Request.CategoryRequest.CreateCategoryRequest;
import com.example.rosaceae.dto.Request.ItemImageRequest;
import com.example.rosaceae.dto.Request.ItemRequest.ItemRequest;
import com.example.rosaceae.dto.Request.RankMemberRequest.CreateRankRequet;
import com.example.rosaceae.dto.Response.ItemTypeResponse.ItemTypeRequest;
import com.example.rosaceae.model.ItemImages;
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
			ItemService itemService,
			ItemImageService itemImageService
	) {
		return args -> {
			// dummy data rank
			var rank = CreateRankRequet.builder()
					.rankName("Hạng Đồng")
					.rankPoint(1000)
					.build();
			memberService.createRank(rank);

			var rank2 = CreateRankRequet.builder()
					.rankName("Hạng Bạc")
					.rankPoint(5000)
					.build();
			memberService.createRank(rank2);
			var rank3 = CreateRankRequet.builder()
					.rankName("Hạng Vàng")
					.rankPoint(10000)
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
			var shop13= RegisterRequest.builder()
					.name("Lady Mom Spa")
					.email("LadyMomSpa@gmail.com")
					.status(true)
					.password("123")
					.phone("0919856332")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop13);
			var shop14= RegisterRequest.builder()
					.name("Spa Ngọc Hân")
					.email("NgocHanSpa@gmail.com")
					.status(true)
					.password("123")
					.phone("0898472108")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop14);
			var shop15= RegisterRequest.builder()
					.name("LAN HOUSE SPA")
					.email("LANHOUSESPA@gmail.com")
					.status(true)
					.password("123")
					.phone("0968654977")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop15);
			var shop16= RegisterRequest.builder()
					.name("Tana Spa")
					.email("TanaSpa@gmail.com")
					.status(true)
					.password("123")
					.phone(":0909298492")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop16);
			var shop17= RegisterRequest.builder()
					.name("Ngọc Anh Spa")
					.email("NgocAnhSpa@gmail.com")
					.status(true)
					.password("123")
					.phone("0353852208")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop17);
			var shop18= RegisterRequest.builder()
					.name("THÚY SPA - MASSAGE KHỎE QUẬN 9 - GỘI ĐẦU DƯỠNG SINH")
					.email("Thuyspa09@gmail.com")
					.status(true)
					.password("123")
					.phone("0938070872")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop18);
			var shop19= RegisterRequest.builder()
					.name("Nắng Mai spa")
					.email("Nangmai@gmail.com")
					.status(true)
					.password("123")
					.phone("0962058833")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop19);
			var shop20= RegisterRequest.builder()
					.name("LAVENDER MEDICAL SPA")
					.email("LAVENDERMEDICAL@gmail.com")
					.status(true)
					.password("123")
					.phone("0932699246")
					.role(SHOP)
					.enabled(true)
					.images("")
					.build();
			service.register(shop20);

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
			//Shop gaspa
			// dịch vụ
			var item = ItemRequest.builder()
						.itemName("Liệu trình da nhiễm Corticoid")
					.quantity(0)
					.price(1000000f)
					.description("Da nhiễm Corticoid là một tình trạng xảy ra khá phổ biến hiện nay, để điều trị dứt điểm vấn đề này không hề dễ dàng và nhanh chóng được. Bạn phải cần thời gian kiên trì điều trị để lấy lại làn da tươi khỏe. Hãy cùng tham khảo qua liệu trình điều trị da nhiễm Corticoid tại Gà Spa nhé!")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(1)
					.itemTypeId(1)
					.discount(13)
					.build();
			itemService.CreateItem(item);
			var item2 = ItemRequest.builder()
					.itemName("Công nghệ điều trị nám sạm Laser Toning")
					.quantity(0)
					.price(500000f)
					.description("Laser Toning là một công nghệ hàng đầu với độ an toàn cao, đã được FDA (Cục quản lý Thực phẩm – Dược phẩm Hoa Kỳ – US Food and Drug Administration) kiểm duyệt và chứng nhận. Laser Toning được biết đến là công nghệ điều trị nám da sâu và nám hỗn hợp, đặc biệt là nám da sâu rất hiệu quả")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(1)
					.itemTypeId(1)
					.discount(10)
					.build();
			itemService.CreateItem(item2);
			var item3 = ItemRequest.builder()
					.itemName("Liệu trình điều trị Mụn lưng – viêm nang lông với công nghệ IPL")
					.quantity(0)
					.price(300000f)
					.description("IPL là tên đầy đủ của nó là Intense Pulsed Light hay gọi theo tiếng Việt là  “máy ánh sáng xung nhiệt”. Đây là một thiết bị này sử dụng xung động ánh sáng với bước sóng phù hợp để giúp điều trị một số bệnh về da. Trong đó, IPL được áp dụng phổ biến trong điều trị mụn lưng, viêm nang lông, da nhờn.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(2)
					.itemTypeId(1)
					.discount(10)
					.build();
			itemService.CreateItem(item3);
			var item4 = ItemRequest.builder()
					.itemName("Dịch vụ cắt móng và chăm sóc cơ bản")
					.quantity(0)
					.price(150000f)
					.description("Cắt, dũa móng tay/móng chân.\n" +
							"Làm sạch lớp biểu bì.\n" +
							"Massage tay/chân.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(3)
					.itemTypeId(1)
					.discount(10)
					.build();
			itemService.CreateItem(item4);
			var item5 = ItemRequest.builder()
					.itemName("Sơn móng tay/móng chân")
					.quantity(0)
					.price(130000f)
					.description("Sơn bóng.\n" +
							"Sơn màu.\n" +
							"Sơn gel (gel polish).")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(3)
					.itemTypeId(1)
					.discount(10)
					.build();
			itemService.CreateItem(item5);

			var item6 = ItemRequest.builder()
					.itemName("Gội đầu dưỡng sinh")
					.quantity(0)
					.price(100000f)
					.description("Kết hợp các kỹ thuật massage trị liệu.\n" +
							"Sử dụng các sản phẩm thảo dược hoặc tinh dầu để dưỡng tóc và da đầu.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(4)
					.itemTypeId(1)
					.discount(10)
					.build();
			itemService.CreateItem(item6);
			var item7 = ItemRequest.builder()
					.itemName("Gội đầu trị liệu")
					.quantity(0)
					.price(200000f)
					.description("Sử dụng các sản phẩm đặc trị cho các vấn đề về tóc và da đầu như gàu, tóc dầu, tóc khô, và tóc hư tổn.\n" +
							"Kết hợp các phương pháp trị liệu da đầu.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(4)
					.categoryId(2)
					.itemTypeId(1)
					.discount(10)
					.build();
			itemService.CreateItem(item7);
			//product
			var product1 = ItemRequest.builder()
					.itemName("Dầu dưỡng móng")
					.quantity(0)
					.price(100000f)
					.description("Giúp làm mềm và dưỡng ẩm vùng da quanh móng.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(3)
					.itemTypeId(2)
					.discount(10)
					.build();
			itemService.CreateItem(product1);

			var product2 = ItemRequest.builder()
					.itemName("Kem dưỡng da tay/móng")
					.quantity(0)
					.price(150000f)
					.description("Giúp da tay và móng mềm mại hơn.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(3)
					.itemTypeId(2)
					.discount(10)
					.build();
			itemService.CreateItem(product2);

			var product3 = ItemRequest.builder()
					.itemName("Sơn gel")
					.quantity(0)
					.price(150000f)
					.description("Giữ màu lâu, cần đèn UV hoặc LED để khô.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(3)
					.itemTypeId(2)
					.discount(10)
					.build();
			itemService.CreateItem(product3);
			var product4 = ItemRequest.builder()
					.itemName("Neutrogena Oil-Free Acne Wash")
					.quantity(0)
					.price(250000f)
					.description("Dùng để làm sạch da mặt, loại bỏ bụi bẩn và dầu thừa.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(1)
					.itemTypeId(2)
					.discount(10)
					.build();
			itemService.CreateItem(product4);
			var product5 = ItemRequest.builder()
					.itemName("La Roche-Posay Effaclar")
					.quantity(0)
					.price(350000f)
					.description("Dùng để làm sạch da mặt, loại bỏ bụi bẩn và dầu thừa.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(1)
					.itemTypeId(2)
					.discount(10)
					.build();
			itemService.CreateItem(product5);
			var product6 = ItemRequest.builder()
					.itemName("Cetaphil Gentle Skin Cleanser")
					.quantity(0)
					.price(300000f)
					.description("Dùng để làm sạch da mặt, loại bỏ bụi bẩn và dầu thừa.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(1)
					.itemTypeId(2)
					.discount(10)
					.build();
			itemService.CreateItem(product6);

			var product7 = ItemRequest.builder()
					.itemName("Thayers Witch Hazel Toner")
					.quantity(0)
					.price(350000f)
					.description("Giúp cân bằng độ pH cho da và làm sạch sâu hơn sau khi rửa mặt")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(1)
					.itemTypeId(2)
					.discount(10)
					.build();
			itemService.CreateItem(product7);

			var product8 = ItemRequest.builder()
					.itemName("Kiehl’s Calendula Herbal Extract Toner")
					.quantity(0)
					.price(800000f)
					.description("Dùng để làm sạch da mặt, loại bỏ bụi bẩn và dầu thừa.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(1)
					.itemTypeId(2)
					.discount(10)
					.build();
			itemService.CreateItem(product8);
			var product9 = ItemRequest.builder()
					.itemName("Simple Soothing Facial Toner")
					.quantity(0)
					.price(250000f)
					.description("Dùng để làm sạch da mặt, loại bỏ bụi bẩn và dầu thừa.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
					.shopId(3)
					.categoryId(1)
					.itemTypeId(2)
					.discount(10)
					.build();
			itemService.CreateItem(product9);



			//Dummy data for item Images
			var image = DummyDataIImages.builder()
					.itemId(1)
					.link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717403332/fccuc8itceb81wuyhddk.jpg")
					.build();
			itemImageService.CreateImage(image);


		};
	}
}
