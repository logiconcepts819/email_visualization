import java.util.HashMap;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class TreeMain extends PApplet {
  static int WIDTH = 640;
  static int HEIGHT = 640;

  private HashMap<String, AppController> controllers = new HashMap<String, AppController>();
  private String current_controller;
  private TreeController tree_controller;

  @Override
  public void setup() {
    size(WIDTH, HEIGHT);

    LoginController login_controller = new LoginController(this);
    tree_controller = new TreeController(this);

    controllers.put("login_controller", login_controller);
    controllers.put("tree_controller", tree_controller);
    current_controller = "login_controller";

    // email listener for the login controller
    EmailDownloadListener email_listener = new EmailDownloadListener() {
      @Override
      public void on_emails_downloaded(EmailCollection email_collection) {
        tree_controller.set_email_collection(email_collection);
        current_controller = "tree_controller";
      }

      @Override
      public void on_status(String status) {}
    };

    login_controller.set_download_thread_listener(email_listener);
  }

  @Override
  public void draw() {
    background(0);
    frameRate(30);
    controllers.get(current_controller).display();
  }
}

