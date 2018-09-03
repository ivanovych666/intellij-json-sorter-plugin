package com.ivanovych666.intellij.plugin.jsonsorter;

import java.util.Comparator;

public class AlphabeticalReverseSort extends AbstractSort {

    public Comparator<String> comparator() {
        return (String a, String b) -> -a.compareToIgnoreCase(b);
    }

}