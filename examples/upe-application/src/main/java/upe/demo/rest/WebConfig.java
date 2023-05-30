package upe.demo.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200",
                        "https://v2202012136335136534.happysrv.de",
                        "https://upe.spraener.de",
                        "https://test.spraener.de"
                )
                .allowedMethods("GET", "PUT", "POST", "OPTIONS", "DELETE");
    }
}
