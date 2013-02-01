// Taken from http://www.openprocessing.org/sketch/49814
//
//example:
//  Rule [] r = {
//    new Rule("F", "FF"), 
//    new Rule ("X", "F[+X]F[-X]+X")
//  };
//  RuleLib rl = new RuleLib (r, "X");
//  ls = new LSystem (rl, iNum);
//  t = new Tree (ls.getLSystem(), width/2, height/2+rad, 270, 20.0, 0.75, color (247));
//  eColor = color(178, 209, 209, 240);
//
//  And then in draw():
//  translate ((float) t.getStartX()/(float) width*(width - zoom*width), ((float) t.getStartY()/(float) height)*(height - zoom*height)); 
//  scale (zoom);
//  t.display();

class Tree
{
  private String input;
  private int startX, startY, angle0;
  private float angle, minSW;
  private ArrayList branches;
  private color cc;

  Tree (String input, int startX, int startY, int startAngle, float angle, float minSW, color cc)
  {
    //
    this.cc = cc;
    this.minSW = minSW;
    this.angle0 = startAngle;
    this.startX = startX;
    this.startY = startY;
    this.input = input;
    this.angle = angle;
    branches = new ArrayList();
    createBranches();
  }

  public float getAngle ()
  {
    return angle0;
  }

  public float getHight ()
  {
    float high = (float) (startY+cos (radians (angle0))*rad);
    Branch b;
    for (int i = 0; i < branches.size(); i++)
    {
      b = (Branch) branches.get (i);
      if (angle0 > 180 && b.getEndPointY() < high) high = b.getEndPointY();
      if (angle0 <= 180 && b.getEndPointY() > high) high = b.getEndPointY();
    } 
    return abs (startY - high);
  }

  public int getStartX ()
  {
    return startX;
  }

  public int getStartY ()
  {
    return startY;
  }

  public void update ()
  {
    branches.clear();
    createBranches();
  }

  public void display ()
  {   
    Branch b;

    for (int i = 0; i < branches.size(); i++)
    {
      b = (Branch) branches.get (i);
      b.display();
    }
  }

  private void createBranches ()
  {

    ArrayList savePoints = new ArrayList();
    float totalAngle = angle0, x = startX, y = (float) startY, l = 3.5, sw = 5.0, startAngle = totalAngle, oldAngle = startAngle, a = 255.0;

    for (int i = 0; i < input.length(); i++)
    {
      char c = input.charAt (i);


      switch (c)
      {
      case 'F':
        // Linie zeichnen
        float nextX = x + cos (radians (totalAngle)) * l;
        float nextY = y + sin (radians (totalAngle)) * l;

        if (branches.size() == 0 || totalAngle != oldAngle)
        {
          float [] [] points = new float [2] [2];
          points [0] [0] = x;
          points [0] [1] = y;
          points [1] [0] = nextX;
          points [1] [1] = nextY;

          branches.add (new Branch (points, sw, totalAngle, angle, a, cc));
          if (sw > 0.5 ) sw *= random (minSW, 0.9999);
          a *= random (0.8, 0.99999);
          if (a < 160) a = 160;
        }
        else
        {
          Branch b = (Branch) branches.get(branches.size()-1);
          b.updateEnd (nextX, nextY);
        }
        l*= random (0.75, 1.25);

        x = nextX;
        y = nextY;
        oldAngle = totalAngle;
        break;
      case '+':
        //rechts drehen
        totalAngle -= random (angle/1.5, angle*1.2);
        break;
      case '-':
        //links drehen
        totalAngle += random (angle/1.5, angle*1.2);
        break;
      case '[':
        // add save point
        savePoints.add (new SavePos (x, y, totalAngle, l, sw, a));
        break;
      case ']':
        if (savePoints.size() != 0)
        {
          SavePos p = (SavePos) savePoints.get (savePoints.size() > 0 ? savePoints.size()-1 : 0);
          x = p.x;
          y = p.y;
          totalAngle = p.angle;
          l = p.l;
          sw = p.sw;
          a = p.a;
          savePoints.remove(savePoints.size()-1);
        }
        // return to save point
        break;
      }
    }

    savePoints.clear();
  }
}

class Branch
{
  private float [] [] points;
  private float sw, angle, angleDist, a;
  private color c;
  Branch (float [] [] points, float sw, float angle, float angleDist, float a, color c)
  {
    this.c = c;
    this.a = a;
    this.angle = angle;
    this.angleDist = angleDist;
    this.sw = sw;
    this.points = new float [points.length] [2];
    arrayCopy (points, this.points);
    createMiddlePoints(1);
  }

  public float getEndPointX()
  {
    return points[points.length-1] [0];
  }

  public float getEndPointY()
  {
    return points[points.length-1] [1];
  }

  private void createMiddlePoints (int i)
  {
    float dis = dist ( points [0] [0], points [0] [1], points [points.length-1] [0], points [points.length-1] [1]);
    float mangle = random (-angleDist, angleDist);
    float m = random (0.25, 0.75);

    float [] [] temp = new float [points.length+i] [2];
    temp [0] [0] = points [0] [0];
    temp [0] [1] = points [0] [1];
    temp [1] [0] = temp [0] [0] + cos (radians (angle + mangle)) * dis * m;
    temp [1] [1] = temp [0] [1] + sin (radians (angle + mangle)) * dis * m;
    temp [temp.length-1] [0] = points [points.length-1] [0];
    temp [temp.length-1] [1] = points [points.length-1] [1];
    //println (temp [1] [0]);
    points = new float [temp.length] [2];
    arrayCopy (temp, points);
  }

  public void updateEnd (float x, float y)
  {
    points[points.length-1] [0] = x;
    points[points.length-1] [1] = y;
    createMiddlePoints(0);
  }

  public float [] [] getPoints ()
  {
    return points;
  }

  public color getColor ()
  {
    return color (red(c), green (c), blue (c), a);
  }

  public float getSW ()
  {
    return sw;
  }

  public void display ()
  {
    noFill();
    stroke (red(c), green (c), blue (c), a);
    strokeWeight (sw);
    beginShape();
    for (int i = 0; i < points.length; i++)
    {
      curveVertex (points[i] [0], points[i] [1]);
      if (i == 0 || i == points.length-1) curveVertex (points[i] [0], points[i] [1]);
    }
    endShape();
  }
}

class SavePos
{
  public float x, y, angle, l, sw, a;
  SavePos (float x, float y, float angle, float l, float sw, float a)
  {
    this.a = a;
    this.sw = sw;
    this.x = x;
    this.y = y;
    this.angle = angle;
    this.l = l;
  }
}

