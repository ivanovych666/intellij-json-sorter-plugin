package org.jetbrains.tutorials.actions;

import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONWriter;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractSort extends AnAction {

    abstract Comparator<String> comparator();

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        Document document = editor.getDocument();
        String text = document.getText();

        JSONObject srcJson = new JSONObject(text);
        JSONWriter writer = new JSONStringer();

        writeValue(srcJson, writer, comparator());

        document.setText(writer.toString());

        reformat(anActionEvent);
    }

    private void writeValue(Object value, JSONWriter writer, Comparator<String> comparator) {

        if (value instanceof JSONObject) {

            writer.object();

            JSONObject objectValue = (JSONObject) value;

            Iterator<String> keysIterator = objectValue.keys();
            List<String> keysList = Lists.newArrayList(keysIterator);

            keysList.sort(comparator);

            for (String key : keysList) {
                writer.key(key);
                writeValue(objectValue.get(key), writer, comparator);
            }

            writer.endObject();

        } else if (value instanceof JSONArray) {

            writer.array();

            JSONArray arrayValue = (JSONArray) value;

            for (Object val : arrayValue) {
                writeValue(val, writer, comparator);
            }

            writer.endArray();

        } else {

            writer.value(value);

        }

    }

    private void reformat(AnActionEvent anActionEvent) {
        ActionManager am = ActionManager.getInstance();
        AnAction action = am.getAction("ReformatCode");
        action.actionPerformed(anActionEvent);
    }

}
