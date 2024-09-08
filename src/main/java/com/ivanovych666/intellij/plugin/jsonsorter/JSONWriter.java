package com.ivanovych666.intellij.plugin.jsonsorter;

import java.util.Comparator;

class JSONWriter {

    private final Comparator<JSONTuple> comparator;

    JSONWriter(Comparator<String> comparator) {
        this.comparator = (JSONTuple a, JSONTuple b) -> {
            String as = a.key();
            String bs = b.key();
            return comparator.compare(
                    as.substring(1, as.length() - 1),
                    bs.substring(1, bs.length() - 1)
            );
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
        } else if (value instanceof JSONObject list) {
            stringBuilder.append('{');

            list.sort(this.comparator);

            for (int i = 0, size = list.size(); i < size; i++) {
                if (i > 0) {
                    stringBuilder.append(',');
                }
                JSONTuple keyValue = list.get(i);

                String key = keyValue.key();
                Object val = keyValue.value();

                stringBuilder.append(key).append(':');

                write(stringBuilder, val);
            }

            stringBuilder.append('}');
        } else if (value instanceof JSONArray list) {
            stringBuilder.append('[');

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
