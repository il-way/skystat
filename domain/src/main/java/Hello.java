import lombok.Getter;

@Getter
public class Hello {

  private String hello = "HELLO";

  public void print() {
    System.out.println(hello + "domain");
  }

}
