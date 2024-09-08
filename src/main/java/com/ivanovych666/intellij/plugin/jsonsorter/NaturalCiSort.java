package com.ivanovych666.intellij.plugin.jsonsorter;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NaturalCiSort extends AbstractSort {

    public Comparator<String> comparator() {
        return (String a, String b) -> normalize(a).compareToIgnoreCase(normalize(b));
    }

    String normalize(String str) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String replacement = String.format("%20s", m.group()).replace(' ', '0');
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);
        return sb.toString();
    }
}