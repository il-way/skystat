package parser;

import exception.GenericSpecificationExeception;
import org.junit.jupiter.api.Test;
import parser.metar.entry.CloudGroupRegexParser;
import parser.shared.ReportParser;
import vo.weather.Cloud;
import vo.weather.CloudGroup;
import vo.weather.type.CloudCoverage;
import vo.weather.type.CloudType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CloudTest {

  ReportParser<CloudGroup> parser = new CloudGroupRegexParser();

  @Test
  public void 고도가_없는_구름객체를_생성할때_고도를_넣으면_예외가_발생한다() {
    // given, when, then

    assertThrows(GenericSpecificationExeception.class, () ->
            Cloud.builder()
                    .coverage(CloudCoverage.CLR)
                    .altitude(200)
                    .type(CloudType.NONE)
                    .build()
    );
  }

  @Test
  public void 고도가_십만을_넘는_구름을_생성하려고_하면_예외가_발생한다() {
    // given, when, then

    assertThrows(GenericSpecificationExeception.class, () ->
            Cloud.builder()
                    .coverage(CloudCoverage.CLR)
                    .altitude(1000)
                    .type(CloudType.NONE)
                    .build()
    );
  }

  @Test
  public void 고도가_필수인_구름객체를_생성할때_고도를_누락하면_예외가_발생한다() {
    // given, when, then

    assertThrows(GenericSpecificationExeception.class, () ->
            Cloud.builder()
                    .coverage(CloudCoverage.BKN)
                    .altitude(null)
                    .type(CloudType.NONE)
                    .build()
    );
  }

  @Test
  public void 고도가_없는_구름정보의_경우_altitude가_null인_구름객체를_포함하는_구름군_객체를_반환한다() {
    String rawText = "KHYI 010056Z AUTO 20004MPS 10SM CLR 09/07 A2999 RMK AO2 SLP153 T00940072";

    // when
    CloudGroupRegexParser parser = new CloudGroupRegexParser();
    CloudGroup cloudGroup = parser.parse(rawText);

    // then
    Cloud cloud = Cloud.builder()
            .coverage(CloudCoverage.CLR)
            .altitude(null)
            .type(CloudType.NONE)
            .build();

    CloudGroup expected = CloudGroup.builder()
            .cloudList(List.of(cloud))
            .build();

    assertAll(
            () -> assertEquals(expected, cloudGroup),
            () -> assertThrows(IllegalStateException.class, () ->
                    cloudGroup.getCloudList().get(0).getAltitudeOrThrow()
            )
    );
  }

  @Test
  public void 고도가_존재하는_단일구름이_존재하는_경우_구름군_객체의_구름리스트_길이가_1이다() {
    String rawText = "RKSI 010300Z 17008KT 4000 -RA SCT006 13/13 Q1007 NOSIG";

    // when
    CloudGroup cloudGroup = parser.parse(rawText);

    // then
    Cloud expected1 = Cloud.builder()
            .coverage(CloudCoverage.SCT)
            .altitude(600)
            .type(CloudType.NONE)
            .build();

    CloudGroup expected = CloudGroup.builder()
            .cloudList(List.of(expected1))
            .build();

    assertEquals(expected, cloudGroup);
  }


  @Test
  public void 고도가_존재하는_구름군이_존재하는_경우_해당_구름군_객체를_반환한다() {
    String rawText = "RKSI 010300Z 17008KT 4000 -RA SCT006 BKN025 OVC070CB 13/13 Q1007 NOSIG";

    // when
    CloudGroup cloudGroup = parser.parse(rawText);

    // then
    Cloud expected1 = Cloud.builder()
            .coverage(CloudCoverage.SCT)
            .altitude(600)
            .type(CloudType.NONE)
            .build();

    Cloud expected2 = Cloud.builder()
            .coverage(CloudCoverage.BKN)
            .altitude(2500)
            .type(CloudType.NONE)
            .build();


    Cloud expected3 = Cloud.builder()
            .coverage(CloudCoverage.OVC)
            .altitude(7000)
            .type(CloudType.CB)
            .build();

    CloudGroup expected = CloudGroup.builder()
            .cloudList(List.of(expected1, expected2, expected3))
            .build();

    assertEquals(expected, cloudGroup);
  }

  @Test
  public void 구름정보가_없는_메타_파싱성공() {
    String metar = "RKSI 010300Z 17008KT 4000 -RA 13/13 Q1007 NOSIG";

    CloudGroup cloudGroup = parser.parse(metar);

    assertEquals(cloudGroup.size(), 0);
  }

  @Test
  public void 단일구름과_임의의_공백을_가진_구름_파싱성공() {
    String rawText1 = "SCT006";
    String rawText1a = "SCT006 ";
    String rawText1b = " SCT006";
    String rawText1c = " SCT006 ";

    // when
    CloudGroup cloudGroup1 = parser.parse(rawText1);
    CloudGroup cloudGroup1a = parser.parse(rawText1a);
    CloudGroup cloudGroup1b = parser.parse(rawText1b);
    CloudGroup cloudGroup1c = parser.parse(rawText1c);

    // then
    Cloud expected1 = Cloud.builder()
            .coverage(CloudCoverage.SCT)
            .altitude(600)
            .type(CloudType.NONE)
            .build();

    CloudGroup expected = CloudGroup.builder()
            .cloudList(List.of(expected1))
            .build();

    assertAll(
            () -> assertEquals(expected, cloudGroup1),
            () -> assertEquals(expected, cloudGroup1a),
            () -> assertEquals(expected, cloudGroup1b),
            () -> assertEquals(expected, cloudGroup1c)
    );
  }

  @Test
  public void 임의의_공백과_구름군을_가진_구름_파싱성공() {
    String rawText1 = "SCT006 BKN025";
    String rawText1a = "SCT006 BKN025 ";
    String rawText1b = " SCT006  BKN025";

    // when
    CloudGroup cloudGroup1 = parser.parse(rawText1);
    CloudGroup cloudGroup1a = parser.parse(rawText1a);
    CloudGroup cloudGroup1b = parser.parse(rawText1b);

    // then
    Cloud expected1 = Cloud.builder()
            .coverage(CloudCoverage.SCT)
            .altitude(600)
            .type(CloudType.NONE)
            .build();


    Cloud expected2 = Cloud.builder()
            .coverage(CloudCoverage.BKN)
            .altitude(2500)
            .type(CloudType.NONE)
            .build();

    CloudGroup expected = CloudGroup.builder()
            .cloudList(List.of(expected1, expected2))
            .build();

    assertAll(
            () -> assertEquals(expected, cloudGroup1),
            () -> assertEquals(expected, cloudGroup1a),
            () -> assertEquals(expected, cloudGroup1b)
    );
  }

}
