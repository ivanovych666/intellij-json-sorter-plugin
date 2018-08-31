package org.jetbrains.tutorials.actions;

import java.util.Comparator;

public class AlphabeticalSort extends AbstractSort {

    public Comparator<String> comparator() {
        return String::compareToIgnoreCase;
    }

}