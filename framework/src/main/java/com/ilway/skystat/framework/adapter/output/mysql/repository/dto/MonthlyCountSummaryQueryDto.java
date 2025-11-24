package com.ilway.skystat.framework.adapter.output.mysql.repository.dto;

import com.ilway.skystat.application.dto.statistic.AverageSummary;
import com.ilway.skystat.application.dto.statistic.MonthlyCountSummaryDto;

public record MonthlyCountSummaryQueryDto(Integer year, Integer month, Long windPeakKtCount, Long visibilityMCount,
                                          Long ceilingFtCount, Long phenomenonCount, Long descriptorCount) {

	public static MonthlyCountSummaryDto map(MonthlyCountSummaryQueryDto dto) {
		if (dto == null) {
			return new MonthlyCountSummaryDto(0, 0, 0, 0, 0, 0, 0);
		}
		return new MonthlyCountSummaryDto(
			nvl(dto.year()),
			nvl(dto.month()),
			nvl(dto.windPeakKtCount()),
			nvl(dto.visibilityMCount()),
			nvl(dto.ceilingFtCount()),
			nvl(dto.phenomenonCount()),
			nvl(dto.descriptorCount())
		);
	}

	private static long nvl(Long v) {
		return v != null ? v : 0L;
	}

	private static int nvl(Integer v) {
		return v != null ? v : 0;
	}

}
