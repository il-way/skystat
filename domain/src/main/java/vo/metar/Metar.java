package vo.metar;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import spec.TimeZoneSpec;
import vo.weather.*;

@Getter
@ToString
@EqualsAndHashCode
public class Metar {

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
	public Metar(String rawText, String stationIcao, ReportType reportType, ZonedDateTime observationTime, ZonedDateTime reportTime, Wind wind, Visibility visibility, Temperature temperature, Temperature dewPoint, Altimeter altimeter, WeatherGroup weatherGroup, CloudGroup cloudGroup, String remarks) {
    timeZoneSpec.check(observationTime);
    timeZoneSpec.check(reportTime);

    this.rawText = rawText;
    this.stationIcao = stationIcao;
    this.reportType = reportType;
    this.observationTime = observationTime;
    this.reportTime = reportTime;
    this.wind = wind;
    this.visibility = visibility;
    this.temperature = temperature;
    this.dewPoint = dewPoint;
    this.altimeter = altimeter;
    this.weatherGroup = weatherGroup;
    this.cloudGroup = cloudGroup;
    this.remarks = remarks;
	}
}