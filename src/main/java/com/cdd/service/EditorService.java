package com.cdd.service;

import com.example.demo.utils.RealtimeState;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EditorService {

    public static void syncFiles() {
        try {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            var files = FileEditorManager.getInstance(project).getSelectedFiles();

            if (files.length >= 1) {
                RealtimeState.getInstance().setFile(files[0]);
                files[0].refresh(true, true);
                FileEditorManager.getInstance(project).closeFile(files[0]);
                FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, files[0]).setUseCurrentWindow(true), false);
            }
        } catch (Exception ignored) {
            log.info("ee " + ignored);
        }
    }
}
