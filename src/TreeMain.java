import java.io.IOException;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class TreeMain extends PApplet {
  static int WIDTH = 640;
  static int HEIGHT = 640;

  private LoginController login_controller;

  @Override
  public void setup() {
    size(WIDTH, HEIGHT);

    EmailDownloadListener email_listener = new EmailDownloadListener() {
      @Override
      public void on_emails_downloaded(EmailCollection email_collection) {
        // TODO analyze emails here
        System.out.println("FUCK YEAH");
      }

      @Override
      public void on_status(String status) {}
    };

    login_controller = new LoginController(this);
    login_controller.set_download_thread_listener(email_listener);
  }

  @Override
  public void draw() {
    background(0);
    frameRate(30);
    login_controller.display();
  }
}

