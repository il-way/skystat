package com.ilway.skystat.framework.adapter.output.mysql.data;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@Table(name = "tbsst_metar_weather_descriptor")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class WeatherDescriptorData {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Setter
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "weather_id")
	private WeatherData weather;

	@Enumerated(STRING)
	@Column(name = "descriptor", nullable = false)
	private WeatherDescriptor descriptor;

}
