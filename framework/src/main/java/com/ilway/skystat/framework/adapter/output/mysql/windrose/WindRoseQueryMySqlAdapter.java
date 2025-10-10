package com.ilway.skystat.framework.adapter.output.mysql.windrose;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.MonthlyCountDto;
import com.ilway.skystat.application.dto.windrose.MonthlyWindRoseRow;
import com.ilway.skystat.application.port.output.WindRoseQueryOutputPort;
import com.ilway.skystat.framework.adapter.output.mysql.repository.MetarWindRoseQueryRepository;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.MonthlyCountQueryDto;
import com.ilway.skystat.framework.adapter.output.mysql.repository.dto.WindRoseRow;
import com.ilway.skystat.framework.common.annotation.TranslateDbExceptions;
import com.ilway.skystat.framework.common.annotation.UppercaseParam;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ilway.skystat.domain.vo.weather.type.WindDirectionType.VARIABLE;

@TranslateDbExceptions("querying wind rose")
@RequiredArgsConstructor
public class WindRoseQueryMySqlAdapter implements WindRoseQueryOutputPort {

	private final MetarWindRoseQueryRepository windRoseQueryRepository;

	@Override
	public List<MonthlyWindRoseRow> aggregateDefaultByMonth(@UppercaseParam String icao, RetrievalPeriod period) {
		List<WindRoseRow> rows = windRoseQueryRepository.aggregateDefaultByMonth(icao, period.fromInclusive(), period.toExclusive());
		return rows.stream().map(WindRoseQueryMySqlAdapter::toMonthlyWindRoseRow)
			.toList();
	}

	@Override
	public List<MonthlyCountDto> countVariableByMonth(@UppercaseParam String icao, RetrievalPeriod period) {
		List<MonthlyCountQueryDto> monthly = windRoseQueryRepository.countVariableByMonth(icao, period.fromInclusive(), period.toExclusive(), VARIABLE);
		return monthly.stream().map(m -> new MonthlyCountDto(m.year(), m.month(), m.count()))
			.toList();
	}

	private static MonthlyWindRoseRow toMonthlyWindRoseRow(WindRoseRow row) {
		return new MonthlyWindRoseRow(
			row.getY(), row.getM(),
			row.getDirOrder(), row.getDirLabel(),
			row.getSpeedOrder(), row.getSpeedLabel(),
			row.getFreq().intValue()
		);
	}

}
