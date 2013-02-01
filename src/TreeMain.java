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
}

