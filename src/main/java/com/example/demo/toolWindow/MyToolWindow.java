package com.example.demo.toolWindow;

import com.cdd.service.JavaClassesService;
import com.example.demo.utils.RealtimeLambdaState;
import com.example.demo.utils.RealtimeState;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
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
        this.configureActions(project);
        this.initializeCheckbox();

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
//        this.syncButton.addActionListener(e -> JavaClassesService.run());
        this.syncButton.addActionListener(e -> JavaClassesService.getListOfQualifiers());
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

            var classChooser = TreeClassChooserFactory.getInstance(project).createFileChooser("adasdasdasda", null, null, null, true);
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

