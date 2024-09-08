package com.ivanovych666.intellij.plugin.jsonsorter;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class JSONSorterGroup extends DefaultActionGroup {
    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);

        Presentation presentation = event.getPresentation();
        DataContext dataContext = event.getDataContext();
        presentation.setEnabledAndVisible(isVisible(dataContext));
    }

    private boolean isVisible(DataContext dataContext) {
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        if (project == null) {
            return false;
        }

        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor == null) {
            return false;
        }

        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (file == null) {
            return false;
        }

        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null) {
            return false;
        }

        return isJson(virtualFile);
    }

    private boolean isJson(@NotNull VirtualFile file) {
        return JsonFileType.INSTANCE.equals(file.getFileType()) || file.getPath().toUpperCase().endsWith(".JSON");
    }
}
