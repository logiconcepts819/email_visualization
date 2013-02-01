// Taken from http://www.openprocessing.org/sketch/49814
class RuleLib
{
  private Rule [] rulz;
  public String start;
  RuleLib (Rule [] rulz, String start)
  {
    this.start = start;
    this.rulz = rulz;
  }
  
  public Rule [] getRulz ()
  {
    return rulz;
  }
  
  public void addRule (Rule r)
  {
    append (rulz, r);
  }
  
  public void removeRule ()
  {
    shorten (rulz);
  }
}

class Rule
{
  public String l, r; // l == lookup // r == replacement
  Rule (String l, String r)
  {
    this.r = r;
    this.l = l;
  }
}

