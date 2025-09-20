package com.ilway.skystat.application.statistic;

import com.ilway.skystat.application.dto.RetrievalPeriod;
import com.ilway.skystat.application.dto.statistic.ObservationStatisticResponse;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.model.generic.Comparison;
import com.ilway.skystat.application.model.weather.ThresholdCondition;
import com.ilway.skystat.application.model.weather.WeatherCondition;
import com.ilway.skystat.application.port.input.internal.ObservationStatisticAggregator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ilway.skystat.application.port.input.CloudStatisticInputPort;
import com.ilway.skystat.application.port.input.ThresholdStatisticInputPort;
import com.ilway.skystat.application.port.input.WeatherStatisticInputPort;
import com.ilway.skystat.application.statistic.data.StatisticTestData;

import java.util.List;

import static com.ilway.skystat.application.model.weather.MetricField.VISIBILITY;
import static com.ilway.skystat.application.model.weather.MetricField.WIND_SPEED;
import static com.ilway.skystat.application.model.weather.WeatherConditionPredicate.HAS_PHENOMENA;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.ilway.skystat.domain.vo.unit.LengthUnit.METERS;
import static com.ilway.skystat.domain.vo.unit.SpeedUnit.KT;
import static com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon.BR;

public class StatisticTest extends StatisticTestData {

	@BeforeEach
	void init() {
		thresholdStatisticUseCase = new ThresholdStatisticInputPort(metarManagementOutputPort);
		weatherStatisticUseCase = new WeatherStatisticInputPort(metarManagementOutputPort);
		cloudStatisticUseCase = new CloudStatisticInputPort(metarManagementOutputPort);
	}

	@Test
	void 시정통계_생성에_성공해야한다() {
		ThresholdStatisticQuery query = new ThresholdStatisticQuery(
			"RKSI",
			new RetrievalPeriod(
				ofUTC(2024, 1, 1, 0, 0),
				ofUTC(2024, 1, 1, 2, 0)
			),
			new ThresholdCondition(VISIBILITY, Comparison.LTE, 4000, METERS)
		);

		ObservationStatisticResponse response = thresholdStatisticUseCase.execute(query);
		System.out.println(response.toString());
		ObservationStatisticResponse actual = ObservationStatisticAggregator.peelOffZeroCount(response);
		assertAll(
			() -> assertEquals(1, actual.monthly().size()),
			() -> assertEquals(1, actual.hourly().size())
		);
	}

	@Test
	void 풍속통계_생성에_성공해야한다() {
		ThresholdStatisticQuery query = new ThresholdStatisticQuery(
			"RKSI",
			new RetrievalPeriod(
				ofUTC(2024, 1, 1, 0, 0),
				ofUTC(2024, 1, 1, 2, 0)
			),
			new ThresholdCondition(WIND_SPEED, Comparison.GTE, 5, KT)
		);

		ObservationStatisticResponse response = thresholdStatisticUseCase.execute(query);
		ObservationStatisticResponse actual = ObservationStatisticAggregator.peelOffZeroCount(response);

		assertAll(
			() -> assertEquals(1, actual.monthly().size()),
			() -> assertEquals(1, actual.hourly().size())
		);
	}

	@Test
	void 날씨통계_생성에_성공해야한다() {
		WeatherStatisticQuery query = new WeatherStatisticQuery(
			"RKSI",
			new RetrievalPeriod(
				ofUTC(2024, 1, 1, 0, 0),
				ofUTC(2024, 1, 1, 2, 0)
			),
			new WeatherCondition(HAS_PHENOMENA, List.of(BR))
		);

		ObservationStatisticResponse actual = weatherStatisticUseCase.execute(query);
		assertAll(
			() -> assertEquals(1, actual.monthly().size()),
			() -> assertEquals(3, actual.hourly().size())
		);
	}



}
