package com.example.demo.promoter;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vfs.VirtualFile;

public class MyChangeFileAction extends AnAction implements DumbAware {
    public MyChangeFileAction() {
        ActionUtil.copyFrom(this, IdeActions.ACTION_OPEN_IN_NEW_WINDOW);
    }

    @Override
    public void update(final AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null && e.getData(CommonDataKeys.VIRTUAL_FILE) != null);
    }

    @Override
    public void actionPerformed(final AnActionEvent e) {
        final FileEditorManagerEx mgr = FileEditorManagerEx.getInstanceEx(e.getProject());
        VirtualFile file = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE);
        EditorWindow window = mgr.getCurrentWindow();

        if (window == null || window.isFilePinned(file)) return;

        if (window.findFileComposite(file) != null) {
            mgr.closeFile(file, window);
        }
    }
}
