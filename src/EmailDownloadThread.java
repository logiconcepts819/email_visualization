import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class EmailDownloadThread extends Thread {
  private String hostname;
  private int port;
  private String username;
  private String password;

  EmailDownloadThread(String hostname, int port, String username, String password) {
    this.hostname = hostname;
    this.port = port;
    this.username = username;
    this.password = password;
  }


  @Override
    public void run() {
      super.run();

      Session session = Session.getDefaultInstance(System.getProperties());

      try {
        Store store = session.getStore("pop3s");

        //TODO fix the status stuff.
        //status.setText("Connecting and authenticating...");
        store.connect(this.hostname, this.port, this.username, this.password);

        //status.setText("Attempting to retrieve messages...");

        Folder folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);
        Message[] messages = folder.getMessages();

        File file = new File("test.txt");

        // TODO: Make the application do more interesting stuff here
        try
        {
          Writer output = new BufferedWriter(new FileWriter(file));
          for (int i = 0; i < messages.length; i++)
          {
            //status.setText("Retrieving message " + String.valueOf(i) + " of " + String.valueOf(messages.length) + "...");

            Date d;
            d = messages[i].getSentDate();
            if (d != null)
            {
              output.write("Date sent: " + d.toString() + '\n');
            }
            d = messages[i].getReceivedDate();
            if (d != null)
            {
              output.write("Date received: " + d.toString() + '\n');
            }
            Address[] addrs;
            addrs = messages[i].getFrom();
            if (addrs != null)
            {
              for (Address addr : addrs)
              {
                if (addr instanceof InternetAddress)
                {
                  InternetAddress iaddr = (InternetAddress) addr;
                  output.write("From: " + iaddr.getPersonal() + " <" + iaddr.getAddress() + ">\n");
                }
                else
                {
                  output.write("From: " + addr.toString() + '\n');
                }
              }
            }
            addrs = messages[i].getRecipients(javax.mail.Message.RecipientType.TO);
            if (addrs != null)
            {
              for (Address addr : addrs)
              {
                if (addr instanceof InternetAddress)
                {
                  InternetAddress iaddr = (InternetAddress) addr;
                  output.write("To: " + iaddr.getPersonal() + " <" + iaddr.getAddress() + ">\n");
                }
                else
                {
                  output.write("To: " + addr.toString() + '\n');
                }
              }
            }
            addrs = messages[i].getRecipients(javax.mail.Message.RecipientType.CC);
            if (addrs != null)
            {
              for (Address addr : addrs)
              {
                if (addr instanceof InternetAddress)
                {
                  InternetAddress iaddr = (InternetAddress) addr;
                  output.write("CC: " + iaddr.getPersonal() + " <" + iaddr.getAddress() + ">\n");
                }
                else
                {
                  output.write("CC: " + addr.toString() + '\n');
                }
              }
            }
            output.write("Subject: " + messages[i].getSubject() + '\n');
            Object obj = messages[i].getContent();
            if (obj instanceof MimeMultipart)
            {
              MimeMultipart mmp = (MimeMultipart) obj;
              int nmmp = mmp.getCount();
              for (int j = 0; j < nmmp; j++)
              {
                Part p = mmp.getBodyPart(j);
                String disp = p.getDisposition();
                if (disp != null && (disp.equals(Part.INLINE) || disp.equals(Part.ATTACHMENT)))
                {
                  MimeBodyPart mbp = (MimeBodyPart) p;
                  if (mbp.isMimeType("text/plain") || mbp.isMimeType("text/html"))
                  {
                    output.write("Body:\n" + (String) mbp.getContent() + '\n');
                  }
                  else
                  {
                    Object sc = mbp.getContent();
                    if (sc instanceof MimeMultipart)
                    {
                      MimeMultipart smp = (MimeMultipart) sc;
                      int nsmp = smp.getCount();
                      for (int k = 0; k < nsmp; k++)
                      {
                        MimeBodyPart smbp = (MimeBodyPart) smp.getBodyPart(k);
                        output.write("Body:\n" + (String) smbp.getContent() + '\n');
                      }
                    }
                  }
                }
              }
            }
            else if (obj instanceof String)
            {
              output.write("Body:\n" + (String) obj + '\n');
            }
            output.write('\n');
          }
          output.close();
          //status.setText("Ready");
        }
        catch (IOException e)
        {
          // TODO handle exception
        }

        folder.close(true);
        store.close();
      } catch (MessagingException e1) {
        //TODO error handling
      }
    }
}
