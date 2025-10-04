package com.demo.errorsimulator;
import static com.demo.errorsimulator.CalculadoraErros.calcular;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class DadosController {

    protected static List<String[]> text;

    @FXML
    protected TextField tfN1, tfN2, tfDigitos;


    @FXML
    protected Button btSoma, btSomaS, btSubtracao, btSubtracaoS, btMultiplicacao, btDivisao, btResultado;

    @FXML //Caixa pra selecionar o metodo (arredondamento ou truncamento)
    protected ChoiceBox<String> cbMetodos;
    //Armazena os tipos de métodos
    private final String[] metodos = {"Todos", "Arredondamento", "Truncamento"};


    private String ultimaOperacao = "";

    @FXML
    protected void initialize() {
        // Configura listeners para os botões
        btSoma.setOnAction(e -> ultimaOperacao = "+");
        btSomaS.setOnAction(e -> ultimaOperacao = "++");
        btSubtracao.setOnAction(e -> ultimaOperacao = "-");
        btSubtracaoS.setOnAction(e -> ultimaOperacao = "--");
        btMultiplicacao.setOnAction(e -> ultimaOperacao = "*");
        btDivisao.setOnAction(e -> ultimaOperacao = "/");

        // Inicializa ChoiceBox
        cbMetodos.getItems().addAll(metodos);
        cbMetodos.setValue(metodos[0]); // valor padrão
    }

    @FXML
    protected void send() {
        text = calcular(tfN1.getText(), tfN2.getText(), ultimaOperacao, Integer.parseInt(tfDigitos.getText()), cbMetodos.getValue());
        StartApplication.updateScene();
    }

    public static List<String[]> getText() {
        return text;
    }
}