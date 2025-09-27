package com.ilway.skystat.framework.adapter.output.mysql.data;

import com.ilway.skystat.domain.vo.metar.ReportType;
import com.ilway.skystat.domain.vo.unit.LengthUnit;
import com.ilway.skystat.domain.vo.unit.PressureUnit;
import com.ilway.skystat.domain.vo.unit.SpeedUnit;
import com.ilway.skystat.domain.vo.weather.type.WindDirectionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(name = "tbsst_metar_report")
public class MetarData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "station_icao")
	private String stationIcao;

	@Column(name = "obs_time_utc")
	private ZonedDateTime observationTime;

	@Column(name = "report_time_utc")
	private ZonedDateTime reportTime;

	@Column(name = "report_type")
	@Enumerated(STRING)
	private ReportType reportType;

	@Column(name = "is_auto")
	private Boolean isAuto;

	@Column(name = "wind_dir_type")
	@Enumerated(STRING)
	private WindDirectionType windDirectionType;

	@Column(name = "wind_unit")
	@Enumerated(STRING)
	private SpeedUnit windUnit;

	@Column(name = "wind_dir_deg")
	private Double windDirection;

	@Column(name = "wind_speed")
	private Double windSpeed;

	@Column(name = "wind_speed_kt", updatable = false, insertable = false)
	private Double windSpeedKt;

	@Column(name = "wind_gust")
	private Double windGust;

	@Column(name = "wind_gust_kt", updatable = false, insertable = false)
	private Double windGustKt;

	@Column(name = "wind_peak_kt", updatable = false, insertable = false)
	private Double windPeakKt;

	@Column(name = "wind_var_from_deg")
	private Double windVariableFrom;

	@Column(name = "wind_var_to_deg")
	private Double windVariableTo;

	@Column(name = "visibility_unit")
	@Enumerated(STRING)
	private LengthUnit visibilityUnit;

	@Column(name = "visibility")
	private Double visibility;

	@Column(name = "visibility_m", updatable = false, insertable = false)
	private Double visibilityM;

	@Column(name = "temp_c")
	private Double temperature;

	@Column(name = "dewpoint_c")
	private Double dewPoint;

	@Column(name = "altimeter_unit")
	@Enumerated(STRING)
	private PressureUnit altimeterUnit;

	@Column(name = "altimeter")
	private Double altimeter;

	@Column(name = "altimeterHpa", updatable = false, insertable = false)
	private Double altimeterHpa;

	@Column(name = "ceiling_ft")
	private Integer ceiling;

	@Column(name = "cloud_layer_count")
	private Integer cloudLayerCount;

	@Column(name = "weather_present")
	private Boolean weatherPresent;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "raw_text")
	private String rawText;

	@OneToMany(mappedBy = "metar", cascade = ALL, orphanRemoval = true, fetch = LAZY)
	@BatchSize(size = 100)
	private Set<CloudData> clouds = new LinkedHashSet<>();

	@OneToMany(mappedBy = "metar", cascade = ALL, orphanRemoval = true, fetch = LAZY)
	@BatchSize(size = 100)
	private Set<WeatherData> weathers = new LinkedHashSet<>();

	public void addCloud(CloudData c) {
		clouds.add(c);
		c.setMetar(this);
		cloudLayerCount--;
	}

	public void removeCloud(CloudData c) {
		clouds.remove(c);
		c.setMetar(null);
	}

	public void addWeather(WeatherData w) {
		weathers.add(w);
		w.setMetar(this);
	}

	public void removeWeather(WeatherData w) {
		weathers.remove(w);
		w.setMetar(null);
		if (weathers.isEmpty()) {
			weatherPresent = false;
		}
	}
}
