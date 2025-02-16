package weolbu.assignment.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI createOpenApi() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", createApiKey()))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"));
    }

    private SecurityScheme createApiKey() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}
