package com.ilway.skystat.domain.vo.metar;

import java.time.ZonedDateTime;

import com.ilway.skystat.domain.vo.weather.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import com.ilway.skystat.domain.service.TimeOperation;
import com.ilway.skystat.domain.spec.TimeZoneSpec;
import com.ilway.skystat.domain.vo.weather.*;

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
  private final WeatherGroup weatherGroup;
  private final CloudGroup cloudGroup;
  private final String remarks;

  private static final TimeZoneSpec timeZoneSpec = new TimeZoneSpec();

  @Builder
	public Metar(String rawText, String stationIcao, ReportType reportType, ZonedDateTime observationTime, Wind wind, Visibility visibility, Temperature temperature, Temperature dewPoint, Altimeter altimeter, WeatherGroup weatherGroup, CloudGroup cloudGroup, String remarks) {
    timeZoneSpec.check(observationTime);

    this.rawText = rawText;
    this.stationIcao = stationIcao;
    this.reportType = reportType;
    this.observationTime = observationTime;
    this.reportTime = TimeOperation.toReportTime(observationTime);
    this.wind = wind;
    this.visibility = visibility;
    this.temperature = temperature;
    this.dewPoint = dewPoint;
    this.altimeter = altimeter;
    this.weatherGroup = weatherGroup != null ? weatherGroup : WeatherGroup.ofEmpty();
    this.cloudGroup = cloudGroup != null ? cloudGroup : CloudGroup.ofEmpty();
    this.remarks = remarks;
	}
}