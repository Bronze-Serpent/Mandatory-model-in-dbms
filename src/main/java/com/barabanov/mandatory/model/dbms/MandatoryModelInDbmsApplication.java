package com.barabanov.mandatory.model.dbms;

import com.barabanov.mandatory.model.dbms.database.DynamicDbManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class MandatoryModelInDbmsApplication
{

	public static void main(String[] args)
	{
		ConfigurableApplicationContext context = SpringApplication.run(MandatoryModelInDbmsApplication.class, args);

		DynamicDbManager dbManager = context.getBean(DynamicDbManager.class);
//		dbManager.createDb("test_4");
//		dbManager.executeSqlInDb("test_4", "SELECT col FROM table");
	}

}
