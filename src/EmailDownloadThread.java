import java.util.ArrayList;
import javax.mail.*;

public class EmailDownloadThread extends Thread {
  private String proto;
  private String hostname;
  private int port;
  private String username;
  private String password;
  private Store store;
  private Folder[] folders;
  private int message_count = 0;
  private ArrayList<EmailDownloadListener> email_download_listeners = new ArrayList<EmailDownloadListener>();
  private EmailCollection email_collection = new EmailCollection();

  EmailDownloadThread(String proto, String hostname, int port, String username, String password) {
    this.proto = proto;
    this.hostname = hostname;
    this.port = port;
    this.username = username;
    this.password = password;
  }

  @Override
  public void run() {
    super.run();

    try {
      //TODO fix the status stuff -- this class shouldn't know about Textlabels.
      setup();
      fetch_folders();
      fetch_messages();
      update_status("done");
      notify_listeners_emails_downloaded();
    } catch (MessagingException e1) {
      //TODO error handling
    }
  }

  public void add_listener(EmailDownloadListener listener) {
    this.email_download_listeners.add(listener);
  }

  private void setup() throws MessagingException {
    Session session = Session.getDefaultInstance(System.getProperties());
    //TODO XXX we can't get other folders because we're using pop3s -- this means we
    //can't get good data (I don't leave everything in my inbox...) so correct
    //this.
    //ref: http://stackoverflow.com/questions/5925944/how-to-retrieve-gmail-sub-folders-labels-using-pop3
    store = session.getStore(proto); // formerly pop3s
    update_status("Connecting and authenticating...");
    store.connect(hostname, port, username, password);
  }

  private void update_status(String status) {
    for (EmailDownloadListener listener : email_download_listeners) {
      listener.on_status(status);
    }
  }

  private void notify_listeners_emails_downloaded() {
    for (EmailDownloadListener listener : email_download_listeners) {
      listener.on_emails_downloaded(email_collection);
    }
  }

  private void fetch_folders() throws MessagingException {
    folders = store.getDefaultFolder().list("*");
  }

  private void fetch_messages() throws MessagingException {
    update_status("Attempting to retrieve messages...");
    for (Folder folder : folders) {
      // ref: http://stackoverflow.com/questions/4790844/how-to-get-the-list-of-available-folders-in-a-mail-account-using-java-mail
      if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
        folder.open(Folder.READ_ONLY);

        on_message(folder.getMessages()[0]);
        // TODO uncomment this
        //for (Message message : folder.getMessages()) {
        //on_message(message);
        //}

        // Note: Leave close(false) -- just in case we do accidentally delete a
        // message, we don't want to delete it in their inbox!
        folder.close(false);
      }
    }
  }

  private void on_message(Message message) {
    Email email = new Email(message);
    email_collection.add_email(email);

    message_count++;
    update_status("On message " + String.valueOf(message_count));
  }
}

interface EmailDownloadListener {
  void on_emails_downloaded(EmailCollection email_collection);
  void on_status(String status);
}


//Folder folder = store.getFolder("inbox");
//folder.open(Folder.READ_ONLY);
//Message[] messages = folder.getMessages();

//File file = new File("test.txt");

//// TODO: Make the application do more interesting stuff here
//try
//{
//Writer output = new BufferedWriter(new FileWriter(file));
//for (int i = 0; i < messages.length; i++)
//{
//status.setText("Retrieving message " + String.valueOf(i) + " of " + String.valueOf(messages.length) + "...");

//Date d;
//d = messages[i].getSentDate();
//if (d != null)
//{
//output.write("Date sent: " + d.toString() + '\n');
//}
//d = messages[i].getReceivedDate();
//if (d != null)
//{
//output.write("Date received: " + d.toString() + '\n');
//}
//Address[] addrs;
//addrs = messages[i].getFrom();
//if (addrs != null)
//{
//for (Address addr : addrs)
//{
//if (addr instanceof InternetAddress)
//{
//InternetAddress iaddr = (InternetAddress) addr;
//output.write("From: " + iaddr.getPersonal() + " <" + iaddr.getAddress() + ">\n");
//}
//else
//{
//output.write("From: " + addr.toString() + '\n');
//}
//}
//}
//addrs = messages[i].getRecipients(javax.mail.Message.RecipientType.TO);
//if (addrs != null)
//{
//for (Address addr : addrs)
//{
//if (addr instanceof InternetAddress)
//{
//InternetAddress iaddr = (InternetAddress) addr;
//output.write("To: " + iaddr.getPersonal() + " <" + iaddr.getAddress() + ">\n");
//}
//else
//{
//output.write("To: " + addr.toString() + '\n');
//}
//}
//}
//addrs = messages[i].getRecipients(javax.mail.Message.RecipientType.CC);
//if (addrs != null)
//{
//for (Address addr : addrs)
//{
//if (addr instanceof InternetAddress)
//{
//InternetAddress iaddr = (InternetAddress) addr;
//output.write("CC: " + iaddr.getPersonal() + " <" + iaddr.getAddress() + ">\n");
//}
//else
//{
//output.write("CC: " + addr.toString() + '\n');
//}
//}
//}
//output.write("Subject: " + messages[i].getSubject() + '\n');
//Object obj = messages[i].getContent();
//if (obj instanceof MimeMultipart)
//{
//MimeMultipart mmp = (MimeMultipart) obj;
//int nmmp = mmp.getCount();
//for (int j = 0; j < nmmp; j++)
//{
//Part p = mmp.getBodyPart(j);
//String disp = p.getDisposition();
//if (disp != null && (disp.equals(Part.INLINE) || disp.equals(Part.ATTACHMENT)))
//{
//MimeBodyPart mbp = (MimeBodyPart) p;
//if (mbp.isMimeType("text/plain") || mbp.isMimeType("text/html"))
//{
//output.write("Body:\n" + (String) mbp.getContent() + '\n');
//}
//else
//{
//Object sc = mbp.getContent();
//if (sc instanceof MimeMultipart)
//{
//MimeMultipart smp = (MimeMultipart) sc;
//int nsmp = smp.getCount();
//for (int k = 0; k < nsmp; k++)
//{
//MimeBodyPart smbp = (MimeBodyPart) smp.getBodyPart(k);
//output.write("Body:\n" + (String) smbp.getContent() + '\n');
//}
//}
//}
//}
//}
//}
//else if (obj instanceof String)
//{
//output.write("Body:\n" + (String) obj + '\n');
//}
//output.write('\n');
//}
//output.close();
//status.setText("Ready");
//} catch (IOException e)
//{
//// TODO handle exception
//}

//folder.close(true);
//store.close();


