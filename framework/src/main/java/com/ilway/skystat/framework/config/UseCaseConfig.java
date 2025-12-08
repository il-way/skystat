package com.ilway.skystat.framework.config;

import com.ilway.skystat.application.dto.statistic.CloudStatisticQuery;
import com.ilway.skystat.application.dto.statistic.ThresholdStatisticQuery;
import com.ilway.skystat.application.dto.statistic.WeatherStatisticQuery;
import com.ilway.skystat.application.port.input.*;
import com.ilway.skystat.application.port.output.*;
import com.ilway.skystat.application.service.metar.MetarInventoryService;
import com.ilway.skystat.application.service.metar.MetarManagementService;
import com.ilway.skystat.application.service.metar.MetarSaveFileService;
import com.ilway.skystat.application.service.metar.fallback.*;
import com.ilway.skystat.application.service.metar.query.*;
import com.ilway.skystat.application.service.metar.scan.*;
import com.ilway.skystat.framework.profile.Default;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Default
@Configuration
public class UseCaseConfig {

	@Bean
	public StatisticSummaryUseCase statisticSummaryUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                       MetarStatisticSummaryQueryOutputPort metarStatisticSummaryQueryOutputPort) {
		return new MetarStatisticSummaryFallbackService(
			new MetarStatisticSummaryQueryService(metarStatisticSummaryQueryOutputPort),
			new MetarStatisticSummaryService(metarManagementOutputPort)
		);
	}

	@Bean
	public BasicStatisticUseCase basicStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                   MetarBasicStatisticQueryOutputPort metarBasicStatisticQueryOutputPort) {
		return new MetarBasicStatisticFallbackService(
			new MetarBasicStatisticQueryService(metarBasicStatisticQueryOutputPort),
			new MetarBasicStatisticService(metarManagementOutputPort)
		);
	}

	@Bean
	public WindRoseUseCase windRoseUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                       WindRoseQueryOutputPort windRoseQueryOutputPort) {
		return new WindRoseFallbackService(
			new WindRoseQueryService(windRoseQueryOutputPort),
			new WindRoseService(metarManagementOutputPort)
		);
	}

	@Bean
	public TemperatureStatisticUseCase temperatureStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                               TemperatureStatisticQueryOutputPort temperatureStatisticQueryOutputPort) {
		return new TemperatureStatisticFallbackService(
			new TemperatureStatisticQueryService(temperatureStatisticQueryOutputPort),
			new TemperatureStatisticService(metarManagementOutputPort)
		);
	}

	@Bean
	public StatisticUseCase<ThresholdStatisticQuery> thresholdStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                                           ThresholdStatisticQueryOutputPort thresholdStatisticQueryOutputPort) {
		return new ThresholdStatisticFallbackService(
			new ThresholdStatisticService(metarManagementOutputPort),
			new ThresholdStatisticQueryService(thresholdStatisticQueryOutputPort)
		);
	}

	@Bean
	public StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                                       WeatherStatisticQueryOutputPort weatherStatisticQueryOutputPort) {
		return new WeatherStatisticFallbackService(
			new WeatherStatisticService(metarManagementOutputPort),
			new WeatherStatisticQueryService(weatherStatisticQueryOutputPort)
		);
	}

	@Bean
	public StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                                   CloudStatisticQueryOutputPort cloudStatisticQueryOutputPort) {
		return new CloudStatisticFallbackService(
			new CloudStatisticService(metarManagementOutputPort),
			new CloudStatisticQueryService(cloudStatisticQueryOutputPort)
		);
	}

	@Bean
	public MetarInventoryUseCase metarInventoryUseCase(MetarInventoryOutputPort metarInventoryOutputPort) {
		return new MetarInventoryService(metarInventoryOutputPort);
	}

	@Bean
	public MetarManagementUseCase metarManagementUseCase(MetarManagementOutputPort metarManagementOutputPort, MetarParsingOutputPort metarParsingOutputPort) {
		return new MetarManagementService(metarManagementOutputPort, metarParsingOutputPort);
	}

	@Bean
	MetarSaveFileUseCase metarSaveFileUseCase(MetarManagementOutputPort metarManagementOutputPort, MetarParsingOutputPort metarParsingOutputPort) {
		return new MetarSaveFileService(metarManagementOutputPort, metarParsingOutputPort);
	}

}
