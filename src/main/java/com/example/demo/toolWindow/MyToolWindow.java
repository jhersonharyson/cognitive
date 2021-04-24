package com.example.demo.toolWindow;

import com.example.demo.utils.RealtimeLambdaState;
import com.example.demo.utils.RealtimeState;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.util.Arrays;

public class MyToolWindow {

    public VirtualFile classAnalised;


    private JPanel myToolWindowContent;
    private JButton chooseAnotherClassButton;
    private JButton openRulesButton;
    private JCheckBox limitExeceededNotificationCheckBox;
    private JCheckBox inlineHintFeedbackCheckBox;
    private JCheckBox refactoringSuggestionNotificationCheckBox;
    private JLabel className;
    private JLabel currentComplexity;
    private JLabel limitOfComplexity;

    public MyToolWindow(ToolWindow toolWindow, Project project) {

        RealtimeState.getInstance().setListener(new RealtimeLambdaState() {
            @Override
            public Object applyLimitOfComplexity(int complexity) {
                limitOfComplexity.setText(String.valueOf(complexity));
                return null;
            }

            @Override
            public Object applyCurrentComplexity(int complexity) {
                currentComplexity.setText(String.valueOf(complexity));
                return null;
            }

            @Override
            public Object applyCurrentClass(VirtualFile filename) {
                if (filename != null)
                    className.setText(filename.getName());
                return null;
            }
        });

        this.chooseAnotherClassButton.addActionListener(l -> {

            var classChooser = TreeClassChooserFactory.getInstance(project).createFileChooser("adasdasdasda", null, null, null, true);
            classChooser.showDialog();

            if (classChooser.getSelectedFile() != null) {
                VirtualFile file = classChooser.getSelectedFile().getContainingFile().getVirtualFile();

                var fileOptional = Arrays.stream(FileEditorManager.getInstance(project).getOpenFiles()).findFirst();
                this.classAnalised = file; //fileOptional.orElseThrow();
                this.className.setText(this.classAnalised.getName());
                FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, file).setUseCurrentWindow(true), false );
                RealtimeState.getInstance().setFile(file);
            }

        });
        this.AutoCompleteTextField();
    }

    public void AutoCompleteTextField() {

    }


    public JPanel getContent() {
        return myToolWindowContent;
    }


}

