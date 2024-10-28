package org.puc;

public class Entry {
    private int code;
    private int value;

    public Entry(int code, int value) {
        this.code = code;
        this.value = value;
    }

    int code() {
        return this.code;
    }

    int value() {
        return this.value;
    }
}
