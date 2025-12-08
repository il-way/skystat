package com.ilway.skystat.bootstrap.config.production;

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
import com.ilway.skystat.bootstrap.profile.Production;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Production
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
			new ThresholdStatisticQueryService(thresholdStatisticQueryOutputPort),
			new ThresholdStatisticService(metarManagementOutputPort)
		);
	}

	@Bean
	public StatisticUseCase<WeatherStatisticQuery> weatherStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                                       WeatherStatisticQueryOutputPort weatherStatisticQueryOutputPort) {
		return new WeatherStatisticFallbackService(
			new WeatherStatisticQueryService(weatherStatisticQueryOutputPort),
			new WeatherStatisticService(metarManagementOutputPort)
		);
	}

	@Bean
	public StatisticUseCase<CloudStatisticQuery> cloudStatisticUseCase(MetarManagementOutputPort metarManagementOutputPort,
	                                                                   CloudStatisticQueryOutputPort cloudStatisticQueryOutputPort) {
		return new CloudStatisticFallbackService(
			new CloudStatisticQueryService(cloudStatisticQueryOutputPort),
			new CloudStatisticService(metarManagementOutputPort)
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
	public MetarManagementUseCase metarManagementUseCase(MetarManagementOutputPort metarManagementOutputPort, MetarParsingOutputPort metarParsingOutputPort) {
		return new MetarManagementService(metarManagementOutputPort, metarParsingOutputPort);
	}

	@Bean MetarSaveFileUseCase metarSaveFileUseCase(MetarManagementOutputPort metarManagementOutputPort, MetarParsingOutputPort metarParsingOutputPort) {
		return new MetarSaveFileService(metarManagementOutputPort, metarParsingOutputPort);
	}

	@Bean
	public MetarInventoryUseCase metarInventoryUseCase(MetarInventoryOutputPort metarInventoryOutputPort) {
		return new MetarInventoryService(metarInventoryOutputPort);
	}

}
