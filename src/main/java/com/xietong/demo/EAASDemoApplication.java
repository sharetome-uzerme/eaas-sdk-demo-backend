package com.xietong.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * 程序启动类。
 *
 */
@SpringBootApplication
@ComponentScan(value = {
		"com.xietong.demo",
})
public class EAASDemoApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EAASDemoApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(EAASDemoApplication.class, args);
	}
}
