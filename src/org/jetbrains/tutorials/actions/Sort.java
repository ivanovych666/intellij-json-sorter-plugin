package org.jetbrains.tutorials.actions;

import com.google.common.collect.Lists;
import org.json.*;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import java.util.*;

public class Sort extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        Document doc = editor.getDocument();
        String text = doc.getText();

        JSONObject srcJson = new JSONObject(text);

        JSONWriter writer = new JSONStringer();
        writeValue(srcJson, writer);

        doc.setText(writer.toString());

        ActionManager am = ActionManager.getInstance();
        AnAction action = am.getAction("ReformatCode");
        action.actionPerformed(anActionEvent);
    }

    private void writeValue(Object value, JSONWriter writer) {

        if (value instanceof JSONObject) {

            writer.object();

            JSONObject objectValue = (JSONObject) value;

            Iterator<String> keysIterator = objectValue.keys();
            List<String> keysList = Lists.newArrayList(keysIterator);

            keysList.sort(String::compareToIgnoreCase);

            for (String key : keysList) {
                writer.key(key);
                writeValue(objectValue.get(key), writer);
            }

            writer.endObject();

        } else if (value instanceof JSONArray) {

            writer.array();

            JSONArray arrayValue = (JSONArray) value;

            for (Object val : arrayValue) {
                writeValue(val, writer);
            }

            writer.endArray();

        } else {

            writer.value(value);

        }

    }

}