package com.github.fashionbrot.common;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringToPropertiesWithPlaceholders {

    public static void main(String[] args) {
        String inputString = "abc=aaa\n" +
                "ccc=ddd\n" +
                "ddd=${abc}${ccc}";
        Properties properties = stringToProperties(inputString);

        // Print the properties
        properties.forEach((key, value) -> System.out.println(key + "=" + value));
    }

    public static Properties stringToProperties(String inputString) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(inputString));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern placeholderPattern = Pattern.compile("\\$\\{([^}]+)\\}");

        String[] lines = inputString.split("\\r?\\n");
        for (String line : lines) {
            String[] keyValue = line.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                Matcher matcher = placeholderPattern.matcher(value);
                StringBuffer resolvedValue = new StringBuffer();
                while (matcher.find()) {
                    String placeholder = matcher.group(1);
                    String replacement = properties.getProperty(placeholder, ""); // You can provide a default value if needed
                    matcher.appendReplacement(resolvedValue, replacement);
                }
                matcher.appendTail(resolvedValue);
                properties.setProperty(key, resolvedValue.toString());
            }
        }
        return properties;
    }
}
