package com.barabanov.mandatory.model.dbms;

import com.barabanov.mandatory.model.dbms.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.entity.TableSecurity;
import com.barabanov.mandatory.model.dbms.repository.ColumnSecurityRepository;
import com.barabanov.mandatory.model.dbms.repository.DbSecurityRepository;
import com.barabanov.mandatory.model.dbms.repository.TableSecurityRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class MandatoryModelInDbmsApplication
{

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MandatoryModelInDbmsApplication.class, args);


		DbSecurityRepository dbSecurityRepository = context.getBean(DbSecurityRepository.class);
		TableSecurityRepository tableSecurityRepository = context.getBean(TableSecurityRepository.class);
		ColumnSecurityRepository columnSecurityRepository = context.getBean(ColumnSecurityRepository.class);

//		DatabaseSecurity car_db = DatabaseSecurity.builder()
//				.name("car_db")
//				.securityLevel(SecurityLevel.TOP_SECRET)
//				.build();
//		dbSecurityRepository.save(car_db);

		DatabaseSecurity db = dbSecurityRepository.findById(1L).get();

		TableSecurity table1 = TableSecurity.builder()
				.name("model")
				.securityLevel(SecurityLevel.TOP_SECRET)
				.databaseSecurity(db)
				.build();

		TableSecurity table2 = TableSecurity.builder()
				.name("price")
				.securityLevel(SecurityLevel.OF_PARTICULAR_IMPORTANCE)
				.databaseSecurity(db)
				.build();

		tableSecurityRepository.save(table1);
		tableSecurityRepository.save(table2);
	}

}
