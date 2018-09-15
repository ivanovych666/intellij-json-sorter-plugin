package com.ivanovych666.intellij.plugin.jsonsorter;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public abstract class AbstractSort extends AnAction {

    abstract Comparator<String> comparator();

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        Document document = editor.getDocument();
        String text = document.getText();
        JSONReader reader = new JSONReader(text);
        Object value;

        try {
            value = reader.read();
        } catch (Exception e) {
            return;
        }

        JSONWriter writer = new JSONWriter(comparator());
        String result = writer.write(value);

        Application app = ApplicationManager.getApplication();
        app.runWriteAction(() -> document.setText(result));

        reformat(anActionEvent);
    }

    private void reformat(AnActionEvent anActionEvent) {
        ActionManager am = ActionManager.getInstance();
        AnAction action = am.getAction("ReformatCode");
        action.actionPerformed(anActionEvent);
    }

}
