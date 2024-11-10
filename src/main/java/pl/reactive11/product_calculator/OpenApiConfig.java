package pl.reactive11.product_calculator;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:4000");
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("slav.dabrowski@gmail.com");
        contact.setName("Sławomir Dąbrowski");


        Info info = new Info()
                .title("API for calculate product's price and amount")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for calculate product's price and amount. Additionally, another endpoints are defined for management and configuration.");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
