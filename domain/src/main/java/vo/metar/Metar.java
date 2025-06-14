package vo.metar;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import vo.metar.field.*;
import vo.metar.type.ReportType;

@Getter
@Builder
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

}