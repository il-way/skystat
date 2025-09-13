package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetarManagementRepository extends JpaRepository<MetarData, Long> {
}
