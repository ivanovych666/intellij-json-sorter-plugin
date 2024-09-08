package com.ivanovych666.intellij.plugin.jsonsorter;

import java.util.Comparator;

public class AlphabeticalCiSort extends AbstractSort {

    public Comparator<String> comparator() {
        return String::compareToIgnoreCase;
    }

}