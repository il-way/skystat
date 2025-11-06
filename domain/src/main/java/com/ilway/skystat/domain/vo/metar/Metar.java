package com.ilway.skystat.domain.vo.metar;

import java.time.ZonedDateTime;

import com.ilway.skystat.domain.spec.WindSpeedSpec;
import com.ilway.skystat.domain.vo.weather.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.ilway.skystat.domain.service.TimeOperation;
import com.ilway.skystat.domain.spec.TimeZoneSpec;

@Getter
@ToString
@EqualsAndHashCode
public class Metar implements MetricSource {

  private final String rawText;
  
  // required fields (ICAO Annex 3)
  private final String stationIcao;
  private final ReportType reportType;
  private final ZonedDateTime observationTime;
  private final ZonedDateTime reportTime;
  private final Wind wind;
  private final Visibility visibility;
  private final Temperature temperature;
  private final Temperature dewPoint;
  private final Altimeter altimeter;
  
  // optional fields (ICAO Annex 3)
  private final Weathers weathers;
  private final Clouds clouds;
  private final String remarks;

  private static final TimeZoneSpec timeZoneSpec = new TimeZoneSpec();

  @Builder
	public Metar(String rawText, String stationIcao, ReportType reportType, ZonedDateTime observationTime, ZonedDateTime reportTime, Wind wind, Visibility visibility, Temperature temperature, Temperature dewPoint, Altimeter altimeter, Weathers weathers, Clouds clouds, String remarks) {
    timeZoneSpec.check(observationTime);

    this.rawText = rawText;
    this.stationIcao = stationIcao;
    this.reportType = reportType;
    this.observationTime = observationTime;
    this.reportTime = reportTime == null ? TimeOperation.toReportTime(observationTime) : reportTime;
    this.wind = wind;
    this.visibility = visibility;
    this.temperature = temperature;
    this.dewPoint = dewPoint;
    this.altimeter = altimeter;
    this.weathers = weathers != null ? weathers : Weathers.ofEmpty();
    this.clouds = clouds != null ? clouds : Clouds.ofEmpty();
    this.remarks = remarks;
	}

}