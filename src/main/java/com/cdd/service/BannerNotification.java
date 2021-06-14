package com.cdd.service;

import com.intellij.ProjectTopics;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootAdapter;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BannerNotification extends EditorNotifications.Provider<EditorNotificationPanel> {

    private static final Key<EditorNotificationPanel> KEY = Key.create("setup.leekwars.api");

    private final Project myProject;

    public BannerNotification(Project project) {
        myProject = project;
        myProject.getMessageBus().connect(project).subscribe(ProjectTopics.PROJECT_ROOTS, new ModuleRootAdapter() {

        });
    }

    @NotNull
    @Override
    public Key<EditorNotificationPanel> getKey() {
        return KEY;
    }

    @Nullable
    @Override
    public EditorNotificationPanel createNotificationPanel(VirtualFile file, FileEditor fileEditor) {
        return createPanel(myProject);
    }


    @NotNull
    private static EditorNotificationPanel createPanel(final @NotNull Project project) {
        final EditorNotificationPanel panel = new EditorNotificationPanel();

        panel.setText("LeekWars API could not be found");
        panel.createActionLabel("Update LeekWars API", () -> {});

        return panel;
    }
}
