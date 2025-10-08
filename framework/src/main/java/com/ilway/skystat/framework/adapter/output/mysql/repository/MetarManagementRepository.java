package com.ilway.skystat.framework.adapter.output.mysql.repository;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.framework.adapter.output.mysql.data.MetarData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface MetarManagementRepository extends JpaRepository<MetarData, Long> {

	@Query("""
			SELECT (COUNT(m) > 0)
			FROM MetarData m
			WHERE m.stationIcao = :icao
				AND m.observationTime = :observationTime
				AND m.rawText = :rawText
		""")
	boolean existsByIcaoAndObsTimeAndRawText(@Param("icao") String icao,
	                                         @Param("observationTime") ZonedDateTime observationTime,
	                                         @Param("rawText") String rawText);

	@Query("""
		SELECT m.id
		FROM MetarData m
		WHERE m.stationIcao = :icao
		""")
	List<Long> findIdsByIcao(@Param("icao") String icao);

	@Query("""
		SELECT m.id
		FROM MetarData m
		WHERE m.stationIcao = :icao
		ORDER BY m.reportTime ASC, m.id ASC
		""")
	List<Long> findIdsByIcaoSorted(@Param("icao") String icao);

	@Query("""
		SELECT m.id
		FROM MetarData m
		WHERE m.stationIcao = :icao
			AND m.reportTime >= :fromInclusive
		  AND m.reportTime <  :toExclusive
		ORDER BY m.reportTime ASC, m.id ASC
		""")
	List<Long> findIdsByIcaoAndPeriod(@Param("icao") String icao,
	                                  @Param("fromInclusive") ZonedDateTime fromInclusive,
	                                  @Param("toExclusive") ZonedDateTime toExclusive);


}
