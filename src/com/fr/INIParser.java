package com.fr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class INIParser {
    public class Key_Value {
        public String key;
        public List<String> values;

        public Key_Value(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();
            for (var value: values) {
                builder.append("Key: " + key + "\tValue: " + value + "\n");
            }
            return builder.toString();
        }
    }

    private Map<String, List<Key_Value>> map; //<section, key-value>

    public INIParser() {
        map = new HashMap<>();
    }

    public List<String> getValue(String section, String key) {

        if (map.containsKey(section)) {
            var kvs = map.get(section);
            for (var kv: kvs) {
                if (kv.key.equals(key)) {
                    return kv.values;
                }
            }
            System.out.println("没有key: " + key);
            return null;
        } else {
            System.out.println("没有section: " + section);
            return null;
        }
    }

    public void putValue(String section, String key, String value) {
        if (map.containsKey(section)) {
            var kvs = map.get(section);
            boolean status = false;
            for (var kv: kvs) {
                if (kv.key.equals(key)) {
                    status = true;
                    kv.values.add(value);
                }
            }
            if (!status) {
                var kv = new Key_Value(key);
                kv.values = new ArrayList<>();
                kv.values.add(value);
                kvs.add(kv);
            }
        } else {
            var kv = new Key_Value(key);
            kv.values = new ArrayList<>();
            kv.values.add(value);
            List<Key_Value> kvs = new ArrayList<>();
            kvs.add(kv);
            map.put(section, kvs);
        }
    }

    public List<Key_Value> getSection(String section) {
        return map.get(section);
    }

    public void load(BufferedReader reader) throws IOException, IllegalArgumentException {
        String data = null;
        String default_section = "main";
        int cnt = 0;

        read: while ((data = reader.readLine()) != null) {
            cnt++;
            int status = 0;
            var sectionBuilder = new StringBuilder();
            var keyBuilder = new StringBuilder();
            var valueBuilder = new StringBuilder();

            parser: for (int i = 0; i < data.length(); i++) {
                char x = data.charAt(i);

                if (x == ';') {
                    continue read;
                }

                switch (status) {
                    case 0:
                        if (x == '[') {
                            status = 1;
                        } else if (x == '=') {
                            break parser;
                        } else {
                            status = 2;
                            keyBuilder.append(x);
                        }
                        break;
                    case 1:
                        if (x != ']') {
                            sectionBuilder.append(x);
                        } else {
                            status = 0;
                        }
                        break;
                    case 2:
                        if (x != '=') {
                            keyBuilder.append(x);
                        } else {
                            status = 3;
                        }
                        break;
                    case 3:
                        valueBuilder.append(x);
                        break;
                }
            }

            if (status != 0 && status != 3) {
                throw new IllegalArgumentException("第 " + cnt + " 行出错");
            }

            if (!sectionBuilder.isEmpty()) {
                default_section = sectionBuilder.toString();
            }
            if (map.containsKey(default_section)) {
                var kvs = map.get(default_section);
                boolean has = false;
                for (var kv: kvs) {
                    if (kv.key == keyBuilder.toString()) {
                        kv.values.add(valueBuilder.toString());
                        has = true;
                    }
                }
                if (!has && !keyBuilder.isEmpty()) {
                    var kv = new Key_Value(keyBuilder.toString());
                    kv.values = new ArrayList<>();
                    kv.values.add(valueBuilder.toString());
                    kvs.add(kv);
                }
            } else {
                List<Key_Value> kvs = new ArrayList<>();
                if (!keyBuilder.isEmpty()) {
                    var kv = new Key_Value(keyBuilder.toString());
                    kv.values = new ArrayList<>();
                    kv.values.add(valueBuilder.toString());
                    kvs.add(kv);
                }
                map.put(default_section, kvs);
            }
        }
        reader.close();
    }

    public void write(BufferedWriter writer) throws IOException {
        var builder = new StringBuilder();
        for (var entry: map.entrySet()) {
            builder.append("[" + entry.getKey() + "]\n");
            for (var kv: entry.getValue()) {
                for (var ca: kv.values) {
                    builder.append(kv.key + "=" + ca + "\n");
                }
            }
        }
        writer.write(builder.deleteCharAt(builder.length() - 1).toString());
        writer.flush();
        writer.close();
    }
}