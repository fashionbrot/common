package com.github.fashionbrot.common;

import java.io.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextToProperties {


    public static void main(String[] args) {
        String inputText = "placeholder=abc \n" +
                "key1=value1\nkey2=${placeholder}\nkey3=Hello, ${name}!";
        Properties properties = parseTextToProperties(inputText);

        // Print the properties
        properties.forEach((key, value) -> System.out.println(key + "=" + value));
    }

    public static Properties parseTextToProperties(String inputText) {
        Properties properties = new Properties();
        Pattern placeholderPattern = Pattern.compile("\\$\\{([^}]+)\\}");

        String[] lines = inputText.split("\\r?\\n");
        for (String line : lines) {
            String[] keyValue = line.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                Matcher matcher = placeholderPattern.matcher(value);
                while (matcher.find()) {
                    String placeholder = matcher.group(1);
                    String replacement = System.getProperty(placeholder, ""); // You can provide a default value if needed
                    value = value.replace("${" + placeholder + "}", replacement);
                }
                properties.setProperty(key, value);
            }
        }
        return properties;
    }
}
