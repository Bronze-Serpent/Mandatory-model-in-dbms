package com.barabanov.mandatory.model.dbms;

import com.barabanov.mandatory.model.dbms.repository.DbManager;
import jakarta.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;


@SpringBootApplication
public class MandatoryModelInDbmsApplication
{

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MandatoryModelInDbmsApplication.class, args);

		DbManager dbManager = context.getBean(DbManager.class);
		EntityManager entityManager = context.getBean(EntityManager.class);

//		String dbName = "db_to_creation_template_test";
//		String tableName = "template_test";
//		dbManager.createDb(dbName);
//		dbManager.createTable(dbName, tableName, Collections.emptyList());
//
//		Boolean wasTableFound = (Boolean) entityManager.createNativeQuery(
//				"SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + tableName + "');",
//				Boolean.class).getSingleResult();

	}

}
