package com.example.demo.fileAssociate;

import com.intellij.openapi.fileEditor.impl.FileEditorAssociateFinder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyFileAssociate implements FileEditorAssociateFinder {
    @Override
    public @Nullable VirtualFile getAssociatedFileToOpen(@NotNull Project project, @NotNull VirtualFile original) {
        return original;
    }
}
