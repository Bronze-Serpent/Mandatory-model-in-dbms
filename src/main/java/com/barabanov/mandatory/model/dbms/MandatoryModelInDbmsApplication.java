package com.barabanov.mandatory.model.dbms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;


@SpringBootApplication
public class MandatoryModelInDbmsApplication
{

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(MandatoryModelInDbmsApplication.class, args);

//		DbService dbService = context.getBean(DbService.class);
//		String dataFromDb = dbService.getDataFromDbAsJson(1L, """
//				SELECT * FROM model;
//				""");
//
//		System.out.println(dataFromDb);

		/*dynamicDbManager.createDb("car_db");
		dynamicDbManager.createTable(
				"car_db",
				"model",
				List.of(
						new ColumnDesc("name", "VARCHAR(45)", Collections.emptyList(), null),
						new ColumnDesc("id", "BIGSERIAL", List.of("PRIMARY KEY"), null)
				)
		);
		dynamicDbManager.createTable(
				"car_db",
				"price",
				Collections.emptyList()
		);
		Long insertedId = dynamicDbManager.insertTuple("car_db", """
				INSERT INTO model (name)
				            VALUES ('oh_yes_my_model_2');
				""");
		System.out.println(insertedId);*/

		/*DbSecurityRepository dbSecurityRepository = context.getBean(DbSecurityRepository.class);
		TableSecurityRepository tableSecurityRepository = context.getBean(TableSecurityRepository.class);

		DatabaseSecurity car_db = DatabaseSecurity.builder()
				.name("car_db")
				.securityLevel(SecurityLevel.TOP_SECRET)
				.build();
		dbSecurityRepository.save(car_db);

		DatabaseSecurity db = dbSecurityRepository.findById(1L).get();

		TableSecurity table1 = TableSecurity.builder()
				.name("model")
				.securityLevel(SecurityLevel.TOP_SECRET)
				.databaseSecurity(db)
				.build();
		tableSecurityRepository.save(table1);


		TableSecurity table2 = TableSecurity.builder()
				.name("price")
				.securityLevel(SecurityLevel.OF_PARTICULAR_IMPORTANCE)
				.databaseSecurity(db)
				.build();
		tableSecurityRepository.save(table2);*/

//		TableSecurityRepository tableSecurityRepository = context.getBean(TableSecurityRepository.class);
//		Optional<TableSecurity> model = tableSecurityRepository.findByNameInDb(1L, "model");
//		System.out.println(model.get());
	}

}
