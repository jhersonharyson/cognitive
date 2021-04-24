package com.example.demo.fileSwapper;

import com.intellij.openapi.fileEditor.impl.EditorFileSwapper;
import com.intellij.openapi.fileEditor.impl.EditorWithProviderComposite;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

public class MyFileSwapper extends EditorFileSwapper {
    @Override
    public @Nullable Pair<VirtualFile, Integer> getFileToSwapTo(Project project, EditorWithProviderComposite editorWithProviderComposite) {
        return null;
    }
}
