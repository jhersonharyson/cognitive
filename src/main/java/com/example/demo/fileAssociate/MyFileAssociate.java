package com.example.demo.fileAssociate;

import com.example.demo.utils.RealtimeState;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.FileEditorAssociateFinder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyFileAssociate implements FileEditorAssociateFinder {
    @Override
    public @Nullable VirtualFile getAssociatedFileToOpen(@NotNull Project project, @NotNull VirtualFile original) {
        try {
            var currentFile = FileEditorManager.getInstance(project).getSelectedFiles()[0];
            RealtimeState.getInstance().updateStateByFile(currentFile);
        } catch (Exception e) {
            // TODO that is ok
        }
        return original;
    }

}

