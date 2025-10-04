package com.demo.errorsimulator;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.math.BigDecimal;

public class ResultadoController {

    @FXML
    protected Button btNovoCalculo;

    @FXML
    protected TableView<String[]> tabela;
    @FXML
    protected TableColumn<String[], String> c1;
    @FXML
    protected TableColumn<String[], String> c2;

    @FXML
    private void initialize() {


        // Adiciona listener para detectar quando a cena é exibida
        tabela.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        // Configura como as colunas pegam os dados do String[]
                        c1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
                        c2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
                        // A cena está sendo mostrada
                        onShow();
                    }
                });
            }
        });
    }

    private void onShow() {
        tabela.getItems().clear();
        tabela.getItems().addAll(DadosController.getText());
    }

    public void novoCalculo() {
        StartApplication.updateScene();
    }
}
