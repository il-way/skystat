package com.ilway.skystat.application.dto.windrose;

public record MonthlyWindRoseRow(
	int year, int month,
	int dirOrder, String dirLabel,
	int speedOrder, String speedLabel,
	int freq
) {
}
