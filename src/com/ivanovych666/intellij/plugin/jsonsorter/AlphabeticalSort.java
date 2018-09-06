package com.ivanovych666.intellij.plugin.jsonsorter;

import java.util.Comparator;

public class AlphabeticalSort extends AbstractSort {

    public Comparator<String> comparator() {
        return String::compareTo;
    }

}