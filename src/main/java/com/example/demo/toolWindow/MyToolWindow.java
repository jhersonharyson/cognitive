package com.example.demo.toolWindow;

import com.example.demo.utils.RealtimeLambdaState;
import com.example.demo.utils.RealtimeState;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MyToolWindow {

    public VirtualFile classAnalysed;


    private JPanel myToolWindowContent;
    private JButton chooseAnotherClassButton;
    private JButton openRulesButton;
    private JCheckBox limitExceededNotificationCheckBox;
    private JCheckBox showComplexityInTheCodeCheckBox;
    private JLabel className;
    private JLabel currentComplexity;
    private JLabel limitOfComplexity;
    private JButton syncButton;

    public MyToolWindow(ToolWindow toolWindow, Project project) {
        this.configureRealtimeState();
        this.configureCurrentOpenedFileInfo();
        this.configureActions(project);
        this.initializeCheckbox();

    }

    private void configureCurrentOpenedFileInfo() {
        var file = FileEditorManager.getInstance(ProjectManager.getInstance().getOpenProjects()[0]).getSelectedFiles();
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

    private void initializeCheckbox() {
        this.showComplexityInTheCodeCheckBox.setSelected(true);
        this.limitExceededNotificationCheckBox.setSelected(true);
    }

    private void configureActions(Project project) {
        this.configureChooseAnotherClassAction(project);
        this.configureOpenRulesAction();
        this.configureShowComplexityInTheCodeAction();
        this.configureLimitExceededNotificationAction();
        this.configureSync();
    }

    private void configureSync() {

        this.syncButton.addActionListener(e -> {
            try {
                Project project = ProjectManager.getInstance().getOpenProjects()[0];
                var files = FileEditorManager.getInstance(project).getSelectedFiles();
                if (files.length >= 1) {
                    this.classAnalysed = files[0];
                    this.className.setText(this.classAnalysed.getName());
                    RealtimeState.getInstance().setFile(files[0]);
                    files[0].refresh(true, true);
                    FileEditorManager.getInstance(project).closeFile(files[0]);
                    FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, files[0]).setUseCurrentWindow(true), false);

//                    PsiFile file = PsiManager.getInstance(project).findFile(files[0]);
//                    ApplicationManager.getApplication().runWriteAction(()->{
//                        PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
//                        Document document = documentManager.getDocument(file);
//                        document.insertString(0, "");
//                        //FileEditorManager.getInstance(project).getEditors(files[0])[0].getBackgroundHighlighter().getInlayModel(). InlayModel.addBlockElement()
//                        DataContext dataContext = DataManager.getInstance().getDataContext();
//                        EditorImpl editor = ObjectUtils.tryCast(dataContext.getData(CommonDataKeys.EDITOR), EditorImpl.class);
//                        editor.get.getInlayModel()
//                        Arrays.stream(FileEditorManager.getInstance(ProjectManager.getInstance().getOpenProjects()[0]).getSelectedFiles()).forEach( f -> {
//                            FileDocumentManager.getInstance().reloadFiles(f);
//                        });
//                    });

//                    files[0]
//                            FileEditorManager.getInstance(project).wr
                }
            } catch (Exception ignored) {
                System.out.println("ee " + ignored);
            }
        });

    }

    private void configureOpenRulesAction() {

        this.openRulesButton.addActionListener(e -> new RulesWindow());

    }


    private void configureShowComplexityInTheCodeAction() {
        this.showComplexityInTheCodeCheckBox.addActionListener(e -> {
            var selected = showComplexityInTheCodeCheckBox.isSelected();
            RealtimeState.getInstance().setShowComplexityInTheCode(selected);
        });
    }

    private void configureLimitExceededNotificationAction() {
        this.limitExceededNotificationCheckBox.addActionListener(e -> {
            var selected = limitExceededNotificationCheckBox.isSelected();
            RealtimeState.getInstance().setLimitExceededNotification(selected);
        });
    }

    private void configureChooseAnotherClassAction(Project project) {
        this.chooseAnotherClassButton.addActionListener(l -> {

            var classChooser = TreeClassChooserFactory.getInstance(project).createFileChooser("adasdasdasda", null, null, null, false);
            classChooser.showDialog();

            if (classChooser.getSelectedFile() != null) {
                VirtualFile file = classChooser.getSelectedFile().getContainingFile().getVirtualFile();

                var fileOptional = Arrays.stream(FileEditorManager.getInstance(project).getOpenFiles()).findFirst();
                this.classAnalysed = file; //fileOptional.orElseThrow();
                this.className.setText(this.classAnalysed.getName());
                FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, file).setUseCurrentWindow(true), false);
                RealtimeState.getInstance().setFile(file);
            }
        });
    }

    private void configureRealtimeState() {
        RealtimeState.getInstance().setListener(new RealtimeLambdaState() {

            @Override
            public Object applyLimitOfComplexity(int complexity) {
                limitOfComplexity.setText(String.valueOf(complexity));
                updateColors();
                return null;
            }

            @Override
            public Object applyCurrentComplexity(int complexity) {
                currentComplexity.setText(String.valueOf(complexity));
                updateColors();
                return null;
            }

            @Override
            public Object applyCurrentClass(VirtualFile filename) {
                if (filename != null) {
                    className.setText(filename.getName());
                    updateColors();
                }
                return null;
            }
        });
    }

    private void updateColors() {
        var color = RealtimeState.getInstance().getCurrentComplexity() >= RealtimeState.getInstance().getLimitOfComplexity() ? new Color(186, 111, 37) : new Color(88, 157, 246);
        this.className.setForeground(color);
    }


}

