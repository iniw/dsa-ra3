package org.puc;

public class Registro {
    int codigo;
    int valor;

    public Registro(int valor, int codigo) {
        this.codigo = codigo;
        this.valor = valor;
    }

    int valor() {
        return this.codigo;
    }

    int codigo() {
        return this.codigo;
    }
}
