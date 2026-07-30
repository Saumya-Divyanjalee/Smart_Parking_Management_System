package lk.ijse.ad.vehicleservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI vehicleServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vehicle Service API")
                        .version("1.0")
                        .description("Manages vehicle registration and entry/exit tracking"));
    }
}