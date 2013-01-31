import processing.core.PApplet;

@SuppressWarnings("serial")
public class TreeMain extends PApplet {
  static int WIDTH = 640;
  static int HEIGHT = 640;

  private LoginController login_controller;

  @Override
    public void setup() {
      size(WIDTH, HEIGHT);

      login_controller = new LoginController(this);
    }

  @Override
    public void draw() {
      background(0);
      frameRate(30);
      login_controller.display();
    }

  //void branch(float h)
  //{
  //// Each branch will be 2/3rds the size of the previous one
  //float new_h = h * 0.66f;

  //// All recursive functions must have an exit condition!!!!
  //// Here, ours is when the length of the branch is 2 pixels or less
  //if (new_h > 2)
  //{
  //pushMatrix();           // Save the current state of transformation (i.e. where are we now)
  //rotate(theta);          // Rotate by theta
  //line(0, 0, 0, -new_h);  // Draw the branch
  //translate(0, -new_h);   // Move to the end of the branch
  //branch(new_h);          // Ok, now call myself to draw two new branches!!
  //popMatrix();            // Whenever we get back here, we "pop" in order to restore the previous matrix state

  //// Repeat the same thing, only branch off to the "left" this time!
  //pushMatrix();
  //rotate(-theta);
  //line(0, 0, 0, -new_h);
  //translate(0, -new_h);
  //branch(new_h);
  //popMatrix();
  //}
  //}
}
