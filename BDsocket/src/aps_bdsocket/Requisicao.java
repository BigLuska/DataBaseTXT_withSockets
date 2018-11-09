package aps_bdsocket;

import java.io.Serializable;

public class Requisicao implements Serializable {

    private String text1, text2;
    private String op;

    public Requisicao(String text1, String text2, String operacao) {
        this.text1 = text1;
        this.text2 = text2;
        this.op = operacao;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String txt1) {
        this.text1 = txt1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String txt2) {
        this.text2 = txt2;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String operacao) {
        this.op = operacao;
    }

}