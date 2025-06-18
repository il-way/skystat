package dto.statistic;

import java.util.List;

public record ObservationStatisticResponse(
	List<MonthlyCountDto> monthly,
	List<HourlyCountDto>  hourly
) { }
