package com.example.demo.navigation;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorNavigationDelegate;
import org.jetbrains.annotations.NotNull;

public class MyNavigation implements EditorNavigationDelegate {
    @Override
    public @NotNull Result navigateToLineEnd(@NotNull Editor editor, @NotNull DataContext dataContext) {
        return null;
    }
}
