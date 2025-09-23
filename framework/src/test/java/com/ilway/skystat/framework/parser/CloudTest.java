package com.ilway.skystat.framework.parser;

import com.ilway.skystat.domain.exception.GenericSpecificationExeception;
import org.junit.jupiter.api.Test;
import com.ilway.skystat.framework.parser.metar.entry.CloudsRegexParser;
import com.ilway.skystat.framework.parser.metar.entry.CloudRegexParser;
import com.ilway.skystat.framework.parser.shared.ReportParser;
import com.ilway.skystat.domain.vo.weather.Cloud;
import com.ilway.skystat.domain.vo.weather.Clouds;
import com.ilway.skystat.domain.vo.weather.type.CloudCoverage;
import com.ilway.skystat.domain.vo.weather.type.CloudType;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class CloudTest {

  ReportParser<Clouds> parser = new CloudsRegexParser();
  ReportParser<Cloud> cloudParser = new CloudRegexParser();

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
  public void 고도가_필수인_구름객체를_파싱할때_고도를_누락하면_예외가_발생한다() {
    // given, when, then
    String rawText = "RKSI 270700Z 22010KT 180V240 CAVOK 15/07 Q1016 TEMPO 9999 -RA FEW 030 OVC 080";
    
    assertThrows(IllegalArgumentException.class, () -> {
      cloudParser.parse(rawText);
    });
  }

  @Test
  public void 고도가_없는_구름정보의_경우_altitude가_null인_구름객체를_포함하는_구름군_객체를_반환한다() {
    String rawText = "KHYI 010056Z AUTO 20004MPS 10SM CLR 09/07 A2999 RMK AO2 SLP153 T00940072";

    // when
    CloudsRegexParser parser = new CloudsRegexParser();
    Clouds clouds = parser.parse(rawText);

    // then
    Cloud cloud = Cloud.builder()
            .coverage(CloudCoverage.CLR)
            .altitude(null)
            .type(CloudType.NONE)
            .build();

    Clouds expected = Clouds.builder()
            .clouds(List.of(cloud))
            .build();

    assertAll(
            () -> assertEquals(expected, clouds),
            () -> assertThrows(NoSuchElementException.class, () ->
                    clouds.getClouds().get(0).getAltitudeOptional().orElseThrow()
            )
    );
  }

  @Test
  public void 고도가_존재하는_단일구름이_존재하는_경우_구름군_객체의_구름리스트_길이가_1이다() {
    String rawText = "RKSI 010300Z 17008KT 4000 -RA SCT006 13/13 Q1007 NOSIG";

    // when
    Clouds clouds = parser.parse(rawText);

    // then
    Cloud expected1 = Cloud.builder()
            .coverage(CloudCoverage.SCT)
            .altitude(600)
            .type(CloudType.NONE)
            .build();

    Clouds expected = Clouds.builder()
            .clouds(List.of(expected1))
            .build();

    assertEquals(expected, clouds);
  }


  @Test
  public void 고도가_존재하는_구름군이_존재하는_경우_해당_구름군_객체를_반환한다() {
    String rawText = "RKSI 010300Z 17008KT 4000 -RA SCT006 BKN025 OVC070CB 13/13 Q1007 NOSIG";

    // when
    Clouds clouds = parser.parse(rawText);

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

    Clouds expected = Clouds.builder()
            .clouds(List.of(expected1, expected2, expected3))
            .build();

    assertEquals(expected, clouds);
  }

  @Test
  public void 구름정보가_없는_메타_파싱성공() {
    String metar = "RKSI 010300Z 17008KT 4000 -RA 13/13 Q1007 NOSIG";

    Clouds clouds = parser.parse(metar);

    assertEquals(clouds.size(), 0);
  }

  @Test
  public void 단일구름과_임의의_공백을_가진_구름_파싱성공() {
    String rawText1 = "SCT006";
    String rawText1a = "SCT006 ";
    String rawText1b = " SCT006";
    String rawText1c = " SCT006 ";

    // when
    Clouds clouds1 = parser.parse(rawText1);
    Clouds clouds1A = parser.parse(rawText1a);
    Clouds clouds1B = parser.parse(rawText1b);
    Clouds clouds1C = parser.parse(rawText1c);

    // then
    Cloud expected1 = Cloud.builder()
            .coverage(CloudCoverage.SCT)
            .altitude(600)
            .type(CloudType.NONE)
            .build();

    Clouds expected = Clouds.builder()
            .clouds(List.of(expected1))
            .build();

    assertAll(
            () -> assertEquals(expected, clouds1),
            () -> assertEquals(expected, clouds1A),
            () -> assertEquals(expected, clouds1B),
            () -> assertEquals(expected, clouds1C)
    );
  }

  @Test
  public void 임의의_공백과_구름군을_가진_구름_파싱성공() {
    String rawText1 = "SCT006 BKN025";
    String rawText1a = "SCT006 BKN025 ";
    String rawText1b = " SCT006  BKN025";

    // when
    Clouds clouds1 = parser.parse(rawText1);
    Clouds clouds1A = parser.parse(rawText1a);
    Clouds clouds1B = parser.parse(rawText1b);

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

    Clouds expected = Clouds.builder()
            .clouds(List.of(expected1, expected2))
            .build();

    assertAll(
            () -> assertEquals(expected, clouds1),
            () -> assertEquals(expected, clouds1A),
            () -> assertEquals(expected, clouds1B)
    );
  }

}
