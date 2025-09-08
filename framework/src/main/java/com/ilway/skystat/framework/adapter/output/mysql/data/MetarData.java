package com.ilway.skystat.framework.adapter.output.mysql.data;

import com.ilway.skystat.domain.vo.weather.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(name = "metar_report")
public class MetarData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "station_icao")
	private String icao;

	@Column(name = "obs_time_utc")
	private LocalDateTime observationTime;

	@Column(name = "report_time_utc")
	private ZonedDateTime reportTime;

	@Column(name = "report_type")
	private String reportType;

	@Column(name = "is_auto")
	private Boolean isAuto;

	@Column(name = "wind_dir_type")
	private String windDirectionType;

	@Column(name = "wind_unit")
	private String windUnit;

	@Column(name = "wind_dir_deg")
	private Integer windDirection;

	@Column(name = "wind_speed")
	private Integer windSpeed;

	@Column(name = "wind_gust")
	private Integer windGust;

	@Column(name = "wind_var_from_deg")
	private Integer windVariableFrom;

	@Column(name = "wind_var_to_deg")
	private Integer windVariableTo;

	@Column(name = "visibility_unit")
	private String visibilityUnit;

	@Column(name = "visibility")
	private Integer visibility;

	@Column(name = "temp_c")
	private Integer temperature;

	@Column(name = "dewpoint_c")
	private Integer dewPoint;

	@Column(name = "altimeter_unit")
	private String altimeterUnit;

	@Column(name = "altimeter")
	private Double altimeter;

	@Column(name = "ceiling_ft")
	private Integer ceiling;

	@Column(name = "cloud_group_count")
	private Integer cloudGroupCount;

	@Column(name = "weather_present")
	private Boolean weatherPresent;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "raw_text")
	private String rawText;

	@OneToMany(mappedBy = "metar", fetch = LAZY)
	private List<CloudData> cloudList = new ArrayList<>();

	@OneToMany(mappedBy = "metar", fetch = LAZY)
	private List<WeatherData> weatherList = new ArrayList<>();

	public void addCloud(CloudData c) {
		cloudList.add(c);
		c.setMetar(this);
		cloudGroupCount--;
	}

	public void removeCloud(CloudData c) {
		cloudList.remove(c);
		c.setMetar(null);
	}

	public void addWeather(WeatherData w) {
		weatherList.add(w);
		w.setMetar(this);
	}

	public void removeWeather(WeatherData w) {
		weatherList.remove(w);
		w.setMetar(null);
		if (weatherList.isEmpty()) {
			weatherPresent = false;
		}
	}
}
