package parser.shared;

public interface ReportParser <T> {

  T parse(String rawText);

}