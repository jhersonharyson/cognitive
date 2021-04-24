package com.example.demo.fileContext;

import com.intellij.psi.FileContextProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class MyFileContext extends FileContextProvider {
    @Override
    protected boolean isAvailable(PsiFile file) {
        return false;
    }

    @Override
    public @NotNull Collection<PsiFileSystemItem> getContextFolders(PsiFile file) {
        return null;
    }

    @Override
    public @Nullable PsiFile getContextFile(PsiFile file) {
        return null;
    }


}
