import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RsaApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/rsaView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 711);

        stage.setTitle("RSA Danny & Jerry");
        stage.setScene(scene);
        stage.show();
    }
}
