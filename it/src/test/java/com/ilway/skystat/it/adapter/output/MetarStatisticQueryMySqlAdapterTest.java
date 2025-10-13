package com.ilway.skystat.it.adapter.output;

import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarInventoryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarManagementRepository;
import com.ilway.skystat.it.config.MySQLConfigData;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MetarStatisticQueryMySqlAdapterTest extends MySQLConfigData {

	@Autowired
	public MetarStatisticQueryMySqlAdapterTest(MetarManagementRepository repository, EntityManager em, @Autowired MetarInventoryRepository metarInventoryRepository) {
		super(repository, em, metarInventoryRepository);
	}


}
