package com.fr;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(new Parser(
                new Tokenize().getTokens(
                        new BufferedReader(new InputStreamReader(new FileInputStream("setting.ini")))
                )
        ).parse());
    }
}