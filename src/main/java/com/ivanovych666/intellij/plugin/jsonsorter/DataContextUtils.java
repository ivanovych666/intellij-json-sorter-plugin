package com.ivanovych666.intellij.plugin.jsonsorter;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DataContextUtils {
    private final DataContext dataContext;

    DataContextUtils(@NotNull DataContext dataContext) {
        this.dataContext = dataContext;
    }

    public Project getProject() {
        return CommonDataKeys.PROJECT.getData(dataContext);
    }

    public Editor getEditor() {
        return CommonDataKeys.EDITOR.getData(dataContext);
    }

    public VirtualFile[] getVirtualFiles() {
        VirtualFile[] virtualFileArray = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
        if (virtualFileArray == null) {
            VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
            if (virtualFile == null) {
                virtualFileArray = new VirtualFile[0];
            } else {
                virtualFileArray = new VirtualFile[]{virtualFile};
            }
        }
        return Arrays.stream(virtualFileArray).filter(this::isJson).toArray(VirtualFile[]::new);
    }

    private boolean isJson(@NotNull VirtualFile file) {
        String path = file.getPath();
        FileType type = file.getFileType();
        return JsonFileType.INSTANCE.equals(type) || path.toLowerCase().endsWith(".json");
    }
}
