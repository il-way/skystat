package com.ilway.skystat.framework.adapter.output.mysql.data;

import com.ilway.skystat.domain.vo.weather.type.WeatherDescriptor;
import com.ilway.skystat.domain.vo.weather.type.WeatherIntensity;
import com.ilway.skystat.domain.vo.weather.type.WeatherPhenomenon;
import com.ilway.skystat.framework.adapter.output.mysql.data.converter.WeatherDescriptorConverter;
import com.ilway.skystat.framework.adapter.output.mysql.data.converter.WeatherPhenomenaConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(name = "tbsst_metar_weather")
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
	private WeatherIntensity intensity;

	@OneToMany(mappedBy = "weather", cascade = ALL, orphanRemoval = true, fetch = LAZY)
	@BatchSize(size = 100)
	private Set<WeatherDescriptorData> descriptors = new LinkedHashSet<>();

	@OneToMany(mappedBy = "weather", cascade = ALL, orphanRemoval = true, fetch = LAZY)
	@BatchSize(size = 100)
	private Set<WeatherPhenomenonData> phenomena = new LinkedHashSet<>();

	public void addDescriptor(WeatherDescriptorData d) {
		descriptors.add(d);
		d.setWeather(this);
	}
	public void removeDescriptor(WeatherDescriptorData d) {
		descriptors.remove(d);
		d.setWeather(null);
	}
	public void addPhenomenon(WeatherPhenomenonData p) {
		phenomena.add(p);
		p.setWeather(this);
	}
	public void removePhenomenon(WeatherPhenomenonData p) {
		phenomena.remove(p);
		p.setWeather(null);
	}

}
