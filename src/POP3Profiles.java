import java.util.NoSuchElementException;


public class POP3Profiles {

	/**
	 * @param args
	 */
	
	class ProfileEntry
	{
		public String name;
		public String hostname;
		int port;
		public ProfileEntry(String _name, String _hostname, int _port)
		{
			name = _name;
			hostname = _hostname;
			port = _port;
		}
	};
	
	// List as of 1/27/2013, taken from
	// http://www.arclab.com/products/amlc/list-of-smtp-and-pop3-servers-mailserver-list.html
	ProfileEntry[] profiles = {
		new ProfileEntry("Googlemail/Gmail", "pop.gmail.com", 995),
		new ProfileEntry("Outlook.com", "pop3.live.com", 995),
		new ProfileEntry("Yahoo Mail", "pop.mail.yahoo.com", 995),
		new ProfileEntry("Yahoo Mail Plus", "plus.pop.mail.yahoo.com", 995),
		new ProfileEntry("Yahoo UK", "pop.mail.yahoo.co.uk", 995),
		new ProfileEntry("Yahoo Deutschland", "pop.mail.yahoo.de", 995),
		new ProfileEntry("Yahoo AU/NZ", "pop.mail.yahoo.com.au", 995),
		new ProfileEntry("O2 (Ireland)", "pop3.o2.ie", 995),
		new ProfileEntry("O2 (UK)", "pop3.o2.co.uk", 995),
		new ProfileEntry("AT&T", "pop.att.yahoo.com", 995),
		new ProfileEntry("NTL @ntlworld.com", "pop.ntlworld.com", 995),
		new ProfileEntry("BT Connect", "mail.btconnect.com", 995),
		new ProfileEntry("BT Openworld", "mail.btopenworld.com", 995),
		new ProfileEntry("BT Internet", "mail.btinternet.com", 995),
		new ProfileEntry("Orange", "pop.orange.net", 995),
		new ProfileEntry("Orange (UK)", "pop.orange.co.uk", 995),
		new ProfileEntry("Wanadoo UK", "pop.wanadoo.co.uk", 995),
		new ProfileEntry("Hotmail", "pop3.live.com", 995),
		new ProfileEntry("O2 Online Deutschland", "pop.o2online.de", 995),
		new ProfileEntry("T-Online Deutschland", "securepop.t-online.de", 995),
		new ProfileEntry("1&1 (1and1)", "pop.1and1.com", 995),
		new ProfileEntry("1&1 Deutschland", "pop.1und1.de", 995),
		new ProfileEntry("smtp.comcast.net", "mail.comcast.net", 995),
		new ProfileEntry("Verizon", "incoming.verizon.net", 995),
		new ProfileEntry("Verizon hosted on Yahoo Server", "incoming.yahoo.verizon.net", 995)
	};
	
	public int getLength()
	{
		return profiles.length;
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
