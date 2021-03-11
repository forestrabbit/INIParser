package com.fr;

import java.util.*;

public class Parser {
    private final List<Token> tokens;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Map<Token, List<KeyValue>> parse() {
        List<KeyValue> kvs = new ArrayList<>();
        Map<Token, List<KeyValue>> tree = new HashMap<>();
        Token section = new Token("section", "default", 1);

        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).type.equals("section")) {
                tree.put(section, kvs);
                kvs = new ArrayList<>();
                section = tokens.get(i);
            } else {
                KeyValue kv = new KeyValue();
                kv.key = tokens.get(i);
                kv.value = tokens.get(i + 1);
                if (!kv.value.type.equals("value")) {
                    throw new IllegalArgumentException("语法分析：第" + kv.value.cnt + "行出错\n");
                } else {
                    i++;
                    kvs.add(kv);
                }
            }
        }
        tree.put(section, kvs);
        return tree;
    }
}