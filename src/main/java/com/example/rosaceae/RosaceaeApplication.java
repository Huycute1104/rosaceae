package com.example.rosaceae;

import com.example.rosaceae.Format.MailFormat;
import com.example.rosaceae.auth.AuthenticationService;
import com.example.rosaceae.auth.RegisterRequest;
import com.example.rosaceae.dto.Request.RankMemberRequest.CreateRankRequet;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.EmailService;
import com.example.rosaceae.service.RankMemberService;
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
//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService service,
//			RankMemberService memberService
//	) {
//		return args -> {
//			var rank = CreateRankRequet.builder()
//					.rankName("Bronze")
//					.build();
//			var bronze = memberService.createRank(rank);
//
//			var rank2 = CreateRankRequet.builder()
//					.rankName("Silver")
//					.build();
//			memberService.createRank(rank2);
//			var rank3 = CreateRankRequet.builder()
//					.rankName("Gold")
//					.build();
//			memberService.createRank(rank3);
//			//Supper admin
//			var super_admin = RegisterRequest.builder()
//					.name("SuperAdmin")
//					.email("SuperAdmin@gmail.com")
//					.password("123")
//					.phone("0392272536")
//					.status(true)
//					.role(SUPPER_ADMIN)
//					.build();
//			System.out.println("Super Admin token :" + service.register(super_admin).getAccessToken());
//
//			//admin
//			var admin = RegisterRequest.builder()
//					.name("Admin")
//					.email("admin@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0854512367")
//					.role(ADMIN)
//					.build();
//			System.out.println("Admin token :" + service.register(admin).getAccessToken());
//
//			//shop
//			var shop = RegisterRequest.builder()
//					.name("Rosaceae Shop")
//					.email("rosaceae001@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0854512367")
//					.role(SHOP)
//					.build();
//			System.out.println("Shop token :" + service.register(shop).getAccessToken());
//
//			//Customer
//			var customer = RegisterRequest.builder()
//					.name("Trần Huy")
//					.email("huypt110402@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0854512367")
//					.role(CUSTOMER)
//					.rankId(1)
//					.build();
//			System.out.println("Customer token :" + service.register(customer).getAccessToken());
//
//			var customer2 = RegisterRequest.builder()
//					.name("Huyền Trân")
//					.email("tran123@gmail.com")
//					.status(true)
//					.password("123")
//					.phone("0854512367")
//					.role(CUSTOMER)
//					.rankId(1)
//					.build();
//			service.register(customer2);
//			// Rank member
//
//
//
//
//		};
//	}

    @GetMapping("")
    public String greeting() {
        try {
            String result = "";
            String[] arr = MailFormat.MailHtml.split("@@@@@@@@######");
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
