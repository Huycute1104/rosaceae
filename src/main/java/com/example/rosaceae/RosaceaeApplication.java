package com.example.rosaceae;

import com.example.rosaceae.auth.AuthenticationService;
import com.example.rosaceae.auth.RegisterRequest;
import com.example.rosaceae.dto.CreateRankRequet;
import com.example.rosaceae.enums.Role;
import com.example.rosaceae.service.RankMemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.example.rosaceae.enums.Role.*;

@SpringBootApplication
public class RosaceaeApplication {

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
			RankMemberService memberService
	) {
		return args -> {
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
			// Rank member




		};
	}

}
