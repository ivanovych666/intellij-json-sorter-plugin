package com.ivanovych666.intellij.plugin.jsonsorter;

import java.util.Comparator;

class JSONWriter {

    private Comparator<JSONTuple> comparator;

    JSONWriter(Comparator<String> comparator) {
        this.comparator = (JSONTuple a, JSONTuple b) -> {
            String as = a.getFirst();
            String bs = b.getFirst();
            return comparator.compare(as, bs);
        };
    }

    String write(Object value) {
        StringBuilder stringBuilder = new StringBuilder();
        write(stringBuilder, value);
        return stringBuilder.toString();
    }

    private void write(StringBuilder stringBuilder, Object value) {

        if (value instanceof String) {
            stringBuilder.append((String) value);
        } else if (value instanceof JSONObject) {
            stringBuilder.append('{');

            JSONObject list = (JSONObject) value;

            list.sort(this.comparator);

            for (int i = 0, size = list.size(); i < size; i++) {
                if (i > 0) {
                    stringBuilder.append(',');
                }
                JSONTuple keyValue = list.get(i);

                String key = keyValue.getFirst();
                Object val = keyValue.getSecond();

                stringBuilder.append(key).append(':');

                write(stringBuilder, val);
            }

            stringBuilder.append('}');
        } else if (value instanceof JSONArray) {
            stringBuilder.append('[');

            JSONArray list = (JSONArray) value;

            for (int i = 0, size = list.size(); i < size; i++) {
                if (i > 0) {
                    stringBuilder.append(',');
                }
                Object item = list.get(i);
                write(stringBuilder, item);
            }

            stringBuilder.append(']');
        }

    }

}
