package exception;

public class MetarParseException extends RuntimeException {
  public MetarParseException(String message) {
    super(message);
  }

  public MetarParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
