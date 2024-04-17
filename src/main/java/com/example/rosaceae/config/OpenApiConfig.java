package com.example.rosaceae.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Service;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Rosaceae",
                        email = "Huypt110402@gmail.com",
                        url = "https://www.facebook.com/hailua.tamquan"
                ),
                description = "OpenAPI document for Rosaceae",
                title = "OpenAPI of Rosaceae",
                version ="1.0"
        ),
        servers = {
                @Server(
                        description = "Local Rosaceae",
                        url = "http://localhost:8080"
                ),
//                @Server(
//                        description = "Local Rosaceae",
//                        url = "http://localhost:8080"
//                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
