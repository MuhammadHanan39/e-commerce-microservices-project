package com.ecommerce.product;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@OpenAPIDefinition(
        info = @Info(
                title = "Product Service API",
                version = "1.0",
                description = "Product Catalog Management Service",
                contact = @Contact(
                        name = "Product Team",
                        email = "product-team@ecommerce.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
        System.out.println("=========================================");
        System.out.println("Product Service Started Successfully!");
        System.out.println("Port: 8081");
        System.out.println("Swagger UI: http://localhost:8081/swagger-ui.html");
        System.out.println("=========================================");
    }
}
