package com.demo.errorsimulator;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CalculadoraErros {

    // Converte string para BigDecimal
    public static BigDecimal tranformarNumero(String entrada) {
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
    public static BigDecimal operar(BigDecimal x, BigDecimal y, String op, int n, String metodo) {
        // Se a operação for inválida, lança exceção
        return switch (op) {
            case "+" -> x.add(y);          // Soma
            case "-" -> x.subtract(y);     // Subtração
            case "*" -> x.multiply(y);     // Multiplicação
            case "/" -> {
                // Cria a configuração de arredondamento para divisão
                MathContext mc = new MathContext(
                        n,
                        metodo.equalsIgnoreCase("truncamento") ? RoundingMode.DOWN : RoundingMode.HALF_UP
                );
                // Realiza a divisão com precisão controlada
                yield x.divide(y, mc);
                // Realiza a divisão com precisão controlada
            }
            // Gera uma exceção caso não seja apertado nenhum dos botões de operações(+, -, *, /)
            default -> throw new IllegalArgumentException("Operação inválida: " + op);
        };
    }


    // Formata o número em notação entre 0 e 1
    public static String formatarNumero(BigDecimal valor, int n) {
        // Caso seja zero, já retorna o zero.
        if (valor.compareTo(BigDecimal.ZERO) == 0) return "0";

        // Calcula o expoente da notação científica:
        // precision() → número total de dígitos;
        // scale() → casas decimais;
        // expoente = dígitos antes do ponto - 1.
        int expoente = valor.precision() - valor.scale() - 1;

        // Normaliza o número para que a mantissa fique entre (0,1)
        BigDecimal mantissa = valor.movePointLeft(expoente).divide(BigDecimal.TEN);
        expoente += 1;

        // Trunca a mantissa para n dígitos significativos
        mantissa = ajustarPrecisao(mantissa, n, "truncamento");

        // Retorna mantissa × 10^expoente
        return mantissa.stripTrailingZeros().toPlainString() + " × 10^" + expoente;
    }


    // Calcula e exibe resultado da operação
    public static String calcular(String strX, String strY, String op, int n, String metodo) {
        // Converte entradas de string para BigDecimal
        BigDecimal x = tranformarNumero(strX);
        BigDecimal y = tranformarNumero(strY);

        // Calcula o valor exato da operação
        BigDecimal valorExato = operar(x, y, op, n, metodo);

        // Aplica arredondamento nos operandos antes da operação
        BigDecimal xAprox = ajustarPrecisao(x, n, metodo);
        BigDecimal yAprox = ajustarPrecisao(y, n, metodo);

        // Calcula valor aproximado da operação (operandos já arredondados)
        BigDecimal valorAprox = ajustarPrecisao(operar(xAprox, yAprox, op, n, metodo), n, metodo);

        // Calcula erro absoluto = |valorExato - valorAprox|
        BigDecimal erroAbs = valorExato.subtract(valorAprox).abs();

        // Calcula erro relativo = erroAbs / valorAprox
        // Se valorAprox == 0, evita divisão por zero usando BigDecimal.ONE
        BigDecimal erroRel = erroAbs.divide(
                valorAprox.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : valorAprox,
                new MathContext(n, RoundingMode.HALF_UP)
        );

        // Retorna relatório formatado com todos os resultados
        return "Operação: " + formatarNumero(xAprox, n) + " " + op + " " + formatarNumero(yAprox, n)
                + "\nMétodo: " + metodo + " | "
                + "\nDígitos: " + n
                + "\nValor Exato: " + formatarNumero(valorExato, n)
                + "\nValor Aprox: " + formatarNumero(valorAprox, n)
                + "\nErro Absoluto: " + formatarNumero(erroAbs, n)
                + "\nErro Relativo: " + formatarNumero(erroRel, n);
    }





}
