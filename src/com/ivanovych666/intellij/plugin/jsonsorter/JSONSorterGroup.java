package com.ivanovych666.intellij.plugin.jsonsorter;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.VirtualFile;

public class JSONSorterGroup extends DefaultActionGroup {
    @Override
    public void update(AnActionEvent event) {
        super.update(event);
        DataContext dataContext = event.getDataContext();
        VirtualFile virtualFile = PlatformDataKeys.VIRTUAL_FILE.getData(dataContext);
        event.getPresentation().setVisible(isJson(virtualFile));
    }

    private boolean isJson(VirtualFile file) {
        if (file == null) {
            return false;
        }
        return JsonFileType.INSTANCE.equals(file.getFileType()) || file.getPath().toUpperCase().endsWith(".JSON");
    }
}
