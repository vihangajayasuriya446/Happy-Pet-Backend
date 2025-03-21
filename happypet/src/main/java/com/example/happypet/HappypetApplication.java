package com.example.happypet;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@EnableTransactionManagement
public class HappypetApplication {

	@Value("${file.upload-dir:src/main/resources/static/images}")
	private String uploadDir;

	public static void main(String[] args) {
		SpringApplication.run(HappypetApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
						.allowedOrigins("http://localhost:5173")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedHeaders("*")
						.allowCredentials(true);
			}

			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
				registry.addResourceHandler("/images/**")
						.addResourceLocations("file:" + uploadPath.toString() + "/");
				System.out.println("Configuring resource handler to serve images from: file:" + uploadPath.toString() + "/");
			}
		};
	}
}
