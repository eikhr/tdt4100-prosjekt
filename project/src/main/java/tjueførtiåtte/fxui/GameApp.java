package tjueførtiåtte.fxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
		GameController controller = new GameController();
		loader.setController(controller);
		Parent parent = loader.load();
		primaryStage.setTitle("Tjueførtiåtte");
		primaryStage.setScene(new Scene(parent));
		primaryStage.minHeightProperty().set(630);
		primaryStage.minWidthProperty().set(480);
		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> controller.resizedUI());
		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> controller.resizedUI());
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(GameApp.class, args);
	}
}
