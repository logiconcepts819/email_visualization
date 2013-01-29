import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

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

import processing.core.PApplet;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlP5;
import controlP5.ListBox;
import controlP5.Textfield;
import controlP5.Textlabel;

public class TreeMain extends PApplet {

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	float theta;
	ControlP5 controlP5;
	
	Textlabel title_lbl1, title_lbl2;
	ListBox plist;
	Textfield host, port, login, pass;
	Button submit;
	Textlabel status;
	POP3Profiles prof;
	
	int sbbgColor;
	
	class StatTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			sbbgColor = 0xff404040;
			status.setText("Ready");
		}
	}
	
	class ConnectThread extends Thread
	{

		public String hostname;
		public int port;
		public String user;
		public String pass;
		
		@Override
		public void run() {
			super.run();
			
			Properties prop = System.getProperties();
			Session session = Session.getDefaultInstance(prop);
			
			try
			{
				Store store = session.getStore("pop3s");
				
				status.setText("Connecting and authenticating...");
				store.connect(hostname, port, user, pass);
				
				status.setText("Attempting to retrieve messages...");

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
						status.setText("Retrieving message " + String.valueOf(i) + " of " + String.valueOf(messages.length) + "...");
						
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
					status.setText("Ready");
				}
				catch (IOException e)
				{
					// TODO: handle exception
				}
				
				folder.close(true);
				store.close();
			}
			catch (MessagingException e1)
			{
				// Catch connection failure
				sbbgColor = 0xff400000;
				status.setText("Failed to retrieve messages (reason = " + e1.getLocalizedMessage() + ")");
				statusTimer.schedule(new StatTimerTask(), 5000);
			}
			
			EnableStartScreen(true);
		}
		
	}
	
	Timer statusTimer;
	ConnectThread thread;
	
	boolean shift_active = false;
	
	enum States
	{
		STATE_LOGIN;
	}
	
	States currState = States.STATE_LOGIN;
	
	void EnableStartScreen(boolean enable)
	{
		if (enable)
		{
			host.unlock();
			port.unlock();
			login.unlock();
			pass.unlock();
			submit.unlock();
		}
		else
		{
			host.lock();
			port.lock();
			login.lock();
			pass.lock();
			submit.lock();			
		}
	}
	
	public void authenticate(String hostname, int port, String user, String pass)
	{
		statusTimer.cancel();
		statusTimer = new Timer();
		thread = new ConnectThread();
		
		status.setText("Connecting to POP server...");
		EnableStartScreen(false);
		
		thread.hostname = hostname;
		thread.port = port;
		thread.user = user;
		thread.pass = pass;
		
		thread.start();
	}
	
	// Colors are expressed as 0xAARRGGBB.
	@Override
	public void setup()
	{
		size(640, 560);
		smooth();
		
		statusTimer = new Timer();
		sbbgColor = 0xff404040;
		controlP5 = new ControlP5(this);
		
		ControlFont cf = new ControlFont(createFont("Arial", 16));
		controlP5.setFont(cf);
		
		controlP5.setColorForeground(0xffffffff);
		controlP5.setColorBackground(0xff000000);
		
		cf.setSize(72);
		title_lbl1 = controlP5.addTextlabel("tree", "TREE");
		cf.setSize(16);
		title_lbl2 = controlP5.addTextlabel("tree2", "TREE Representation of Everlasting Emails");
		
		int w1 = ControlFont.getWidthFor(title_lbl1.getValueLabel().getText(), title_lbl1.getValueLabel(), this);
		int w2 = ControlFont.getWidthFor(title_lbl2.getValueLabel().getText(), title_lbl2.getValueLabel(), this);
		
		title_lbl1.setPosition((width - w1) / 2.0f, 20.0f);
		title_lbl2.setPosition((width - w2) / 2.0f, 100.0f);
		
		controlP5.addTextlabel("plistlabel", "Profile list:", 20, 150);
		controlP5.addTextlabel("hostlabel", "Hostname:", 20, 250);
		controlP5.addTextlabel("portlabel", "Port:", 20, 300);
		controlP5.addTextlabel("loginlabel", "Username:", 20, 350);
		controlP5.addTextlabel("passwordlabel", "Password: ", 20, 400);
		
		plist = controlP5.addListBox("profilelist", 110, 150, 500, 80);
		host = controlP5.addTextfield("hostbox", 110, 240, 500, 40);
		port = controlP5.addTextfield("portbox", 110, 290, 500, 40);
		login = controlP5.addTextfield("loginbox", 110, 340, 500, 40);
		pass = controlP5.addTextfield("passwordbox", 110, 390, 500, 40);
		cf.setSize(36);
		submit = controlP5.addButton("submitbtn");
		cf.setSize(16);
		pass.setPasswordMode(true);
		
		submit.setCaptionLabel("Login");
		submit.getCaptionLabel().align(CENTER, CENTER);
		submit.setSize(200, 70);
		submit.setPosition((width - submit.getWidth()) / 2.0f, 440);
		submit.setColorForeground(0xff606060);
		submit.setColorBackground(0xff404040);
		
		plist.getCaptionLabel().hide();
		host.getCaptionLabel().hide();
		port.getCaptionLabel().hide();
		login.getCaptionLabel().hide();
		pass.getCaptionLabel().hide();
		
		plist.setColorForeground(0xff606060);
		plist.setColorBackground(0xff404040);
		host.setColorBackground(0xff404040);
		port.setColorBackground(0xff404040);
		login.setColorBackground(0xff404040);
		pass.setColorBackground(0xff404040);
		
		prof = new POP3Profiles();
		plist.hideBar();
		plist.addItem("-- Select an item --", 0);
		for (int i = 0; i < prof.getLength(); i++)
		{
			plist.addItem(prof.nameAt(i), i + 1);
		}
		
		status = controlP5.addTextlabel("statusbar", "Ready", 10, 530);
	}

	@Override
	public void draw()
	{
		background(0);
		frameRate(30);
		switch (currState)
		{
			case STATE_LOGIN:
				fill(sbbgColor);
				rect(0, 520, 640, 40);
				break;
			default:
		}
		/*
		stroke(255);
		// Let's pick an angle 0 to 90 degrees based on the mouse position
		float a = (mouseX / (float) width) * 90f;
		// Convert it to radians
		theta = radians(a);
		// Start the tree from the bottom of the screen
		translate(width/2,height);
		// Draw a line 120 pixels
		line(0,0,0,-120);
		// Move to the end of that line
		translate(0,-120);
		// Start the recursive branching!
		branch(120);
		*/
	}

	void branch(float h)
	{
		// Each branch will be 2/3rds the size of the previous one
		float new_h = h * 0.66f;
		
		// All recursive functions must have an exit condition!!!!
		// Here, ours is when the length of the branch is 2 pixels or less
		if (new_h > 2)
		{
			pushMatrix();           // Save the current state of transformation (i.e. where are we now)
			rotate(theta);          // Rotate by theta
			line(0, 0, 0, -new_h);  // Draw the branch
			translate(0, -new_h);   // Move to the end of the branch
			branch(new_h);          // Ok, now call myself to draw two new branches!!
			popMatrix();            // Whenever we get back here, we "pop" in order to restore the previous matrix state
			
			// Repeat the same thing, only branch off to the "left" this time!
			pushMatrix();
			rotate(-theta);
			line(0, 0, 0, -new_h);
			translate(0, -new_h);
			branch(new_h);
			popMatrix();
		}
	}

	public void controlEvent(ControlEvent theEvent)
	{
		if (theEvent.isGroup())
		{
			if (theEvent.getGroup().equals(plist))
			{
				int idx = (int) plist.getValue();
				if (idx != 0)
				{
					idx--;
					host.setText(prof.hostnameAt(idx));
					port.setText(String.valueOf(prof.portAt(idx)));
					
					if (login.getText().isEmpty() || pass.getText().isEmpty())
					{
						host.setFocus(false);
						port.setFocus(false);
						login.setFocus(login.getText().isEmpty());
						pass.setFocus(!login.getText().isEmpty());
					}
				}
			}
		}
		else if (theEvent.isController())
		{
			if (theEvent.getController().equals(submit))
			{
				String shost = host.getText();
				int iport = parseInt(port.getText());
				String suser = login.getText();
				String spass = pass.getText();
				authenticate(shost, iport, suser, spass);
			}
		}
	}
	
	@Override
	public void keyPressed()
	{
		// Unfortunately, controlP5 doesn't implement tab to navigate, so this
		// method and the following method (keyReleased) along with the state
		// variable 'shift_active' provide one workaround
		if (keyCode == SHIFT)
		{
			shift_active = true;
		}
		else if (key == TAB)
		{
			if (host.isFocus())
			{
				pass.setFocus(shift_active);
				host.setFocus(false);
				port.setFocus(!shift_active);
			}
			else if (port.isFocus())
			{
				host.setFocus(shift_active);
				port.setFocus(false);
				login.setFocus(!shift_active);
			}
			else if (login.isFocus())
			{
				port.setFocus(shift_active);
				login.setFocus(false);
				pass.setFocus(!shift_active);
			}
			else
			{
				login.setFocus(shift_active);
				pass.setFocus(false);
				host.setFocus(!shift_active);
			}
		}
	}
	
	@Override
	public void keyReleased()
	{
		if (keyCode == SHIFT)
		{
			shift_active = false;
		}
	}
}
