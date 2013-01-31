import processing.core.PApplet;
import processing.core.PConstants;

import controlP5.Button;
import controlP5.Canvas;
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.ListBox;
import controlP5.Textfield;
import controlP5.Textlabel;

public class LoginController extends Canvas implements ControlListener {
  private ControlP5 controlP5 = null;
  private Textlabel title_lbl1, title_lbl2;
  private ListBox plist;
  private Textfield host, port, login, pass;
  private Button submit;
  private Textlabel status;
  private POP3Profiles prof;
  

  LoginController(ControlP5 controlP5) {
    this.controlP5 = controlP5;
  }
  public void setup(PApplet parent) {
    controlP5.addListener(this);
		
    ControlFont cf = new ControlFont(parent.createFont("Arial", 16));
    controlP5.setFont(cf);
		
    cf.setSize(72);
    title_lbl1 = controlP5.addTextlabel("tree", "TREE");
    cf.setSize(16);
    title_lbl2 = controlP5.addTextlabel("tree2", "TREE Representation of Everlasting Emails");
		
    int w1 = ControlFont.getWidthFor(title_lbl1.getValueLabel().getText(), title_lbl1.getValueLabel(), parent);
    int w2 = ControlFont.getWidthFor(title_lbl2.getValueLabel().getText(), title_lbl2.getValueLabel(), parent);
		
    title_lbl1.setPosition((parent.width - w1) / 2.0f, 20.0f);
    title_lbl2.setPosition((parent.width - w2) / 2.0f, 100.0f);
		
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
    submit.getCaptionLabel().align(PConstants.CENTER, PConstants.CENTER);
    submit.setSize(200, 70);
    submit.setPosition((parent.width - submit.getWidth()) / 2.0f, 440);
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

  public void draw(PApplet parent) {
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
        int iport = Integer.parseInt(port.getText());
        String suser = login.getText();
        String spass = pass.getText();
        (new EmailDownloadThread(shost, iport, suser, spass, status)).start();
      }
    }
  }
  
  //@Override
  //public void keyPressed()
  //{
    //// Unfortunately, controlP5 doesn't implement tab to navigate, so this
    //// method and the following method (keyReleased) along with the state
    //// variable 'shift_active' provide one workaround
    //if (keyCode == SHIFT)
    //{
      //shift_active = true;
    //}
    //else if (key == TAB)
    //{
      //if (host.isFocus())
      //{
        //pass.setFocus(shift_active);
        //host.setFocus(false);
        //port.setFocus(!shift_active);
      //}
      //else if (port.isFocus())
      //{
        //host.setFocus(shift_active);
        //port.setFocus(false);
        //login.setFocus(!shift_active);
      //}
      //else if (login.isFocus())
      //{
        //port.setFocus(shift_active);
        //login.setFocus(false);
        //pass.setFocus(!shift_active);
      //}
      //else
      //{
        //login.setFocus(shift_active);
        //pass.setFocus(false);
        //host.setFocus(!shift_active);
      //}
    //}
  //}
  
  //@Override
  //public void keyReleased()
  //{
    //if (keyCode == SHIFT)
    //{
      //shift_active = false;
    //}
  //}
}