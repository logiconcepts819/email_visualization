import javax.mail.Message;

// Represents a single Email
public class Email implements Visualizable {
  private Message message;

  Email(Message message) {
    this.message = message;
  }

  @Override
  public int sentiment() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int age() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int average_email_length() {
    // TODO Auto-generated method stub
    return 0;
  }
}

