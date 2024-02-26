package com.barabanov.mandatory.model.dbms;

import com.barabanov.mandatory.model.dbms.database.DbSecurityRepository;
import com.barabanov.mandatory.model.dbms.database.TableSecurityRepository;
import com.barabanov.mandatory.model.dbms.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.entity.TableSecurity;
import com.barabanov.mandatory.model.dbms.service.iterface.SecureDynamicDbService;
import com.barabanov.mandatory.model.dbms.service.iterface.SecureDynamicTableService;
import com.barabanov.mandatory.model.dbms.service.iterface.SecureDynamicTupleService;
import jakarta.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;

import static com.barabanov.mandatory.model.dbms.entity.SecurityLevel.SECRET;
import static com.barabanov.mandatory.model.dbms.entity.SecurityLevel.TOP_SECRET;


@SpringBootApplication
public class MandatoryModelInDbmsApplication
{

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MandatoryModelInDbmsApplication.class, args);

		SecureDynamicDbService secureDynamicDbService = context.getBean(SecureDynamicDbService.class);
		DbSecurityRepository dbSecurityRepository = context.getBean(DbSecurityRepository.class);
		EntityManager entityManager = context.getBean(EntityManager.class);
		TableSecurityRepository tableSecurityRepository = context.getBean(TableSecurityRepository.class);
		SecureDynamicTableService secureDynamicTableService = context.getBean(SecureDynamicTableService.class);



		// создание базы данных
		String dbName = "test_db_1";
		secureDynamicDbService.createDb(dbName, SECRET);
		DatabaseSecurity db = dbSecurityRepository.findByName(dbName).get();

		// проверка создания БД
		Integer foundedDb = (Integer) entityManager.createNativeQuery(
						" SELECT COUNT(*) FROM pg_database WHERE datname = '" + dbName + "'",
						Integer.class)
				.getSingleResult();

		// создание таблиц
		String tableName_1 = "car";
		List<ColumnDesc> columnsDesc_1 = List.of(
				new ColumnDesc("name", "VARCHAR(45)", List.of("UNIQUE"), TOP_SECRET),
				new ColumnDesc("id", "BIGSERIAL", List.of("PRIMARY KEY"), null)
		);
		secureDynamicTableService.createTableInDb(db.getId(), tableName_1, columnsDesc_1);

		String tableName_2 = "person";
		List<ColumnDesc> columnsDesc_2 = List.of(
				new ColumnDesc("name", "VARCHAR(45)", List.of("UNIQUE"), null),
				new ColumnDesc("id", "BIGSERIAL", List.of("PRIMARY KEY"), null)
		);
		secureDynamicTableService.createTableInDb(db.getId(), tableName_2, columnsDesc_2, TOP_SECRET);

		// проверка создания таблиц
		TableSecurity tableSecurity_1 = tableSecurityRepository.findByNameInDb(db.getId(), tableName_1).get();

		TableSecurity tableSecurity_2 = tableSecurityRepository.findByNameInDb(db.getId(), tableName_2).get();

		Boolean isTableExist = (Boolean) entityManager.createNativeQuery(
						"SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '" + tableName_1 + "') exist;",
						Boolean.class)
				.getSingleResult();
		System.out.println(isTableExist);
	}

}
