package com.fr;

import java.io.*;
import java.util.*;

public class Tokenize {

    public List<Token> getTokens(BufferedReader reader) throws IOException {
        String data;
        List<Token> tokens = new ArrayList<>();
        int cnt = 0;

        looper: while ((data = reader.readLine()) != null) {
            cnt++;
            int status = 0;
            data = data.split(";")[0].trim();

            for (int ptr = 0; ptr < data.length(); ptr++) {
                char x = data.charAt(ptr);
                switch (status) {
                    case 0 -> {
                        if (x == '[') {
                            status = 1;
                        } else if (x == ']' || x == '=') {
                            throw new IllegalArgumentException("词法分析：第" + cnt + "行出错\n");
                        } else {
                            var builder = new StringBuilder();
                            ptr = getString(builder, ptr, data);
                            tokens.add(new Token("key", builder.toString(), cnt));
                            ptr--;
                            status = 3;
                        }
                    }
                    case 1 -> {
                        if (x == '[' || x == '=') {
                            throw new IllegalArgumentException("词法分析：第" + cnt + "行出错\n");
                        } else if (x == ']') {
                            continue looper;
                        } else {
                            var builder = new StringBuilder();
                            ptr = getString(builder, ptr, data);
                            tokens.add(new Token("section", builder.toString(), cnt));
                            if (ptr >= data.length()) {
                                throw new IllegalArgumentException("词法分析：第" + cnt + "行出错\n");
                            }
                            ptr--;
                        }
                    }
                    case 2 -> {
                        if (x == '[' || x == '=' || x == ']') {
                            throw new IllegalArgumentException("词法分析：第" + cnt + "行出错\n");
                        } else {
                            var builder = new StringBuilder();
                            ptr = getString(builder, ptr, data);
                            tokens.add(new Token("value", builder.toString(), cnt));
                        }
                    }
                    case 3 -> {
                        if (x != '=') {
                            throw new IllegalArgumentException("词法分析：第" + cnt + "行出错\n");
                        } else {
                            status = 2;
                        }
                    }
                }
            }
        }
        reader.close();
        return tokens;
    }

    private int getString(StringBuilder builder, int ptr, String data) {
        while (ptr < data.length()) {
            char x = data.charAt(ptr);
            if (x == '[' || x == ']' || x == '=') {
                break;
            }
            if (x != ' ' && x != '\t') {
                builder.append(x);
            }
            ptr++;
        }
        return ptr;
    }
}