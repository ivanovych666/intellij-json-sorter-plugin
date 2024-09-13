package com.ivanovych666.intellij.plugin.jsonsorter;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public abstract class AbstractSort extends AnAction {

    abstract Comparator<String> comparator();

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        DataContext dataContext = anActionEvent.getDataContext();
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        if (project == null) {
            return;
        }

        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor == null) {
            return;
        }

        PsiDocumentManager.getInstance(project).commitAllDocuments();
        Document document = editor.getDocument();
        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(document);
        if (file == null) {
            return;
        }

        String text = document.getText();
        JSONReader reader = new JSONReader(text);
        Object value;

        try {
            value = reader.read();
        } catch (Exception e) {
            return;
        }

        JSONWriter writer = new JSONWriter(comparator(), reader.getFormat());
        String result = writer.write(value);

        if (!text.equals(result)) {
            WriteCommandAction.runWriteCommandAction(project, () -> document.setText(result));
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
