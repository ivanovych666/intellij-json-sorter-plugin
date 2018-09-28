package com.ivanovych666.intellij.plugin.jsonsorter;

class JSONTuple {

    private String key;
    private Object value;

    JSONTuple(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    String getKey() {
        return this.key;
    }

    Object getValue() {
        return this.value;
    }

}
