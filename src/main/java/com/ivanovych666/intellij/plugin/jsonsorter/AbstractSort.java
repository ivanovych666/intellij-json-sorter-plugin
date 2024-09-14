package com.ivanovych666.intellij.plugin.jsonsorter;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public abstract class AbstractSort extends AnAction {

    abstract Comparator<String> comparator();

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        DataContext dataContext = anActionEvent.getDataContext();
        DataContextUtils dataContextUtils = new DataContextUtils(dataContext);
        Editor editor = dataContextUtils.getEditor();
        if (editor == null) {
            projectAction(dataContextUtils);
        } else {
            editorAction(dataContextUtils);
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    private void projectAction(@NotNull DataContextUtils dataContextUtils) {
        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        VirtualFile[] files = dataContextUtils.getVirtualFiles();
        for (VirtualFile file : files) {
            Document document = fileDocumentManager.getDocument(file);
            documentAction(dataContextUtils, document);
        }
    }

    private void editorAction(@NotNull DataContextUtils dataContextUtils) {
        Editor editor = dataContextUtils.getEditor();
        Document document = editor.getDocument();
        documentAction(dataContextUtils, document);
    }

    private void documentAction(@NotNull DataContextUtils dataContextUtils, Document document) {
        Project project = dataContextUtils.getProject();
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        psiDocumentManager.commitDocument(document);
        psiDocumentManager.performForCommittedDocument(document, () -> {
            String text = document.getText();
            String updatedText = updateContent(text);
            if (updatedText != null) {
                WriteCommandAction.runWriteCommandAction(project, () -> document.setText(updatedText));
            }
        });
    }

    private @Nullable String updateContent(String text) {
        String result = null;
        try {
            JSONReader reader = new JSONReader(text);
            Object value = reader.read();
            JSONWriter writer = new JSONWriter(comparator(), reader.getFormat());
            result = writer.write(value);
        } catch (Exception ignore) {
        }
        return text.equals(result) ? null : result;
    }
}
