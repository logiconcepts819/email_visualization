import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

import controlP5.*;

public class TreeController implements ControlListener {
  private PApplet parent;
  private ControlP5 controlP5;
  private EmailCollection email_collection;
  private Tree tree;

  TreeController(PApplet parent) {
    this.controlP5 = new ControlP5(parent);
    this.controlP5.addListener(this).setAutoDraw(false);
    this.parent = parent;
    setup();
  }

  public void setup() {

    //controlP5.mapKeyFor(new ControlKey() {
      //@Override
      //public void keyEvent() {
        //tab_focus(true);
      //}}, PConstants.TAB);

  }

  public void display() {
    controlP5.draw();
  }

  public void set_email_collection(EmailCollection email_collection) {
    this.email_collection = email_collection;
  }
}

