package com.demo.errorsimulator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraErros {

    // Converte string para BigDecimal
    public static BigDecimal transformarNumero(String entrada) {
        // Verifica se a entrada é nula.
        if (entrada == null) throw new IllegalArgumentException("Entrada nula");

        // Remove todos os espaços em branco e substitui vírgula por ponto (1,23, para 1.23).
        entrada = entrada.replaceAll("\\s+", "").replace(',', '.');

        // Após verificar se a string não é nula, ou retirar espaço e substituir as vírgulas. É criado um BigDecimal, que foi escolhido nesse projeto no lugar de um float ou double devido aos ruídos (0,76545 - 0,76541 = 0,0000399999999..., devendo ser 0,00004).
        return new BigDecimal(entrada);
    }


    // Ajusta valor para n dígitos significativos
    public static BigDecimal ajustarPrecisao(BigDecimal valor, int n, String metodo) {
        // Se o valor é zero, retorna o zero sem arredondamento ou truncamento.
        if (valor.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        // MathContext define como as operações numéricas com BigDecimal devem ser feitas, controlando: Precisão e Modo de arredondamento (modo). Se o metodo recebido como parâmetro for truncamento utiliza RoundingMode.DOWN(Corta os dígitos excedentes, sem arredondar), não sendo, que é o caso do arredondamento, utiliza RoundingMode.HALF_UP(Se o dígito seguinte for ≥ 5, aumenta o atual em mais um.)
        MathContext mc = new MathContext(n, metodo.equalsIgnoreCase("truncamento") ? RoundingMode.DOWN : RoundingMode.HALF_UP);

        // Aplica o arredondamento ao valor
        return valor.round(mc);
    }


    // Operações aritméticas com BigDecimal
    public static BigDecimal operar(BigDecimal x, BigDecimal y, String op, int n, String metodo){
        switch (op) {
            case "+" -> {
                return x.add(y);          // Soma
            }
            case "-" -> {
                return x.subtract(y);     // Subtração
            }
            case "++" -> {                 // SomaSucessiva
                BigDecimal resultado = BigDecimal.ZERO;
                for (int i = 0; i < y.intValue(); i++) {
                    resultado = resultado.add(x);
                    resultado = ajustarPrecisao(resultado, n, metodo);
                }
                return resultado;
            }
            case "--" -> {
                BigDecimal resultado = BigDecimal.ZERO;
                for (int i = 0; i < y.intValue(); i++) {
                    resultado = resultado.subtract(x);
                    resultado = ajustarPrecisao(resultado, n, metodo);
                }
                return resultado;   // SubtraçãoSucessiva
            }
            case "*" -> {
                return x.multiply(y);     // Multiplicação
            }
            case "/" -> {
                return x.divide(y, MathContext.DECIMAL128);       // Divisão
            }
            // Gera uma exceção caso não seja apertado nenhum dos botões de operações(+, ++, -, --, *, /)
            default -> throw new IllegalArgumentException("Operação inválida: " + op);
        }
    }


    // Formata o número em notação entre 0 e 1
    public static String formatarNumero(BigDecimal valor, int n, String metodo, int modo) {
        // Caso seja zero, já retorna o zero.
        if (valor.compareTo(BigDecimal.ZERO) == 0) return "0";

        // Calcula o expoente da notação científica:
        // precision() → número total de dígitos;
        // scale() → casas decimais;
        // expoente = dígitos antes do ponto - 1.
        int expoente = valor.precision() - valor.scale() - 1;

        // Normaliza o número para que a mantissa fique entre (0,1)
        BigDecimal mantissa;
        if(modo == 0){
            mantissa = valor.movePointLeft(expoente).divide(BigDecimal.TEN, MathContext.DECIMAL128);
        }else{
            mantissa = valor.movePointLeft(expoente).multiply(new BigDecimal(10));
        }
        expoente += 1;
        //mantissa = ajustarPrecisao(mantissa, n, metodo);

        // Retorna mantissa × 10^expoente
        return mantissa.stripTrailingZeros().toPlainString() + " × 10^" + expoente;
    }





    // Calcula e exibe resultado da operação
    public static List<String[]> calcular(String strX, String strY, String op, int n, String metodo) {
        // Converte entradas de string para BigDecimal
        BigDecimal x = transformarNumero(strX);
        BigDecimal y = transformarNumero(strY);

        // Calcula valor exato da operação
        BigDecimal valorExato;
        if(op.equals("++") || op.equals("--")){
            if (op.equals("++")){
                valorExato = operar(x, y, "*", n, metodo);
            }else{
                valorExato = operar(x, y, "/", n, metodo);
            }
        }else{
            valorExato = operar(x, y, op, n, metodo);
        }

        // Lista final
        List<String[]> resultado = new ArrayList<>();
        resultado.add(new String[]{"Operação", strX + " " + op + " " + strY});
        resultado.add(new String[]{"Método", metodo});
        resultado.add(new String[]{"Dígitos", String.valueOf(n)});
        resultado.add(new String[]{"Valor Exato", formatarNumero(valorExato, n, metodo, 0)});
        resultado.add(new String[]{null, null});    // linha vazia

        switch (metodo) {
            case "Arredondamento" -> {
                resultado.add(new String[]{"Arredondamento:", null});
                resultado.addAll(calcularPorMetodo(x, y, valorExato, op, n, "arredondamento"));
            }
            case "Truncamento" -> {
                resultado.add(new String[]{"Truncamento:", null});
                resultado.addAll(calcularPorMetodo(x, y, valorExato, op, n, "truncamento"));
            }
            case "Todos" -> {
                resultado.add(new String[]{"Arredondamento:", null});
                resultado.addAll(calcularPorMetodo(x, y, valorExato, op, n, "arredondamento"));

                resultado.add(new String[]{null, null});    // linha vazia
                resultado.add(new String[]{"Truncamento:", null});
                resultado.addAll(calcularPorMetodo(x, y, valorExato, op, n, "truncamento"));
            }
            default -> throw new IllegalArgumentException("Método inválido: " + metodo);
        }

        return resultado;
    }

    // Método auxiliar para evitar duplicação
    private static List<String[]> calcularPorMetodo(BigDecimal x, BigDecimal y, BigDecimal valorExato, String op, int n, String metodo) {
        List<String[]> resultado = new ArrayList<>();

        BigDecimal xAprox = ajustarPrecisao(x, n, metodo);
        BigDecimal yAprox = ajustarPrecisao(y, n, metodo);

        BigDecimal valorAprox = ajustarPrecisao(operar(xAprox, yAprox, op, n, metodo), n, metodo);
        BigDecimal erroAbs = valorExato.subtract(valorAprox).abs();
        BigDecimal erroRel;
        if (valorAprox.compareTo(BigDecimal.ZERO) == 0) {
            erroRel = null;
        }else{
            erroRel = ajustarPrecisao(erroAbs.divide(valorAprox.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : valorAprox, MathContext.DECIMAL128), n, metodo);
        }

        resultado.add(new String[]{"Valor Aprox", formatarNumero(valorAprox, n, metodo, 0)});
        resultado.add(new String[]{"Erro Absoluto", formatarNumero(erroAbs, n, metodo, 0)});
        resultado.add(new String[]{"Erro Relativo", erroRel == null ? "Divisão por zero!" : formatarNumero(erroRel, n, metodo, 1) + "%"});

        return resultado;
    }



}
