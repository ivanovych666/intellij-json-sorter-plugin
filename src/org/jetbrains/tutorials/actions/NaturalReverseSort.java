package org.jetbrains.tutorials.actions;

import java.util.Comparator;

public class NaturalReverseSort extends NaturalSort {

    public Comparator<String> comparator() {
        return (String a, String b) -> -normalize(a).compareToIgnoreCase(normalize(b));
    }

}