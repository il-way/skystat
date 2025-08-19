package com.ilway.skystat.framework.parser.shared;

public interface ReportParser <T> {

  T parse(String rawText);

}