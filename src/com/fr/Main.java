package com.fr;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        INIParser iniParser = new INIParser();
        try {
            iniParser.putValue("com.fr.Student", "age", "20");
            iniParser.putValue("com.fr.Student", "name", "forestrabbit");
            iniParser.write(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("setting.ini"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}