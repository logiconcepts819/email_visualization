import java.util.ArrayList;

// Represents a collection of emails
//
// TODO make an interface to represent emails over time in addition to
// Visualizable
public class EmailCollection implements Visualizable {
  private ArrayList<Email> emails = new ArrayList<Email>();
  // TODO some collection

  public void add_email(Email email) {
    emails.add(email);
  }

  @Override
  public float sentiment() {
    float sentiment = 0.0f;
    for (Email email : emails) {
      sentiment += email.sentiment();
    }
    return sentiment;
  }

  @Override
  public int age() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float average_email_length() {
    // TODO Auto-generated method stub
    return 0;
  }
}

