package com.ivanovych666.intellij.plugin.jsonsorter;

import java.util.Comparator;

class JSONWriter extends JSONFormat {

    private final Comparator<JSONTuple> comparator;
    private final String indent;
    private final String newLine;
    private final String finalNewLine;
    private final String space;

    JSONWriter(Comparator<String> comparator, JSONFormat format) {
        this.comparator = (JSONTuple a, JSONTuple b) -> {
            String as = a.key();
            String bs = b.key();
            return comparator.compare(
                    as.substring(1, as.length() - 1),
                    bs.substring(1, bs.length() - 1)
            );
        };

        this.indent = format.indent;

        if (!format.indent.isEmpty()) {
            this.newLine = format.newLine;
            this.space = " ";
        } else {
            this.newLine = "";
            this.space = "";
        }

        this.finalNewLine = format.finalNewLine ? format.newLine : "";
    }

    String write(Object value) {
        StringBuilder stringBuilder = new StringBuilder();
        write(stringBuilder, value, "");
        return stringBuilder.append(finalNewLine).toString();
    }

    private void write(StringBuilder stringBuilder, Object value, String parentPad) {
        String pad = parentPad + indent;

        if (value instanceof String) {
            stringBuilder.append((String) value);
        } else if (value instanceof JSONObject list) {
            stringBuilder.append('{').append(newLine);

            list.sort(comparator);

            for (int i = 0, size = list.size(); i < size; i++) {
                if (i > 0) {
                    stringBuilder.append(',').append(newLine);
                }
                JSONTuple keyValue = list.get(i);

                String key = keyValue.key();
                Object val = keyValue.value();

                stringBuilder.append(pad).append(key).append(':').append(space);

                write(stringBuilder, val, pad);
            }

            stringBuilder.append(newLine).append(parentPad).append('}');
        } else if (value instanceof JSONArray list) {
            stringBuilder.append('[').append(newLine);

            for (int i = 0, size = list.size(); i < size; i++) {
                if (i > 0) {
                    stringBuilder.append(',').append(newLine);
                }
                Object item = list.get(i);
                stringBuilder.append(pad);
                write(stringBuilder, item, pad);
            }

            stringBuilder.append(newLine).append(parentPad).append(']');
        }

    }

}
