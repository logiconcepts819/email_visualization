import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

import controlP5.*;

public class LoginController implements ControlListener {
  private static final String FONTRESOURCE = "/uhvb8a.pfb"; 

  private PApplet parent;
  private ControlP5 controlP5;
  private Textlabel title_lbl1, title_lbl2;
  private ListBox plist;
  private RadioButton ptype;
  private Textfield host, port, login, pass;
  private Button submit;
  private Textlabel status;
  private ServiceProfiles prof;
  private boolean change_port;

  LoginController(PApplet parent) {
    this.controlP5 = new ControlP5(parent);
    this.controlP5.addListener(this).setAutoDraw(false);
    this.parent = parent;
    setup();
  }

  public void setup() {
    //TODO refactor / cleanup this method
    change_port = true; // change the port by default
    ControlFont cf = new ControlFont(getCP5PFont());
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
    controlP5.addTextlabel("plisttype", "Protocol:", 20, 250);
    controlP5.addTextlabel("hostlabel", "Hostname:", 20, 300);
    controlP5.addTextlabel("portlabel", "Port:", 20, 350);
    controlP5.addTextlabel("loginlabel", "Username:", 20, 400);
    controlP5.addTextlabel("passwordlabel", "Password: ", 20, 450);

    plist = controlP5.addListBox("profilelist", 120, 150, 490, 80);
    ptype = controlP5.addRadio("radioimap", 120, 255);
    host = controlP5.addTextfield("hostbox", 120, 290, 490, 40);
    port = controlP5.addTextfield("portbox", 120, 340, 490, 40);
    login = controlP5.addTextfield("loginbox", 120, 390, 490, 40);
    pass = controlP5.addTextfield("passwordbox", 120, 440, 490, 40);
    cf.setSize(36);
    submit = controlP5.addButton("submitbtn");
    cf.setSize(16);
    pass.setPasswordMode(true);

    ptype.setItemsPerRow(4);
    ptype.setSpacingColumn(122);
    ptype.addItem("IMAP", 0);
    ptype.addItem("IMAPS", 1);
    ptype.addItem("POP3", 2);
    ptype.addItem("POP3S", 3);
    ptype.activate(1);
    ptype.setNoneSelectedAllowed(false);

    submit.setCaptionLabel("Login");
    submit.getCaptionLabel().align(PConstants.CENTER, PConstants.CENTER);
    submit.setSize(200, 70);
    submit.setPosition((parent.width - submit.getWidth()) / 2.0f, 490);
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

    prof = new ServiceProfiles();
    plist.hideBar();
    plist.addItem("-- Select an item --", 0);
    for (int i = 0; i < prof.getLength(); i++)
    {
      plist.addItem(prof.nameAt(i), i + 1);
    }

    controlP5.mapKeyFor(new ControlKey() {
      @Override
      public void keyEvent() {
        tab_focus(true);
      }}, PConstants.TAB);

    controlP5.mapKeyFor(new ControlKey() {
      @Override
      public void keyEvent() {
        tab_focus(false);
      }}, PConstants.SHIFT, PConstants.TAB);

    status = controlP5.addTextlabel("statusbar", "Ready", 10, 580);
  }

  public void display() {
    controlP5.draw();
  }

  private Font getCP5Font() {
    InputStream input = getClass().getResourceAsStream(FONTRESOURCE);
    Font font;
    try {
      font = Font.createFont(Font.TYPE1_FONT, input);
    } catch (FontFormatException | IOException e) {
      font = new Font("Arial", Font.BOLD, 16);
      e.printStackTrace();
    }
    return font;
  }

  private PFont getCP5PFont() {
    return new PFont(getCP5Font(), true);
  }

  // forward = true means tab forward
  // forward = false means tab backward
  private void tab_focus(boolean forward) {
    Textfield[] text_fields = {host, port, login, pass};
    // to tell if we should set the first item in focus
    boolean text_field_not_found = true;

    for (int i = 0; i < text_fields.length; i++) {
      Textfield text_field = text_fields[i];
      if (text_field.isFocus()) {
        text_field.setFocus(false);
        // TODO refactor idx
        int idx = (text_fields.length + i + (forward ? 1 : -1)) % text_fields.length;
        text_fields[idx].setFocus(true);
        text_field_not_found = false;
        break;
      }
    }

    if (text_field_not_found) {
      text_fields[0].setFocus(true);
    }
  }

  @Override
  public void controlEvent(ControlEvent theEvent) {
    if (theEvent.isGroup())
    {
      if (theEvent.getGroup().equals(plist))
      {
        int idx = (int) plist.getValue();
        if (idx != 0)
        {
          idx--;
          change_port = false;
          //TODO refactor this if else into a method
          if (prof.protoAt(idx).equals("imap")) {
            ptype.activate(0);
          }
          else if (prof.protoAt(idx).equals("pop3")) {
            ptype.activate(2);
          }
          else if (prof.protoAt(idx).equals("pop3s")) {
            ptype.activate(3);
          }
          else {
            ptype.activate(1);
          }
          host.setText(prof.hostnameAt(idx));
          port.setText(String.valueOf(prof.portAt(idx)));

          if (login.getText().isEmpty() || pass.getText().isEmpty()) {
            host.setFocus(false);
            port.setFocus(false);
            login.setFocus(login.getText().isEmpty());
            pass.setFocus(!login.getText().isEmpty());
          }
        }
      }
      else if (theEvent.getGroup().equals(ptype)) {
        if (change_port) {
          port.setText(ptype.getState(0) ? "143" :
              ptype.getState(2) ? "110" :
              ptype.getState(3) ? "995" : "993");
        }
        else {
          change_port = true;
        }
      }
    }
    else if (theEvent.isController()) {
      if (theEvent.getController().equals(submit)) {
        String sproto;
        // TODO refactor this if/else into a method
        if (ptype.getState(0)) {
          sproto = "imap";
        }
        else if (ptype.getState(2)) {
          sproto = "pop3";
        }
        else if (ptype.getState(3)) {
          sproto = "pop3s";
        }
        else {
          sproto = "imaps";
        }
        String shost = host.getText();
        int iport = Integer.parseInt(port.getText());
        String suser = login.getText();
        String spass = pass.getText();
        (new EmailDownloadThread(sproto, shost, iport, suser, spass, status)).start();
      }
    }
  }
}

