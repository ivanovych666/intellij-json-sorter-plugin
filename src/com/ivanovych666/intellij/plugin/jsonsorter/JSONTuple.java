package com.ivanovych666.intellij.plugin.jsonsorter;

import groovy.lang.Tuple2;

class JSONTuple extends Tuple2<String, Object> {
    JSONTuple(String first, Object second) {
        super(first, second);
    }
}
