package com.barabanov.mandatory.model.dbms;

import com.barabanov.mandatory.model.dbms.repository.DbManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class MandatoryModelInDbmsApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MandatoryModelInDbmsApplication.class, args);

		DbManager dbManager = context.getBean(DbManager.class);

		System.out.println();
	}

}
