import processing.core.PApplet;

// Taken from http://www.openprocessing.org/sketch/49814
class RuleLib {
  private Rule [] rulz;
  public String start;
  RuleLib (Rule [] rulz, String start) {
    this.start = start;
    this.rulz = rulz;
  }
  
  public Rule [] getRulz() {
    return rulz;
  }
  
  public void addRule (Rule r) {
    PApplet.append (rulz, r);
  }
  
  public void removeRule() {
    PApplet.shorten (rulz);
  }
}

