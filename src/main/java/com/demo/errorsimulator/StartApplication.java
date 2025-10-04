package com.demo.errorsimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StartApplication extends Application {
    private static Stage stage;
    private static Scene dadosScene;
    private static Scene resolucaoScene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        // -------- Telas -----------
        Parent fxml1 = FXMLLoader.load(Objects.requireNonNull(StartApplication.class.getResource("FXML/Dados.fxml")));
        Parent fxml2 = FXMLLoader.load(Objects.requireNonNull(StartApplication.class.getResource("FXML/Resultado.fxml")));
        dadosScene = new Scene(fxml1);
        resolucaoScene = new Scene(fxml2);
        primaryStage.setScene(dadosScene);

        // -------- ICON -----------
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Images/icon.png")));
        primaryStage.getIcons().add(icon);

        primaryStage.show();
    }

    public static void updateScene() {
        if (stage.getScene() == resolucaoScene) {
            stage.setScene(dadosScene);
        } else {
            stage.setScene(resolucaoScene);
        }
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();

        //  9370    12,72
        //  0.76545     0.76541
        // 0.56786
    }
}