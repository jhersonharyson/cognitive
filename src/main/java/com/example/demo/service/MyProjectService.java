package com.example.demo.service;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import org.jetbrains.annotations.NotNull;

public class MyProjectService {

    public MyProjectService() {
        EditorFactory editorFactory = EditorFactory.getInstance();
        editorFactory.addEditorFactoryListener(new EditorFactoryListener() {
            @Override
            public void editorCreated(@NotNull EditorFactoryEvent editorFactoryEvent) {
                editorFactoryEvent.getEditor();
                return;
            }
        });
    }
}
