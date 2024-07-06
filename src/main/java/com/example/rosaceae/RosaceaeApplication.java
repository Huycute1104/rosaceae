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
import com.example.rosaceae.dto.Request.LocationRequest.LocationRequest;
import com.example.rosaceae.dto.Request.OrderRequest.CreateOrderRequest;
import com.example.rosaceae.dto.Request.RankMemberRequest.CreateRankRequet;
import com.example.rosaceae.dto.Request.TimeBookingRequest.TimeBookingRequest;
import com.example.rosaceae.dto.Response.ItemTypeResponse.ItemTypeRequest;
import com.example.rosaceae.enums.BookingStatus;
import com.example.rosaceae.enums.OrderStatus;
import com.example.rosaceae.model.*;
import com.example.rosaceae.repository.*;
import com.example.rosaceae.service.*;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private TimeBookingRepo timeBookingRepo;
    @Autowired
    private ItemRepo itemRepo;

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
        };
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthenticationService service,
            RankMemberService memberService,
            CategoryService categoryService,
            ItemTypeService itemTypeService,
            ItemService itemService,
            ItemImageService itemImageService,
            LocationService locationService,
            TimeBookingService timeBookingService,
            OrderService orderService
//			FeeService feeService
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
                    .locationUrl("https://maps.app.goo.gl/FTWtDaEja8t9DnQs6")
                    .address("1 Đ. Hoàng Diệu 2, Phường Linh Trung, Thủ Đức, Thành phố Hồ Chí Minh, Việt Nam")
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
                    .locationUrl("https://maps.app.goo.gl/8ZdDUkDrpkqBnSVcA")
                    .address("375 Đ. Nguyễn Thượng Hiền, Phường 11, Quận 10, Thành phố Hồ Chí Minh 700000, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717326881/qxlbkep8pol7eb8w833i.png")
                    .build();
            service.register(shop2);
            var shopReal = RegisterRequest.builder()
                    .name("Bliss Nails Studio")
                    .email("blissnail@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0921592529")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/8pS2KnuL2iNqqyiu6")
                    .address("682/8 Đ. Quang Trung, Phường 11, Gò Vấp, Hồ Chí Minh, Việt Nam")
                    .images("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719567543/sasg5vrajatirouzqxlo.jpg")
                    .build();
            service.register(shopReal);
            var shop3 = RegisterRequest.builder()
                    .name("Hơ-nie Nails & Eyelash")
                    .email("oceannail@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0912268183")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/7dSVtVgQR1B4f5cd9")
                    .address("79 Đ Làng Tăng Phú, Tăng Nhơn Phú A, Quận 9, Thành phố Hồ Chí Minh 700000, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717810522/wjwijr686epskqqfqamp.jpg")
                    .build();
            service.register(shop3);

            var shop4 = RegisterRequest.builder()
                    .name("Serene Spa")
                    .email("serensepa@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0254859865")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/V1oHMj1uK3dJRyUh6")
                    .address("7A/87, Thành Thái, Phường 14, Quận 10, Thành phố Hồ Chí Minh, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717810654/giy29isjrk6hhtg3skaq.jpg")
                    .build();
            service.register(shop4);
            var shop5 = RegisterRequest.builder()
                    .name("HerbalSpa")
                    .email("HerbalSpa11@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0365412478")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/qsPRzH4KPTaLYK7s5")
                    .address("421/20 Đ. Sư Vạn Hạnh, Phường 12, Quận 10, Thành phố Hồ Chí Minh 12345, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717810701/etfgdgqaan5pargu3iey.png")
                    .build();
            service.register(shop5);
            var shop6 = RegisterRequest.builder()
                    .name("Coral Spa")
                    .email("CoralSpa@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0912268183")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/NioPqppHv9bkYQyL8")
                    .address("69 Nguyễn Phúc Tần, Phường Minh An, Hội An, Quảng Nam, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811792/pjlybbvwtrpyp2pqntlp.png")
                    .build();
            service.register(shop6);
            var shop7 = RegisterRequest.builder()
                    .name("La Belle Vie Spa")
                    .email("LaBelleVieSpa@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0912268183")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/3ZA3Uf5yAgUGaczK6")
                    .address("258 Võ Nguyên Giáp, Phước Mỹ, Sơn Trà, Đà Nẵng 550000, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717810756/qff61icaqlxci1xvmddr.png")
                    .build();
            service.register(shop7);
            var shop8 = RegisterRequest.builder()
                    .name("Lily Nail")
                    .email("LilyNail@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0938070872")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/6Py4NFKCxZf8NhZe7")
                    .address("377 Đ. Man Thiện, Phường Tân Phú, Quận 9, Thành phố Hồ Chí Minh 71010, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717810925/w0nt15j2xmpbsyeng4p3.jpg")
                    .build();
            service.register(shop8);
            var shop9 = RegisterRequest.builder()
                    .name("Chang Beauty")
                    .email("ChangBeauty@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0935138782")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/Vhxp2MFRRBzyVtPu6")
                    .address("13K1 Đường số 40, Tân Phong, Quận 7, Thành phố Hồ Chí Minh, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717810988/ga0fd6zu5xyeqfh2dvrw.png")
                    .build();
            service.register(shop9);
            var shop10 = RegisterRequest.builder()
                    .name("Shine Nails & Beauty")
                    .email("ShineNailsBeauty@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0909985329")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/8n7W23y1YBmZSwmW8")
                    .address("264 Lê Văn Việt, Tăng Nhơn Phú B, Quận 9, Thành phố Hồ Chí Minh, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811027/ch70tcxyg83ajddmxwuz.jpg")
                    .build();
            service.register(shop10);
            var shop11 = RegisterRequest.builder()
                    .name(" Spa gội đầu dưỡng sinh Mộc Nhiên")
                    .email("mocnhien@gmail.com")
                    .status(true)
                    .password("123")
                    .phone(" 0906234226")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/67bzyJvRmNQkfdwN9")
                    .address("166/2 Đ. Nguyễn Hữu Cảnh, Phường 22, Bình Thạnh, Thành phố Hồ Chí Minh, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811097/znlmr5zfbn3bdggqptox.png")
                    .build();
            service.register(shop11);
            var shop12 = RegisterRequest.builder()
                    .name("Hi Beauty Spa")
                    .email("HiBeautySpa@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0906234226")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/cFELDNEotW7jkqd2A")
                    .address("108 đường D3 phường Phước Long B Quận 9, Thủ Đức, Thành phố Hồ Chí Minh 71210, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811176/hvd3du9g3p0m7tcjxppf.jpg")
                    .build();
            service.register(shop12);
            var shop13 = RegisterRequest.builder()
                    .name("Lady Mom Spa")
                    .email("LadyMomSpa@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0919856332")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/1iDxAALiV2UVretw9")
                    .address("11A Đ. 339, Phước Long B, TP. Thủ Đức, Thành phố Hồ Chí Minh, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811212/on3mohvszw3mmrehht4z.png")
                    .build();
            service.register(shop13);
            var shop14 = RegisterRequest.builder()
                    .name("Spa Ngọc Hân")
                    .email("NgocHanSpa@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0898472108")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/XyEGVDqUYyWK8sAv5")
                    .address("Hẻm 115 Đường số 39, Phường Bình Trưng Tây, Quận 2, Thành phố Hồ Chí Minh, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811231/rohvlm9sr0hvrjldxcik.png")
                    .build();
            service.register(shop14);
            var shop15 = RegisterRequest.builder()
                    .name("LAN HOUSE SPA")
                    .email("LANHOUSESPA@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0968654977")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/QwnhQPewHL7ixuVz9")
                    .address("131 Đ Làng Tăng Phú, QUAN 9, Thủ Đức, Thành phố Hồ Chí Minh, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811292/s3ionuxnaupmpee3yfwr.jpg")
                    .build();
            service.register(shop15);
            var shop16 = RegisterRequest.builder()
                    .name("Tana Spa")
                    .email("TanaSpa@gmail.com")
                    .status(true)
                    .password("123")
                    .phone(":0909298492")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/FVXch5p1fa8dJB2GA")
                    .address("43 Đường Số 1, Bình Trưng Đông, Thủ Đức, Thành phố Hồ Chí Minh 700000, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811348/yajg4phpfvxzvkiouelg.jpg")
                    .build();
            service.register(shop16);
            var shop17 = RegisterRequest.builder()
                    .name("Mỹ Ngọc  Spa")
                    .email("MyNgocSpa@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0353852208")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/dcPSt4USnWLfPky27")
                    .address("38 Dương Đình Hội, Phước Long B, Quận 9, Thành phố Hồ Chí Minh, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811398/ffytdr6bkqvveuhj3wxp.jpg")
                    .build();
            service.register(shop17);
            var shop18 = RegisterRequest.builder()
                    .name("LiDoSpa")
                    .email("LiDoSpa@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0938070872")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/wedkZjxxbDxZRDw9A")
                    .address("424 Lê Văn Việt, Tăng Nhơn Phú A, Thủ Đức, Thành phố Hồ Chí Minh 70000, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811495/ehfg6krfimwqhhjmltzc.jpg")
                    .build();
            service.register(shop18);
            var shop19 = RegisterRequest.builder()
                    .name("Nắng Mai spa")
                    .email("Nangmai@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0962058833")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/wedkZjxxbDxZRDw9A")
                    .address("424 Lê Văn Việt, Tăng Nhơn Phú A, Thủ Đức, Thành phố Hồ Chí Minh 70000, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811579/crjtbxqbl5z4jvr3vau6.jpg")
                    .build();
            service.register(shop19);
            var shop20 = RegisterRequest.builder()
                    .name("LAVENDER MEDICAL SPA")
                    .email("LAVENDERMEDICAL@gmail.com")
                    .status(true)
                    .password("123")
                    .phone("0932699246")
                    .role(SHOP)
                    .enabled(true)
                    .locationUrl("https://maps.app.goo.gl/ioGjn47ow5ASHeAz7")
                    .address("424 Lê Văn Việt, Tăng Nhơn Phú A, Thủ Đức, Thành phố Hồ Chí Minh 70000, Việt Nam")
                    .images("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717811615/sjtjwvqowqiu86wksxby.jpg")
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
                    .quantity(100)
                    .itemPrice(1000000f)
                    .itemDescription("Da nhiễm Corticoid là một tình trạng xảy ra khá phổ biến hiện nay, để điều trị dứt điểm vấn đề này không hề dễ dàng và nhanh chóng được. Bạn phải cần thời gian kiên trì điều trị để lấy lại làn da tươi khỏe. Hãy cùng tham khảo qua liệu trình điều trị da nhiễm Corticoid tại Gà Spa nhé!")
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
                    .quantity(100)
                    .itemPrice(500000f)
                    .itemDescription("Laser Toning là một công nghệ hàng đầu với độ an toàn cao, đã được FDA (Cục quản lý Thực phẩm – Dược phẩm Hoa Kỳ – US Food and Drug Administration) kiểm duyệt và chứng nhận. Laser Toning được biết đến là công nghệ điều trị nám da sâu và nám hỗn hợp, đặc biệt là nám da sâu rất hiệu quả")
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
                    .quantity(100)
                    .itemPrice(300000f)
                    .itemDescription("IPL là tên đầy đủ của nó là Intense Pulsed Light hay gọi theo tiếng Việt là  “máy ánh sáng xung nhiệt”. Đây là một thiết bị này sử dụng xung động ánh sáng với bước sóng phù hợp để giúp điều trị một số bệnh về da. Trong đó, IPL được áp dụng phổ biến trong điều trị mụn lưng, viêm nang lông, da nhờn.")
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
                    .quantity(100)
                    .itemPrice(150000f)
                    .itemDescription("Cắt, dũa móng tay/móng chân.\n" +
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
                    .quantity(100)
                    .itemPrice(130000f)
                    .itemDescription("Sơn bóng.\n" +
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
                    .quantity(100)
                    .itemPrice(100000f)
                    .itemDescription("Kết hợp các kỹ thuật massage trị liệu.\n" +
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
                    .quantity(100)
                    .itemPrice(200000f)
                    .itemDescription("Sử dụng các sản phẩm đặc trị cho các vấn đề về tóc và da đầu như gàu, tóc dầu, tóc khô, và tóc hư tổn.\n" +
                            "Kết hợp các phương pháp trị liệu da đầu.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(3)
                    .categoryId(2)
                    .itemTypeId(1)
                    .discount(10)
                    .build();
            itemService.CreateItem(item7);
            //product
            var product1 = ItemRequest.builder()
                    .itemName("Dầu dưỡng móng")
                    .quantity(100)
                    .itemPrice(100000f)
                    .itemDescription("Giúp làm mềm và dưỡng ẩm vùng da quanh móng.")
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
                    .quantity(100)
                    .itemPrice(150000f)
                    .itemDescription("Giúp da tay và móng mềm mại hơn.")
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
                    .quantity(100)
                    .itemPrice(150000f)
                    .itemDescription("Giữ màu lâu, cần đèn UV hoặc LED để khô.")
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
                    .quantity(100)
                    .itemPrice(250000f)
                    .itemDescription("Dùng để làm sạch da mặt, loại bỏ bụi bẩn và dầu thừa.")
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
                    .quantity(100)
                    .itemPrice(350000f)
                    .itemDescription("Dùng để làm sạch da mặt, loại bỏ bụi bẩn và dầu thừa.")
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
                    .quantity(100)
                    .itemPrice(300000f)
                    .itemDescription("Dùng để làm sạch da mặt, loại bỏ bụi bẩn và dầu thừa.")
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
                    .quantity(100)
                    .itemPrice(350000f)
                    .itemDescription("Giúp cân bằng độ pH cho da và làm sạch sâu hơn sau khi rửa mặt")
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
                    .quantity(100)
                    .itemPrice(10000f)
                    .itemDescription("Dùng để làm sạch da mặt, loại bỏ bụi bẩn và dầu thừa.")
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
                    .quantity(100)
                    .itemPrice(250000f)
                    .itemDescription("Dùng để làm sạch da mặt, loại bỏ bụi bẩn và dầu thừa.")
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
            var image2 = DummyDataIImages.builder()
                    .itemId(1)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717404152/occqnbyew0zbbp4q0qof.jpg")
                    .build();
            itemImageService.CreateImage(image2);
            var image3 = DummyDataIImages.builder()
                    .itemId(1)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717404220/m1zuiipuz4jiiaa0isp8.jpg")
                    .build();
            itemImageService.CreateImage(image3);
            var image4 = DummyDataIImages.builder()
                    .itemId(2)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717404321/stmgig7r3r4lxfzqpii2.jpg")
                    .build();
            itemImageService.CreateImage(image4);
            var image5 = DummyDataIImages.builder()
                    .itemId(2)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717404368/is2upg0xehiuhtqflswa.jpg")
                    .build();
            itemImageService.CreateImage(image5);
            var image6 = DummyDataIImages.builder()
                    .itemId(2)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717404448/zk2lfxazyya5rxeffmic.jpg")
                    .build();
            itemImageService.CreateImage(image6);
            var image7 = DummyDataIImages.builder()
                    .itemId(3)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717404518/fdcngdp2pkebjsqj3cve.jpg")
                    .build();
            itemImageService.CreateImage(image7);
            var image8 = DummyDataIImages.builder()
                    .itemId(3)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717404519/e3k7cn3v2tnazds3iqm8.jpg")
                    .build();
            itemImageService.CreateImage(image8);

            var image9 = DummyDataIImages.builder()
                    .itemId(3)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717404730/gdlcwbp6sznt26f84hpg.jpg")
                    .build();
            itemImageService.CreateImage(image9);

            var image10 = DummyDataIImages.builder()
                    .itemId(16)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717405874/axg6kguzesa08uy8nno0.jpg")
                    .build();
            itemImageService.CreateImage(image10);
            var image11 = DummyDataIImages.builder()
                    .itemId(16)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717405875/ezagnxvayoxzqidzqcgl.jpg")
                    .build();
            itemImageService.CreateImage(image11);
            var image12 = DummyDataIImages.builder()
                    .itemId(16)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717405876/tc0ya3zt13ytgzg8iuc9.jpg")
                    .build();
            itemImageService.CreateImage(image12);

            var image13 = DummyDataIImages.builder()
                    .itemId(15)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717406038/ef2a7zbfvrycu7hvkdab.jpg")
                    .build();
            itemImageService.CreateImage(image13);
            var image14 = DummyDataIImages.builder()
                    .itemId(15)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717406039/owbchzs8fvctz0sfg86r.jpg")
                    .build();
            itemImageService.CreateImage(image14);
            var image15 = DummyDataIImages.builder()
                    .itemId(15)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717406040/seuob8pbnuh2zvqedim8.jpg")
                    .build();
            itemImageService.CreateImage(image15);

            var image16 = DummyDataIImages.builder()
                    .itemId(14)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717406182/cvnjvugpp8h7uwf1wutg.jpg")
                    .build();
            itemImageService.CreateImage(image16);
            var image17 = DummyDataIImages.builder()
                    .itemId(14)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717406183/t7zzoernv9rj10d95y3c.jpg")
                    .build();
            itemImageService.CreateImage(image17);
            var image18 = DummyDataIImages.builder()
                    .itemId(14)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717406185/tkl5uifbtxwgxiwwoh8v.jpg")
                    .build();
            itemImageService.CreateImage(image18);

            var image19 = DummyDataIImages.builder()
                    .itemId(13)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407449/myu5soyzf4dzkzhzobpm.jpg")
                    .build();
            itemImageService.CreateImage(image19);
            var image20 = DummyDataIImages.builder()
                    .itemId(13)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407450/vauykv2hwfhxwbzfqnt8.jpg")
                    .build();
            itemImageService.CreateImage(image20);
            var image21 = DummyDataIImages.builder()
                    .itemId(13)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407451/u1n7zcciljozymfinjgs.jpg")
                    .build();
            itemImageService.CreateImage(image21);

            var image22 = DummyDataIImages.builder()
                    .itemId(12)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407584/yt3hzmncu9vsxphbowus.jpg")
                    .build();
            itemImageService.CreateImage(image22);
            var image23 = DummyDataIImages.builder()
                    .itemId(12)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407583/rgwbol6p9hlmlyy9yipm.jpg")
                    .build();
            itemImageService.CreateImage(image23);
            var image24 = DummyDataIImages.builder()
                    .itemId(12)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407581/zbjbqrxgbcamfr7kkewb.jpg")
                    .build();
            itemImageService.CreateImage(image24);

            var image25 = DummyDataIImages.builder()
                    .itemId(11)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407641/rvjbd8mxvcoulnqgpura.jpg")
                    .build();
            itemImageService.CreateImage(image25);
            var image26 = DummyDataIImages.builder()
                    .itemId(11)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407643/u5ppburzgfjoujbrfqbe.jpg")
                    .build();
            itemImageService.CreateImage(image26);
            var image27 = DummyDataIImages.builder()
                    .itemId(11)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407645/hgavl24cypkurmx8yg0j.jpg")
                    .build();
            itemImageService.CreateImage(image27);

            var image28 = DummyDataIImages.builder()
                    .itemId(10)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407914/h0k0sqmzuqppu3n35hrc.jpg")
                    .build();
            itemImageService.CreateImage(image28);
            var image29 = DummyDataIImages.builder()
                    .itemId(10)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407916/d3a7q9psyt6xpyhzf10h.jpg")
                    .build();
            itemImageService.CreateImage(image29);
            var image30 = DummyDataIImages.builder()
                    .itemId(10)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717407918/ystsi2mpkx6g15kvaz39.webp")
                    .build();
            itemImageService.CreateImage(image30);

            var image31 = DummyDataIImages.builder()
                    .itemId(9)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408282/cayn8wyo8njiqmzkgoxd.jpg")
                    .build();
            itemImageService.CreateImage(image31);
            var image32 = DummyDataIImages.builder()
                    .itemId(9)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408286/os2wouvnawsunvxl73nx.jpg")
                    .build();
            itemImageService.CreateImage(image32);
            var image33 = DummyDataIImages.builder()
                    .itemId(9)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408289/szfpklbfaytwr31ymnbo.jpg")
                    .build();
            itemImageService.CreateImage(image33);

            var image34 = DummyDataIImages.builder()
                    .itemId(8)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408363/b9r1vyzallw5n8vhuvot.jpg")
                    .build();
            itemImageService.CreateImage(image34);
            var image35 = DummyDataIImages.builder()
                    .itemId(8)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408367/e3thzlxppovudvpbsv86.webp")
                    .build();
            itemImageService.CreateImage(image35);
            var image36 = DummyDataIImages.builder()
                    .itemId(8)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408370/ezeeydkwyraikjluqfq1.jpg")
                    .build();
            itemImageService.CreateImage(image36);
            var image37 = DummyDataIImages.builder()
                    .itemId(7)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408506/i6gs63l3vstdfyrfnw7o.jpg")
                    .build();
            itemImageService.CreateImage(image37);
            var image38 = DummyDataIImages.builder()
                    .itemId(7)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408509/nobkks3stb136ydtym1s.jpg")
                    .build();
            itemImageService.CreateImage(image38);
            var image39 = DummyDataIImages.builder()
                    .itemId(7)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408513/vz5zm3bedq6birscplaf.webp")
                    .build();
            itemImageService.CreateImage(image39);
            var image40 = DummyDataIImages.builder()
                    .itemId(6)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408604/biiypkrvaz0tt0mv0g4s.jpg")
                    .build();
            itemImageService.CreateImage(image40);
            var image41 = DummyDataIImages.builder()
                    .itemId(6)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408607/w3atftgeh1gxiotiqdia.jp")
                    .build();
            itemImageService.CreateImage(image41);
            var image42 = DummyDataIImages.builder()
                    .itemId(6)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408612/k8pfq970we0y8nsphdhe.jpg")
                    .build();
            itemImageService.CreateImage(image42);
            var image43 = DummyDataIImages.builder()
                    .itemId(5)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408680/mudj9a9wczteonfvdl82.png")
                    .build();
            itemImageService.CreateImage(image43);
            var image44 = DummyDataIImages.builder()
                    .itemId(5)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408683/v02cb5higxe7mkz36rim.jpg")
                    .build();
            itemImageService.CreateImage(image44);
            var image45 = DummyDataIImages.builder()
                    .itemId(5)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408685/ajpkkwrjylpgdixmxrch.jpg")
                    .build();
            itemImageService.CreateImage(image45);
            var image46 = DummyDataIImages.builder()
                    .itemId(4)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408766/xtruyz145i0dprcqeux2.jpg")
                    .build();
            itemImageService.CreateImage(image46);
            var image47 = DummyDataIImages.builder()
                    .itemId(4)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408769/xixzy0md10mwrnwtdnin.jpg")
                    .build();
            itemImageService.CreateImage(image47);
            var image48 = DummyDataIImages.builder()
                    .itemId(4)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717408771/r3auog8egqaihujnl7r4.jpg")
                    .build();
            itemImageService.CreateImage(image48);


            // dummy data Seoul Center

            var shop2_Item_service = ItemRequest.builder()
                    .itemName("Vẽ gel nổi Loang")
                    .quantity(100)
                    .itemPrice(50000f)
                    .itemDescription("Vẽ gel nổi Loang là một kỹ thuật tạo ra hiệu ứng gradient màu độc đáo trên móng tay")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(4)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_service);
            var shop2_Item_service1 = ItemRequest.builder()
                    .itemName("Úp Móng")
                    .quantity(100)
                    .itemPrice(120000f)
                    .itemDescription("Kết quả là một bộ móng độc đáo, với phần đầu móng và thân móng mang hai màu sắc khác nhau. Hiệu ứng này tạo nên sự ấn tượng và thu hút.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(4)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_service1);
            var shop2_Item_service2 = ItemRequest.builder()
                    .itemName("Tháo móng")
                    .quantity(100)
                    .itemPrice(40000f)
                    .itemDescription("Làm sạch móng và dưỡng ẩm móng")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(4)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_service2);

            var shop2_Item_service3 = ItemRequest.builder()
                    .itemName("Tráng gương")
                    .quantity(100)
                    .itemPrice(20000f)
                    .itemDescription("Khi hoàn thành, bề mặt móng sẽ tạo ra một lớp bóng loáng, phản chiếu ánh sáng như một tấm gương thật. Kỹ thuật này đòi hỏi cẩn thận trong thao tác để đạt được hiệu quả tối ưu.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(4)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_service3);

            var image49 = DummyDataIImages.builder()
                    .itemId(17)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717490721/o3cke0w4clnaib4synzc.jpg")
                    .build();
            itemImageService.CreateImage(image49);
            var image50 = DummyDataIImages.builder()
                    .itemId(17)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717490723/hrasmdjlq4fffgbcb2xk.jpg")
                    .build();
            itemImageService.CreateImage(image50);
            var image51 = DummyDataIImages.builder()
                    .itemId(17)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717490725/u6f4cangvt1heqqeeyav.jpg")
                    .build();
            itemImageService.CreateImage(image51);

            var image52 = DummyDataIImages.builder()
                    .itemId(18)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717490842/zqtcqr5vpwxzevtowur6.jpg")
                    .build();
            itemImageService.CreateImage(image52);
            var image53 = DummyDataIImages.builder()
                    .itemId(18)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717490843/xxsddhvsxg2toofuhju2.jpg")
                    .build();
            itemImageService.CreateImage(image53);
            var image54 = DummyDataIImages.builder()
                    .itemId(18)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717490845/wqvihojwmcahvfze7hjj.jpg")
                    .build();
            itemImageService.CreateImage(image54);

            var image55 = DummyDataIImages.builder()
                    .itemId(19)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717491025/qb36ifpjkwockxdvwtex.webp")
                    .build();
            itemImageService.CreateImage(image55);
            var image56 = DummyDataIImages.builder()
                    .itemId(19)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717491026/pg1byau2wxi1sz2dmu55.jpg")
                    .build();
            itemImageService.CreateImage(image56);
            var image57 = DummyDataIImages.builder()
                    .itemId(19)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717491028/znmje8s0swsyiwesbisi.jpg")
                    .build();
            itemImageService.CreateImage(image57);

            var image58 = DummyDataIImages.builder()
                    .itemId(20)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717491255/a57civkwk4chckjttr0a.jpg")
                    .build();
            itemImageService.CreateImage(image58);
            var image59 = DummyDataIImages.builder()
                    .itemId(20)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717491257/hz1qmqs01mfpewfbvccl.jpg")
                    .build();
            itemImageService.CreateImage(image59);
            var image60 = DummyDataIImages.builder()
                    .itemId(20)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717491259/k2y2oevdpg0je66zekc7.jpg")
                    .build();
            itemImageService.CreateImage(image60);


            var shop2_Item_product = ItemRequest.builder()
                    .itemName("Cetaphil Gentle Skin Cleansee")
                    .quantity(100)
                    .itemPrice(260000f)
                    .itemDescription("Sữa tắm dịu nhẹ, không gây kích ứng da, giúp làm sạch và giữ ẩm da.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(4)
                    .categoryId(2)
                    .itemTypeId(2)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_product);

            var shop2_Item_product1 = ItemRequest.builder()
                    .itemName("Dove Sensitive Skin Beauty Cream Bar")
                    .quantity(100)
                    .itemPrice(40000f)
                    .itemDescription("Thanh xà bông dưỡng ẩm, dịu nhẹ cho da nhạy cảm.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(4)
                    .categoryId(2)
                    .itemTypeId(2)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_product1);
            var shop2_Item_product2 = ItemRequest.builder()
                    .itemName("L'Occitane Shea Butter Extra-Gentle Vegetable-Based Soap")
                    .quantity(100)
                    .itemPrice(190000f)
                    .itemDescription("Thanh xà bông chứa 5% bơ hạt mỡ, làm sạch và dưỡng ẩm da.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(4)
                    .categoryId(2)
                    .itemTypeId(2)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_product2);

            var shop2_Item_product3 = ItemRequest.builder()
                    .itemName("The Body Shop Shea Body Butter")
                    .quantity(100)
                    .itemPrice(360000f)
                    .itemDescription("Kem dưỡng giàu bơ hạt mỡ, nuôi dưỡng và làm mềm da.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(5)
                    .categoryId(2)
                    .itemTypeId(2)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_product3);

            var shop2_Item_product4 = ItemRequest.builder()
                    .itemName("Aveeno Daily Moisturizing Lotion")
                    .quantity(100)
                    .itemPrice(280000f)
                    .itemDescription("Kem dưỡng chứa yến mạch tự nhiên, giúp cấp ẩm sâu cho da.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(5)
                    .categoryId(2)
                    .itemTypeId(2)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_product4);

            var shop2_Item_product5 = ItemRequest.builder()
                    .itemName("Nivea Soft Moisturizing Cream")
                    .quantity(100)
                    .itemPrice(260000f)
                    .itemDescription("Kem dưỡng ẩm, làm mềm mịn da toàn thân.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(5)
                    .categoryId(2)
                    .itemTypeId(2)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_product5);

            var shop2_Item_product6 = ItemRequest.builder()
                    .itemName("Scrub Love Sugar Scrub")
                    .quantity(100)
                    .itemPrice(280000f)
                    .itemDescription("Tẩy tế bào chết dạng hạt với đường và tinh dầu, làm sáng và mịn da.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(5)
                    .categoryId(2)
                    .itemTypeId(2)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_product6);
            var shop2_Item_product7 = ItemRequest.builder()
                    .itemName("Frank Body Original Coffee Scrub")
                    .quantity(100)
                    .itemPrice(390000f)
                    .itemDescription("Tẩy tế bào chết với cà phê, giúp cải thiện lưu thông máu và làm săn chắc da.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(5)
                    .categoryId(2)
                    .itemTypeId(2)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_product7);
            var shop2_Item_product8 = ItemRequest.builder()
                    .itemName("The Body Shop Spa of the World")
                    .quantity(100)
                    .itemPrice(510000f)
                    .itemDescription("Scrub chứa sữa dừa, làm sạch và dưỡng ẩm da sâu.")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(5)
                    .categoryId(2)
                    .itemTypeId(2)
                    .discount(10)
                    .build();
            itemService.CreateItem(shop2_Item_product8);

            var image61 = DummyDataIImages.builder()
                    .itemId(21)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493525/yg22vplpkxcqnplicklt.jpg")
                    .build();
            itemImageService.CreateImage(image61);
            var image62 = DummyDataIImages.builder()
                    .itemId(21)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493526/qudmw9vb2kjiuw9ktufe.jpg")
                    .build();
            itemImageService.CreateImage(image62);
            var image63 = DummyDataIImages.builder()
                    .itemId(21)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493527/rezwmai3042ndjt9kfxs.jpg")
                    .build();
            itemImageService.CreateImage(image63);
            var image64 = DummyDataIImages.builder()
                    .itemId(22)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493632/rqb7khkomqn1wxypuwxw.jpg")
                    .build();
            itemImageService.CreateImage(image64);
            var image65 = DummyDataIImages.builder()
                    .itemId(22)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493634/rvx7poukmawn4vn2hxuo.jpg")
                    .build();
            itemImageService.CreateImage(image65);
            var image66 = DummyDataIImages.builder()
                    .itemId(22)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493636/rzsfktkx2a5p8kbqf1jc.jpg")
                    .build();
            itemImageService.CreateImage(image66);
            var image67 = DummyDataIImages.builder()
                    .itemId(23)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493754/advnpumcnmx1ybbx5myp.jpg")
                    .build();
            itemImageService.CreateImage(image67);
            var image68 = DummyDataIImages.builder()
                    .itemId(23)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493755/bvse3ctnfxayqoyc4dbb.jpg")
                    .build();
            itemImageService.CreateImage(image68);
            var image69 = DummyDataIImages.builder()
                    .itemId(23)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493757/fuiskpx0zlaeppa0pre9.jpg")
                    .build();
            itemImageService.CreateImage(image69);
            var image70 = DummyDataIImages.builder()
                    .itemId(24)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493908/zkq5ipjh64j4hezwwtkv.jpg")
                    .build();
            itemImageService.CreateImage(image70);
            var image71 = DummyDataIImages.builder()
                    .itemId(24)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493909/f3vfafn5ikjw0gehdwoq.jpg")
                    .build();
            itemImageService.CreateImage(image71);
            var image72 = DummyDataIImages.builder()
                    .itemId(24)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493911/t633lmphayeu1za6knda.jpg")
                    .build();
            itemImageService.CreateImage(image72);
            var image73 = DummyDataIImages.builder()
                    .itemId(25)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493993/mn2eodzy8h8nibwidicm.jpg")
                    .build();
            itemImageService.CreateImage(image73);
            var image74 = DummyDataIImages.builder()
                    .itemId(25)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493995/q2bvhlt6fkrlddioz45v.jpg")
                    .build();
            itemImageService.CreateImage(image74);
            var image75 = DummyDataIImages.builder()
                    .itemId(25)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717493996/r5ijvoqnngiv1vsvfto4.jpg")
                    .build();
            itemImageService.CreateImage(image75);
            var image76 = DummyDataIImages.builder()
                    .itemId(26)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494083/lo9uzlif8zurjoxydmef.jpg")
                    .build();
            itemImageService.CreateImage(image76);
            var image77 = DummyDataIImages.builder()
                    .itemId(26)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494084/jedeai1t2xat9yw8dfht.jpg")
                    .build();
            itemImageService.CreateImage(image77);
            var image78 = DummyDataIImages.builder()
                    .itemId(26)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494086/xlplhy8k2yqzjeknsrus.jpg")
                    .build();
            itemImageService.CreateImage(image78);
            var image79 = DummyDataIImages.builder()
                    .itemId(27)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494230/cdishttakfjboppdh1g1.jpg")
                    .build();
            itemImageService.CreateImage(image79);
            var image80 = DummyDataIImages.builder()
                    .itemId(27)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494232/pixcdcyhidlghygjuwfj.jpg")
                    .build();
            itemImageService.CreateImage(image80);
            var image81 = DummyDataIImages.builder()
                    .itemId(27)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494233/j9q6hc1y4gxx3hbwz7xn.jpg")
                    .build();
            itemImageService.CreateImage(image81);
            var image82 = DummyDataIImages.builder()
                    .itemId(28)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494346/raqi0nvoaqhim0v3tckk.jpg")
                    .build();
            itemImageService.CreateImage(image82);
            var image83 = DummyDataIImages.builder()
                    .itemId(28)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494347/qdsazx9shwdizlvevx3f.jpg")
                    .build();
            itemImageService.CreateImage(image83);
            var image84 = DummyDataIImages.builder()
                    .itemId(28)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494349/eipxb3ahzdlh154ig2ff.jpg")
                    .build();
            itemImageService.CreateImage(image84);
            var image85 = DummyDataIImages.builder()
                    .itemId(29)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494439/l02qm0zhz8wcejt3zjfu.jpg")
                    .build();
            itemImageService.CreateImage(image85);
            var image86 = DummyDataIImages.builder()
                    .itemId(29)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494440/wqalrpj0n2rxnor9flb0.jpg")
                    .build();
            itemImageService.CreateImage(image86);
            var image87 = DummyDataIImages.builder()
                    .itemId(29)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1717494441/avd2ifjq528eattite10.jpg")
                    .build();
            itemImageService.CreateImage(image87);

            var location = LocationRequest.builder()
                    .latitude("10.8618393")
                    .longitude("106.7580455")
                    .shopID(3)
                    .build();
            locationService.createLocation(location);
            var location1 = LocationRequest.builder()
                    .latitude("10.8312034")
                    .longitude("106.7025974")
                    .shopID(4)
                    .build();
            locationService.createLocation(location1);
            var location2 = LocationRequest.builder()
                    .latitude("10.8595319")
                    .longitude("106.7648504")
                    .shopID(6)
                    .build();
            locationService.createLocation(location2);
            var location3 = LocationRequest.builder()
                    .latitude("10.8136653")
                    .longitude("106.6547022")
                    .shopID(7)
                    .build();
            locationService.createLocation(location3);
            var location4 = LocationRequest.builder()
                    .latitude("10.8143615")
                    .longitude("106.655274")
                    .shopID(8)
                    .build();
            locationService.createLocation(location4);
            var location5 = LocationRequest.builder()
                    .latitude("13.3096391")
                    .longitude("105.4040519")
                    .shopID(9)
                    .build();
            locationService.createLocation(location5);
            var location6 = LocationRequest.builder()
                    .latitude("13.4029018")
                    .longitude("105.439347")
                    .shopID(10)
                    .build();
            locationService.createLocation(location6);
            var locationReal = LocationRequest.builder()
                    .latitude("10.8410204")
                    .longitude("106.6416241")
                    .shopID(5)
                    .build();
            locationService.createLocation(locationReal);

            //timebooking

            var timeBooking = TimeBookingRequest.builder()
                    .time("7h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking);
            var timeBooking1 = TimeBookingRequest.builder()
                    .time("8h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking1);
            var timeBooking2 = TimeBookingRequest.builder()
                    .time("9h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking2);
            var timeBooking3 = TimeBookingRequest.builder()
                    .time("10h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking3);
            var timeBooking4 = TimeBookingRequest.builder()
                    .time("11h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking4);
            var timeBooking5 = TimeBookingRequest.builder()
                    .time("12h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking5);
            var timeBooking6 = TimeBookingRequest.builder()
                    .time("13h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking6);
            var timeBooking7 = TimeBookingRequest.builder()
                    .time("14h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking7);
            var timeBooking8 = TimeBookingRequest.builder()
                    .time("15h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking8);
            var timeBooking9 = TimeBookingRequest.builder()
                    .time("16h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking9);
            var timeBooking10 = TimeBookingRequest.builder()
                    .time("17h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking10);
            var timeBooking11 = TimeBookingRequest.builder()
                    .time("18h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking11);
            var timeBooking12 = TimeBookingRequest.builder()
                    .time("19h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking12);
            var timeBooking13 = TimeBookingRequest.builder()
                    .time("20h00")
                    .build();
            timeBookingService.createBookingTime(timeBooking13);

            List<CreateOrderRequest.OrderItemRequest> orderItems = List.of(
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(15)
                            .quantity(1)
                            .build(),
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(26)
                            .quantity(1)
                            .build()
            );
            var order = CreateOrderRequest.builder()
                    .total(243000)
                    .customerId(23)
                    .customerName("Huy Phạm")
                    .customerAddress("Bình Định")
                    .customerPhone("0392272536")
                    .items(orderItems)
                    .build();
            orderService.createOrderWithDetails(order);

            List<CreateOrderRequest.OrderItemRequest> orderItems1 = List.of(
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(28)
                            .quantity(2)
                            .build(),
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(8)
                            .quantity(3)
                            .build()
            );
            var order1 = CreateOrderRequest.builder()
                    .total(972000)
                    .customerId(24)
                    .customerName("Huyền Trân")
                    .customerAddress("HCM")
                    .customerPhone("0325487965")
                    .items(orderItems1)
                    .build();
            orderService.createOrderWithDetails(order1);

            List<CreateOrderRequest.OrderItemRequest> orderItems2 = List.of(
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(25)
                            .quantity(2)
                            .build(),
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(26)
                            .quantity(5)
                            .build(),
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(27)
                            .quantity(1)
                            .build()
            );
            var order2 = CreateOrderRequest.builder()
                    .total(1926000)
                    .customerId(25)
                    .customerName("Phước Hữu")
                    .customerAddress("Phú Yên")
                    .customerPhone("0958745689")
                    .items(orderItems2)
                    .build();
            orderService.createOrderWithDetails(order2);

            List<CreateOrderRequest.OrderItemRequest> orderItems3 = List.of(
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(15)
                            .quantity(1)
                            .build(),
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(26)
                            .quantity(1)
                            .build()
            );
            var order3 = CreateOrderRequest.builder()
                    .total(243000)
                    .customerId(23)
                    .customerName("Tuyết Nhi")
                    .customerAddress("Bình Duong")
                    .customerPhone("0392295836")
                    .items(orderItems3)
                    .build();
            orderService.createOrderWithDetails(order3);

            List<CreateOrderRequest.OrderItemRequest> orderItems4 = List.of(
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(28)
                            .quantity(2)
                            .build(),
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(8)
                            .quantity(3)
                            .build()
            );
            var order4 = CreateOrderRequest.builder()
                    .total(972000)
                    .customerId(24)
                    .customerName("Gia Hân")
                    .customerAddress("HCM")
                    .customerPhone("0325985965")
                    .items(orderItems4)
                    .build();
            orderService.createOrderWithDetails(order4);

            List<CreateOrderRequest.OrderItemRequest> orderItems5 = List.of(
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(25)
                            .quantity(2)
                            .build(),
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(26)
                            .quantity(5)
                            .build(),
                    CreateOrderRequest.OrderItemRequest.builder()
                            .itemId(27)
                            .quantity(1)
                            .build()
            );
            var order5 = CreateOrderRequest.builder()
                    .total(1926000)
                    .customerId(25)
                    .customerName("Minh Tri")
                    .customerAddress("HCM")
                    .customerPhone("0958986689")
                    .items(orderItems5)
                    .build();
            orderService.createOrderWithDetails(order5);

            orderService.changeStatus(4, OrderStatus.DELIVERED);
            orderService.changeStatus(5, OrderStatus.DELIVERED);
            orderService.changeStatus(6, OrderStatus.DELIVERED);
//			feeService.createFee(3);
            
            // Dummy data for booking
            User user24 = userRepo.findById(24).orElse(null);
            Item item04 = itemRepo.findById(4).orElse(null);
            TimeBooking timeBooking01 = timeBookingRepo.findById(1).orElse(null);
            User user25 = userRepo.findById(25).orElse(null);
            Item item05 = itemRepo.findById(5).orElse(null);
            TimeBooking timeBooking06 = timeBookingRepo.findById(6).orElse(null);
            User user26 = userRepo.findById(26).orElse(null);
            Item item06 = itemRepo.findById(6).orElse(null);
            TimeBooking timeBooking08 = timeBookingRepo.findById(8).orElse(null);
            User user27 = userRepo.findById(27).orElse(null);
            Item item07 = itemRepo.findById(7).orElse(null);
            TimeBooking timeBooking014 = timeBookingRepo.findById(14).orElse(null);
            Booking booking1 = Booking.builder()
                    .customer(user24)
                    .service(item04)
                    .timeBooking(timeBooking01)
                    .bookingDate(new Date(1737736800000L))
                    .status(BookingStatus.PENDING)
                    .build();
            Booking booking2 = Booking.builder()
                    .customer(user25)
                    .service(item05)
                    .timeBooking(timeBooking06)
                    .bookingDate(new Date())
                    .status(BookingStatus.CONFIRMED)
                    .build();
            Booking booking3 = Booking.builder()
                    .customer(user26)
                    .service(item06)
                    .timeBooking(timeBooking08)
                    .bookingDate(new Date())
                    .status(BookingStatus.CANCELLED)
                    .build();
            Booking booking4 = Booking.builder()
                    .customer(user27)
                    .service(item07)
                    .timeBooking(timeBooking014)
                    .bookingDate(new Date())
                    .status(BookingStatus.COMPLETED)
                    .build();
            bookingRepo.save(booking1);
            bookingRepo.save(booking2);
            bookingRepo.save(booking3);
            bookingRepo.save(booking4);



            // dummy data real shop
            var realProduct1 = ItemRequest.builder()
                    .itemName("Cắt da tay")
                    .quantity(0)
                    .itemPrice(30000f)
                    .itemDescription("Cắt da tay - 30k")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(5)
                    .categoryId(1)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct1);

            var realImage = DummyDataIImages.builder()
                    .itemId(30)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1719569625/bcug5lqaapdimp1fpo4m.jpg")
                    .build();
            itemImageService.CreateImage(realImage);
            var realImage1 = DummyDataIImages.builder()
                    .itemId(30)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1719569628/z43pfkwcilgwtt2ybrbn.jpg")
                    .build();
            itemImageService.CreateImage(realImage1);
            var realImage2 = DummyDataIImages.builder()
                    .itemId(30)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1719569629/rrkrqdmo4cpxxzv1ddtp.jpg")
                    .build();
            itemImageService.CreateImage(realImage2);

            var realProduct2 = ItemRequest.builder()
                    .itemName("Phá gel tay - chân")
                    .quantity(100)
                    .itemPrice(30000f)
                    .itemDescription("Phá gel tay - chân - 30k")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(5)
                    .categoryId(1)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct2);
            var realImage3 = DummyDataIImages.builder()
                    .itemId(31)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1719569727/mu5fzobnes2o607udksn.jpg")
                    .build();
            itemImageService.CreateImage(realImage3);
            var realImage4 = DummyDataIImages.builder()
                    .itemId(31)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1719569729/wmgottsb7tczbd3qtj87.webp")
                    .build();
            itemImageService.CreateImage(realImage4);
            var realImage5 = DummyDataIImages.builder()
                    .itemId(31)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1719569731/us33u4fk0cnmwcvzk8mg.jpg")
                    .build();
            itemImageService.CreateImage(realImage5);
            var realProduct3 = ItemRequest.builder()
                    .itemName("Tháo gel cứng - Móng giả")
                    .quantity(0)
                    .itemPrice(65000f)
                    .itemDescription("Tháo gel cứng - Móng giả - 65k")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(5)
                    .categoryId(1)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct3);
            var realImage6 = DummyDataIImages.builder()
                    .itemId(32)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1719569817/wm1efuouf35u0qmuxdwb.jpg")
                    .build();
            itemImageService.CreateImage(realImage6);
            var realImage7 = DummyDataIImages.builder()
                    .itemId(32)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1719569818/vlkdjiadieb4to8wglgq.jpg")
                    .build();
            itemImageService.CreateImage(realImage7);
            var realImage8 = DummyDataIImages.builder()
                    .itemId(32)
                    .link("http://res.cloudinary.com/dpxs39hkb/image/upload/v1719569820/nfsqyj7y8tb072pulxi4.jpg")
                    .build();
            itemImageService.CreateImage(realImage8);



            var realProduct4 = ItemRequest.builder()
                    .itemName("Sơn gel")
                    .quantity(0)
                    .itemPrice(79000f)
                    .itemDescription("Sơn gel - 79k")
//					.commentCount(0)
//					.rate(0f)
//					.countUsage(0)
                    .shopId(5)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct4);
            var realImage9 = DummyDataIImages.builder()
                    .itemId(33)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571295/p1g8lxj1caiflx1gism7.webp")
                    .build();
            itemImageService.CreateImage(realImage9);
            var realImage10 = DummyDataIImages.builder()
                    .itemId(33)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571297/czjvelbyhslmck5yvziw.webp")
                    .build();
            itemImageService.CreateImage(realImage10);
            var realImage11 = DummyDataIImages.builder()
                    .itemId(33)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571300/st6btx35khxp18kxao6s.png")
                    .build();
            itemImageService.CreateImage(realImage11);

// Product 5
            var realProduct5 = ItemRequest.builder()
                    .itemName("Sơn mắt mèo")
                    .quantity(0)
                    .itemPrice(80000f)
                    .itemDescription("Sơn mắt mèo - 80k")
                    .shopId(5)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct5);

            var realImage12 = DummyDataIImages.builder()
                    .itemId(34)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571377/caga6chpb5idslcvddbu.jpg")
                    .build();
            itemImageService.CreateImage(realImage12);

            var realImage13 = DummyDataIImages.builder()
                    .itemId(34)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571378/zwfglbabclt9s0sg1pyn.jpg")
                    .build();
            itemImageService.CreateImage(realImage13);

            var realImage14 = DummyDataIImages.builder()
                    .itemId(34)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571380/ystosk0jkl7kmwqty7yx.jpg")
                    .build();
            itemImageService.CreateImage(realImage14);

// Product 6
            var realProduct6 = ItemRequest.builder()
                    .itemName("Sơn nhũ")
                    .quantity(0)
                    .itemPrice(80000f)
                    .itemDescription("Sơn nhũ - 80k")
                    .shopId(5)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct6);

            var realImage15 = DummyDataIImages.builder()
                    .itemId(35)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571464/onqzaebhctooc7cxngxq.jpg")
                    .build();
            itemImageService.CreateImage(realImage15);

            var realImage16 = DummyDataIImages.builder()
                    .itemId(35)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571466/yr62buz7ejyxmbkpzq3j.webp")
                    .build();
            itemImageService.CreateImage(realImage16);

            var realImage17 = DummyDataIImages.builder()
                    .itemId(35)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571469/azr2ycrefrkwqmx5iayr.jpg")
                    .build();
            itemImageService.CreateImage(realImage17);

// Product 7
            var realProduct7 = ItemRequest.builder()
                    .itemName("Sơn thường")
                    .quantity(0)
                    .itemPrice(40000f)
                    .itemDescription("Sơn thường 40k")
                    .shopId(5)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct7);

            var realImage18 = DummyDataIImages.builder()
                    .itemId(36)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571649/slt1kofbj80zsafupoqf.jpg")
                    .build();
            itemImageService.CreateImage(realImage18);

            var realImage19 = DummyDataIImages.builder()
                    .itemId(36)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571650/p1pnt6n5rezoqtmtasin.jpg")
                    .build();
            itemImageService.CreateImage(realImage19);

            var realImage20 = DummyDataIImages.builder()
                    .itemId(36)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571652/gji7qnc33286oftk1cog.webp")
                    .build();
            itemImageService.CreateImage(realImage20);

// Product 8
            var realProduct8 = ItemRequest.builder()
                    .itemName("Đá đính nails")
                    .quantity(0)
                    .itemPrice(15000f)
                    .itemDescription("Đá đính nails 5-15k 1 ngón")
                    .shopId(5)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct8);

            var realImage21 = DummyDataIImages.builder()
                    .itemId(37)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571812/q77qfidczeuilovoccj5.jpg")
                    .build();
            itemImageService.CreateImage(realImage21);

            var realImage22 = DummyDataIImages.builder()
                    .itemId(37)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571814/ugxc904ezlty5xs9wupm.jpg")
                    .build();
            itemImageService.CreateImage(realImage22);

            var realImage23 = DummyDataIImages.builder()
                    .itemId(37)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571816/j9hamopmlqj1242amlom.jpg")
                    .build();
            itemImageService.CreateImage(realImage23);

// Product 9
            var realProduct9 = ItemRequest.builder()
                    .itemName("Loang cẩm thạch 2 màu")
                    .quantity(0)
                    .itemPrice(12000f)
                    .itemDescription("Loang cẩm thạch 2 màu - 12k")
                    .shopId(5)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct9);

            var realImage24 = DummyDataIImages.builder()
                    .itemId(38)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571938/zgzieoffwj303hy1cyob.jpg")
                    .build();
            itemImageService.CreateImage(realImage24);

            var realImage25 = DummyDataIImages.builder()
                    .itemId(38)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571940/jxkuk59hqcmn9xyvhb3o.jpg")
                    .build();
            itemImageService.CreateImage(realImage25);

            var realImage26 = DummyDataIImages.builder()
                    .itemId(38)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719571942/cnqc8yq95kkffxvrv692.jpg")
                    .build();
            itemImageService.CreateImage(realImage26);

// Product 10
            var realProduct10 = ItemRequest.builder()
                    .itemName("Ombre móng")
                    .quantity(0)
                    .itemPrice(180000f)
                    .itemDescription("Ombre móng 180k")
                    .shopId(5)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct10);

            var realImage27 = DummyDataIImages.builder()
                    .itemId(39)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572017/h1p4otxcriok7atgzwgh.jpg")
                    .build();
            itemImageService.CreateImage(realImage27);

            var realImage28 = DummyDataIImages.builder()
                    .itemId(39)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572018/an73hgrtvoyh8vrztkqe.jpg")
                    .build();
            itemImageService.CreateImage(realImage28);

            var realImage29 = DummyDataIImages.builder()
                    .itemId(39)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572020/ebi2ngjezhtpxisqdxpk.jpg")
                    .build();
            itemImageService.CreateImage(realImage29);

// Product 11
            var realProduct11 = ItemRequest.builder()
                    .itemName("Tráng gương")
                    .quantity(0)
                    .itemPrice(200000f)
                    .itemDescription("Tráng gương 200k")
                    .shopId(5)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct11);

            var realImage30 = DummyDataIImages.builder()
                    .itemId(40)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572074/sejl3ltzokbbaaymslrn.jpg")
                    .build();
            itemImageService.CreateImage(realImage30);

            var realImage31 = DummyDataIImages.builder()
                    .itemId(40)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572075/itiycxxi1xsh5vpfxw1f.jpg")
                    .build();
            itemImageService.CreateImage(realImage31);

            var realImage32 = DummyDataIImages.builder()
                    .itemId(40)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572077/mivmiwqozqtq3ja29mwu.jpg")
                    .build();
            itemImageService.CreateImage(realImage32);

// Product 12
            var realProduct12 = ItemRequest.builder()
                    .itemName("Up móng giả")
                    .quantity(0)
                    .itemPrice(70000f)
                    .itemDescription("up móng giả - 70k")
                    .shopId(5)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct12);

            var realImage33 = DummyDataIImages.builder()
                    .itemId(41)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572156/e3hgyjglex0ev5nscpfa.jpg")
                    .build();
            itemImageService.CreateImage(realImage33);

            var realImage34 = DummyDataIImages.builder()
                    .itemId(41)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572158/ppuljqbx6wilpotxpelj.jpg")
                    .build();
            itemImageService.CreateImage(realImage34);

            var realImage35 = DummyDataIImages.builder()
                    .itemId(41)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572160/wmcrsdododlvfniwz1kf.jpg")
                    .build();
            itemImageService.CreateImage(realImage35);

// Product 13
            var realProduct13 = ItemRequest.builder()
                    .itemName("Vân đá")
                    .quantity(0)
                    .itemPrice(65000f)
                    .itemDescription("Vân đá 180k")
                    .shopId(5)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct13);

            var realImage36 = DummyDataIImages.builder()
                    .itemId(42)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572232/hmu4zsyavmorm2p7thzf.jpg")
                    .build();
            itemImageService.CreateImage(realImage36);

            var realImage37 = DummyDataIImages.builder()
                    .itemId(42)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572234/nicq8t4jqvxfk3hwnqvr.jpg")
                    .build();
            itemImageService.CreateImage(realImage37);

            var realImage38 = DummyDataIImages.builder()
                    .itemId(42)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572236/fi8cn2zzb16bqu9xdeww.jpg")
                    .build();
            itemImageService.CreateImage(realImage38);

// Product 14
            var realProduct14 = ItemRequest.builder()
                    .itemName("Vẽ móng")
                    .quantity(0)
                    .itemPrice(15000f)
                    .itemDescription("vẽ móng 5-15k 1 ngón")
                    .shopId(5)
                    .categoryId(3)
                    .itemTypeId(1)
                    .discount(0)
                    .build();
            itemService.CreateItem(realProduct14);

            var realImage39 = DummyDataIImages.builder()
                    .itemId(43)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572307/y0ixtgj4g5you9rmndry.jpg")
                    .build();
            itemImageService.CreateImage(realImage39);

            var realImage40 = DummyDataIImages.builder()
                    .itemId(43)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572309/xxrawkcqeptfqutglahq.jpg")
                    .build();
            itemImageService.CreateImage(realImage40);

            var realImage41 = DummyDataIImages.builder()
                    .itemId(43)
                    .link("https://res.cloudinary.com/dpxs39hkb/image/upload/v1719572311/j079tsey39sh6fusqnc3.jpg")
                    .build();
            itemImageService.CreateImage(realImage41);

        };
    }
}
