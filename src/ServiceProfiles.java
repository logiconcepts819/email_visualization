import java.util.NoSuchElementException;


public class ServiceProfiles {

	/**
	 * @param args
	 */
	
	class ProfileEntry
	{
		public String proto;
		public String name;
		public String hostname;
		int port;
		public ProfileEntry(String proto, String name, String hostname, int port)
		{
			this.proto = proto;
			this.name = name;
			this.hostname = hostname;
			this.port = port;
		}
	};
	
	// List as of 1/27/2013, taken from
	// http://www.arclab.com/products/amlc/list-of-smtp-and-pop3-servers-mailserver-list.html and
	// http://www.arclab.com/products/amlc/list-of-smtp-and-imap-servers-mailserver-list.html
	ProfileEntry[] profiles = {
		new ProfileEntry("imaps", "Googlemail/Gmail", "imap.gmail.com", 993),
		new ProfileEntry("pop3s", "Outlook.com", "pop3.live.com", 995),
		new ProfileEntry("imaps", "Yahoo Mail", "imap.mail.yahoo.com", 993),
		new ProfileEntry("imaps", "Yahoo Mail Plus", "plus.imap.mail.yahoo.com", 993),
		new ProfileEntry("imaps", "Yahoo UK", "imap.mail.yahoo.co.uk", 993),
		new ProfileEntry("imaps", "Yahoo Deutschland", "imap.mail.yahoo.de", 993),
		new ProfileEntry("imaps", "Yahoo AU/NZ", "imap.mail.yahoo.com.au", 993),
		new ProfileEntry("pop3s", "O2 (Ireland)", "pop3.o2.ie", 995),
		new ProfileEntry("pop3s", "O2 (UK)", "pop3.o2.co.uk", 995),
		new ProfileEntry("imaps", "AT&T", "imap.att.yahoo.com", 993),
		new ProfileEntry("imaps", "NTL @ntlworld.com", "pop.ntlworld.com", 993),
		new ProfileEntry("imap" , "BT Connect", "imap4.btconnect.com", 143),
		new ProfileEntry("pop3s", "BT Openworld", "mail.btopenworld.com", 995),
		new ProfileEntry("pop3s", "BT Internet", "mail.btinternet.com", 995),
		new ProfileEntry("pop3s", "Orange", "pop.orange.net", 995),
		new ProfileEntry("pop3s", "Orange (UK)", "pop.orange.co.uk", 995),
		new ProfileEntry("pop3s", "Wanadoo UK", "pop.wanadoo.co.uk", 995),
		new ProfileEntry("pop3s", "Hotmail", "pop3.live.com", 995),
		new ProfileEntry("imaps", "O2 Online Deutschland", "imap.o2online.de", 993),
		new ProfileEntry("imaps", "T-Online Deutschland", "imapmail.t-online.de", 993),
		new ProfileEntry("imaps", "1&1 (1and1)", "pop.1and1.com", 993),
		new ProfileEntry("imaps", "1&1 Deutschland", "imap.1und1.de", 995),
		new ProfileEntry("pop3s", "smtp.comcast.net", "mail.comcast.net", 995),
		new ProfileEntry("imaps", "Verizon", "incoming.verizon.net", 993),
		new ProfileEntry("pop3s", "Verizon hosted on Yahoo Server", "incoming.yahoo.verizon.net", 995)
	};
	
	public int getLength()
	{
		return profiles.length;
	}
	
	public String protoAt(int index)
	{
		return profiles[index].proto;
	}
	
	public String nameAt(int index)
	{
		return profiles[index].name;
	}
	
	public String hostnameAt(int index)
	{
		return profiles[index].hostname;
	}
	
	public int portAt(int index)
	{
		return profiles[index].port;
	}
	
	public String protoByName(String name) throws NoSuchElementException
	{
		for (int i = 0; i < profiles.length; i++)
		{
			if (profiles[i].name.equals(name))
			{
				return profiles[i].proto;
			}
		}
		throw new NoSuchElementException();		
	}
	
	public String hostnameByName(String name) throws NoSuchElementException
	{
		for (int i = 0; i < profiles.length; i++)
		{
			if (profiles[i].name.equals(name))
			{
				return profiles[i].hostname;
			}
		}
		throw new NoSuchElementException();
	}
	
	public int portByName(String name) throws NoSuchElementException
	{
		for (int i = 0; i < profiles.length; i++)
		{
			if (profiles[i].name.equals(name))
			{
				return profiles[i].port;
			}
		}
		throw new NoSuchElementException();
	}
}
