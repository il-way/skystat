package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MetarManagementRepository extends JpaRepository<MetarData, Long> {

	@Query("""
		SELECT m.id
		FROM MetarData m
		WHERE UPPER(m.stationIcao) = UPPER(:icao)
		""")
	List<Long> findIdsByIcao(@Param("icao") String icao);

	@Query("""
		SELECT m.id
		FROM MetarData m
		WHERE UPPER(m.stationIcao) = UPPER(:icao)
		ORDER BY m.reportTime ASC, m.id ASC
		""")
	List<Long> findIdsByIcaoSorted(@Param("icao") String icao);

	@Query("""
		SELECT m.id
		FROM MetarData m
		WHERE UPPER(m.stationIcao) = UPPER(:icao)
			AND m.reportTime between :#{#period.from} and :#{#period.to}
		ORDER BY m.reportTime ASC, m.id ASC
		""")
	List<Long> findIdsByIcaoAndPeriod(@Param("icao") String icao,
	                                  @Param("period") RetrievalPeriod period);


}
