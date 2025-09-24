package com.demo.errorsimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static com.demo.errorsimulator.CalculadoraErros.calcular;

public class App extends Application {
    private static String n1, n2, operacao, metodo;
    private static int digitos;
    private static Stage stage;
    private static Scene login;
    private static Scene resolucao;

    public static void setValores(String n1, String n2, int digitos, String operacao, String metodo) {
        App.n1 = n1;
        App.n2 = n2;
        App.digitos = digitos;
        App.operacao = operacao;
        App.metodo = metodo;
    }


    @Override
    public void start(Stage primarystage) throws IOException {
        stage = primarystage;

        Parent loginP = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("FXML/login.fxml")));
        Parent resolucaoP = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("FXML/resolucao.fxml")));
        login = new Scene(loginP);
        resolucao = new Scene(resolucaoP);
        primarystage.setTitle("Hello!");
        primarystage.setScene(login);
        primarystage.show();
    }

    public static void UpdateScene(){
        stage.setScene(resolucao);
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        launch();

        System.out.println("\nRodando Exemplo 2 (Cancelamento Subtrativo):");
        calcular(n1, n2, operacao, digitos, metodo);


        sc.close();
    }
}