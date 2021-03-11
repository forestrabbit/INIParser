package com.fr;

public class Token {
    public final String type;
    public final String value;
    public final int cnt;

    public Token(String type, String value, int cnt) {
        this.type = type;
        this.value = value;
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "Type: " + type + " Value: " + value;
    }
}