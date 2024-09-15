package com.ivanovych666.intellij.plugin.jsonsorter;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;


public class JSONSorterGroup extends DefaultActionGroup {
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);

        Presentation presentation = event.getPresentation();
        DataContext dataContext = event.getDataContext();
        presentation.setEnabledAndVisible(isVisible(dataContext));
    }

    private boolean isVisible(DataContext dataContext) {
        DataContextUtils dataContextUtils = new DataContextUtils(dataContext);
        VirtualFile[] files = dataContextUtils.getSelectedJsonFiles(true);
        return files.length > 0;
    }
}
