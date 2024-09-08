package com.ivanovych666.intellij.plugin.jsonsorter;

import com.intellij.codeInsight.actions.*;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.lang.LanguageFormatting;
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

        JSONWriter writer = new JSONWriter(comparator());
        String result = writer.write(value);

        WriteCommandAction.runWriteCommandAction(project, () -> document.setText(result));

        FormattingModelBuilder formattingModelBuilder = LanguageFormatting.INSTANCE.forContext(file);
        if (formattingModelBuilder == null) {
            return;
        }

        LastRunReformatCodeOptionsProvider provider = new LastRunReformatCodeOptionsProvider(PropertiesComponent.getInstance());
        ReformatCodeRunOptions currentRunOptions = provider.getLastRunOptions(file);

        currentRunOptions.setProcessingScope(TextRangeType.WHOLE_FILE);
        (new FileInEditorProcessor(file, editor, currentRunOptions)).processCode();
    }

}
