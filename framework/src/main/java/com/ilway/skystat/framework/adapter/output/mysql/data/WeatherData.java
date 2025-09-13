package com.ilway.skystat.framework.adapter.output.mysql.data;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherInensity;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.framework.adapter.output.mysql.data.converter.WeatherDescriptorConverter;
import com.ilway.skystat.framework.adapter.output.mysql.data.converter.WeatherPhenomenaConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(name = "metar_weather")
public class WeatherData {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private Long id;

	@Setter
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "metar_id")
	private MetarData metar;

	@Column(name = "intensity")
	@Enumerated(STRING)
	private WeatherInensity intensity;

	@Column(name = "descriptors")
	@Convert(converter = WeatherDescriptorConverter.class)
	private List<WeatherDescriptor> descriptors = new ArrayList<>();

	@Column(name = "phenomena")
	@Convert(converter = WeatherPhenomenaConverter.class)
	private List<WeatherPhenomenon> phenomena = new ArrayList<>();

}
