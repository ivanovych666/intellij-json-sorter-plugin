package com.ivanovych666.intellij.plugin.jsonsorter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

class JSONReader {

    private String input;
    private int index;
    private int length;

    JSONReader(@NotNull String input) {
        this.input = input;
        index = 0;
        length = input.length();
    }

    Object read() throws Exception {
        skipWhitespaces();
        Object value = readValue();

        if (index < length) {
            throw new Exception("Unexpected content.");
        }

        return value;
    }

    private Object readValue() throws Exception {
        String type = getType();
        if (type.equals("object")) {
            return readValueObject();
        }
        if (type.equals("array")) {
            return readValueArray();
        }
        if (type.equals("string")) {
            return readValueString();
        }
        if (type.equals("any")) {
            return readValueAny();
        }
        throw new Exception("Expected value.");
    }

    private JSONArray readValueArray() throws Exception {
        JSONArray list = new JSONArray();

        readChar('[');

        String type = getType();
        while (!type.equals("arrayEnd")) {
            Object item = readValue();
            list.add(item);
            type = getType();
            if (!type.equals("comma")) {
                break;
            }
            readChar(',');
        }

        readChar(']');

        return list;
    }

    private JSONObject readValueObject() throws Exception {
        JSONObject list = new JSONObject();

        readChar('{');

        String type = getType();
        while (!type.equals("objectEnd")) {
            String key = readValueString();
            readChar(':');
            Object val = readValue();
            JSONTuple item = new JSONTuple(key, val);
            list.add(item);
            type = getType();
            if (!type.equals("comma")) {
                break;
            }
            readChar(',');
        }

        readChar('}');

        return list;
    }

    @NotNull
    private String readValueAny() {
        StringBuilder string = new StringBuilder();
        char c;

        while (index < length) {
            c = input.charAt(index);

            if (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == ',' || c == '}' || c == ']') {
                break;
            }

            index++;
            string.append(c);
        }

        skipWhitespaces();
        return string.toString();
    }

    @NotNull
    @Contract(pure = true)
    private String getType() {
        char c;
        try {
            c = input.charAt(index);
        } catch (Exception e) {
            return "error";
        }
        if (c == '"') {
            return "string";
        }
        if (c == '{') {
            return "object";
        }
        if (c == '}') {
            return "objectEnd";
        }
        if (c == '[') {
            return "array";
        }
        if (c == ']') {
            return "arrayEnd";
        }
        if (c == ',') {
            return "comma";
        }
        return "any";
    }

    private void readChar(char expected) throws Exception {
        readCharExact(expected);
        skipWhitespaces();
    }

    private void readCharExact(char expected) throws Exception {
        char c = input.charAt(index++);
        if (c != expected) {
            throw new Exception("Expected `" + expected + "`.");
        }
    }

    @NotNull
    private String readValueString() throws Exception {
        StringBuilder string = new StringBuilder();

        readCharExact('"');
        string.append('"');

        boolean escaped = true;
        while (index < length) {
            char c = input.charAt(index);

            if (c == '\n') {
                throw new Exception("Unexpected `\\n`.");
            }

            if (c == '\r') {
                throw new Exception("Unexpected `\\r`.");
            }

            if (c == '"' && !escaped) {
                break;
            }

            index++;
            string.append(c);
            escaped = c == '\\' && !escaped;
        }

        readChar('"');
        string.append('"');
        return string.toString();
    }

    private void skipWhitespaces() {
        while (index < length) {
            char c = input.charAt(index);
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                index++;
                continue;
            }
            break;

        }
    }

}
