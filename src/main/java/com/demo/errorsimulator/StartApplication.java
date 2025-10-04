package com.demo.errorsimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StartApplication extends Application {
    private static Stage stage;
    private static Scene dadosScene;
    private static Scene resolucaoScene;

    @Override
    public void start(Stage primarystage) throws IOException {
        stage = primarystage;

        Parent fxml1 = FXMLLoader.load(Objects.requireNonNull(StartApplication.class.getResource("FXML/Dados.fxml")));
        Parent fxml2 = FXMLLoader.load(Objects.requireNonNull(StartApplication.class.getResource("FXML/Resultado.fxml")));
        dadosScene = new Scene(fxml1);
        resolucaoScene = new Scene(fxml2);
        primarystage.setScene(dadosScene);
        primarystage.show();
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