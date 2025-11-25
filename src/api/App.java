package api;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(12);
        root.setVgap(12);
        root.setAlignment(Pos.CENTER);

        Label codeLbl = new Label("Car Code:");
        TextField codeField = new TextField();

        Label brandLbl = new Label("Brand:");
        TextField brandField = new TextField();

        Label modelLbl = new Label("Model:");
        TextField modelField = new TextField();

        Label yearLbl = new Label("Year:");
        TextField yearField = new TextField();

        Button saveBtn = new Button("Save");
        Button clearBtn = new Button("Clear");

        root.add(codeLbl, 0, 0);
        root.add(codeField, 1, 0);

        root.add(brandLbl, 0, 1);
        root.add(brandField, 1, 1);

        root.add(modelLbl, 0, 2);
        root.add(modelField, 1, 2);

        root.add(yearLbl, 0, 3);
        root.add(yearField, 1, 3);

        HBox buttons = new HBox(10, saveBtn, clearBtn);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        root.add(buttons, 1, 4);

        Scene scene = new Scene(root, 700, 300);
        stage.setTitle("Car Form Example");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
