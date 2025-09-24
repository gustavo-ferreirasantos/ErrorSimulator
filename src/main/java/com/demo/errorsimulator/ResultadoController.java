package com.demo.errorsimulator;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ResultadoController {

    @FXML
    protected TextArea tResolucao;


    @FXML
    private void initialize() {
        // Adiciona listener para detectar quando a cena é exibida
        tResolucao.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        // A cena está sendo mostrada
                        onShow();
                    }
                });
            }
        });
    }

    private void onShow() {
        tResolucao.setText(DadosController.text);
    }

}
