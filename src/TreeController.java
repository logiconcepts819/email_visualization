import processing.core.PApplet;
import controlP5.*;

public class TreeController implements AppController {
  private PApplet parent;
  private ControlP5 controlP5;
  private EmailCollection email_collection;
  private Tree tree;
  private int width = 640;
  private int height = 640;
  private int zoom = 2;
  private int iterations = 7;

  TreeController(PApplet parent) {
    this.controlP5 = new ControlP5(parent);
    this.controlP5.addListener(this).setAutoDraw(false);
    this.parent = parent;
    setup();
  }

  public void setup() {
    Rule[] rules = {
      new Rule("F", "FF"), 
      new Rule ("X", "F[+X]F[-X]+X")
    };
    RuleLib rule_library = new RuleLib (rules, "X");
    LSystem l_system = new LSystem (rule_library, iterations);
    tree = new Tree (parent, l_system.getLSystem(), width/2, height/2 + Tree.rad, 270, 20.0f, 0.75f, 247);
    //eColor = color(178, 209, 209, 240);

    //controlP5.mapKeyFor(new ControlKey() {
    //@Override
    //public void keyEvent() {
    //tab_focus(true);
    //}}, PConstants.TAB);

  }

  public void display() {

    parent.translate((float) tree.getStartX()/(float) width*(width - zoom*width), ((float) tree.getStartY()/(float) height)*(height - zoom*height)); 
    parent.scale(zoom);
    tree.display();

  }

  public void set_email_collection(EmailCollection email_collection) {
    this.email_collection = email_collection;
  }

  @Override
  public void controlEvent(ControlEvent arg0) {
    // TODO Auto-generated method stub

  }
}

