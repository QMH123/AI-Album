package com.example.aiphoto_album;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.example.aiphoto_album"})
@MapperScan("com.example.aiphoto_album.dao")
@EnableSwagger2
public class AiphotoAlbumApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiphotoAlbumApplication.class, args);
	}

}
