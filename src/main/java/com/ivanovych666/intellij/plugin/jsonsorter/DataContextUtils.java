package com.ivanovych666.intellij.plugin.jsonsorter;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DataContextUtils {
    private final DataContext dataContext;
    private final FileType jsonFileType;

    DataContextUtils(@NotNull DataContext dataContext) {
        this.dataContext = dataContext;
        FileTypeRegistry fileTypeRegistry = FileTypeRegistry.getInstance();
        jsonFileType = fileTypeRegistry.getFileTypeByExtension("json");
    }

    public Project getProject() {
        return CommonDataKeys.PROJECT.getData(dataContext);
    }

    public Editor getEditor() {
        return CommonDataKeys.EDITOR.getData(dataContext);
    }

    public VirtualFile @NotNull [] getSelectedJsonFiles(boolean returnFirst) {
        Project project = getProject();
        ProjectFileIndex projectFileIndex = ProjectFileIndex.getInstance(project);
        VirtualFile[] selectedFiles = getVirtualFileArray();
        ArrayList<VirtualFile> list = new ArrayList<>();

        for (VirtualFile selectedFile : selectedFiles) {
            if (selectedFile.isDirectory()) {
                boolean skipIgnored = !projectFileIndex.isExcluded(selectedFile);

                VfsUtilCore.iterateChildrenRecursively(selectedFile, file -> {
                    boolean fileIgnored = projectFileIndex.isExcluded(file);
                    if (fileIgnored && skipIgnored) {
                        return false;
                    }
                    return file.isDirectory() || isJson(file);
                }, file -> {
                    if (isJson(file)) {
                        list.add(file);
                        return !returnFirst;
                    }
                    return true;
                });
            } else if (isJson(selectedFile)) {
                list.add(selectedFile);
            }

            if (returnFirst && !list.isEmpty()) {
                break;
            }
        }

        return list.toArray(new VirtualFile[0]);
    }

    private VirtualFile @NotNull [] getVirtualFileArray() {
        VirtualFile[] virtualFileArray = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
        if (virtualFileArray == null) {
            VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(dataContext);
            if (virtualFile == null) {
                virtualFileArray = new VirtualFile[0];
            } else {
                virtualFileArray = new VirtualFile[]{virtualFile};
            }
        }
        return virtualFileArray;
    }

    private boolean isJson(@NotNull VirtualFile file) {
        String path = file.getPath();
        FileType type = file.getFileType();
        return jsonFileType.equals(type) || path.toLowerCase().endsWith(".json");
    }
}
