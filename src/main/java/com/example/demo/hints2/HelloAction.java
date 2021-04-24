package com.example.demo.hints2;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HelloAction extends AnAction {

    //  DATA DIALOG WRAPPER
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        var get = new MyIdeaPopupList("My List", Arrays.asList("123", "321", "456", "789", "asdas", "hfghfg", "zxczxczcxz"));
//        if(e.getProject() != null){
//            JBPopupFactory.getInstance().createListPopup(get, 2).showCenteredInCurrentWindow(e.getProject());
//        }
        this.actionPerformed3(e);
    }


    public void actionPerformed2(AnActionEvent anActionEvent) {
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (editor == null || psiFile == null) {
            return;
        }
        int offset = editor.getCaretModel().getOffset();

        final StringBuilder infoBuilder = new StringBuilder();
        PsiElement element = psiFile.findElementAt(offset);
        infoBuilder.append("Element at caret: ").append(element).append("\n");
        if (element != null) {
            PsiMethod containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
            infoBuilder
                    .append("Containing method: ")
                    .append(containingMethod != null ? containingMethod.getName() : "none")
                    .append("\n");
            if (containingMethod != null) {
                PsiClass containingClass = containingMethod.getContainingClass();
                infoBuilder
                        .append("Containing class: ")
                        .append(containingClass != null ? containingClass.getName() : "none")
                        .append("\n");

                infoBuilder.append("Local variables:\n");
                containingMethod.accept(new JavaRecursiveElementVisitor() {
                    @Override
                    public void visitLocalVariable(PsiLocalVariable variable) {
                        super.visitLocalVariable(variable);
                        infoBuilder.append(variable.getName()).append("\n");
                    }
                });

                infoBuilder.append("\n");
                infoBuilder.append("If Statements:\n");
                containingMethod.accept(new JavaRecursiveElementVisitor() {
                    @Override
                    public void visitIfStatement(PsiIfStatement statement) {
                        super.visitIfStatement(statement);
                        infoBuilder.append(Objects.requireNonNull(statement.getCondition()).getText()).append("\n");
                    }
                });
            }
        }
        Messages.showMessageDialog(anActionEvent.getProject(), infoBuilder.toString(), "PSI Info", null);
    }


    public void actionPerformed3(AnActionEvent anActionEvent) {
        PsiFile psiFile = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null) {
            return;
        }

        final StringBuilder infoBuilder = new StringBuilder();


        final int[] numberOfIfStatements = {0};
        final int[] numberMethods = {0};
        ((PsiJavaFileImpl) psiFile).getClasses()[0].accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitIfStatement(PsiIfStatement statement) {
                super.visitIfStatement(statement);
                numberOfIfStatements[0] += 1;
            }

            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                numberMethods[0] += 1;
            }
        });

        infoBuilder.append("number of if statements:")
                .append("\n")
                .append(numberOfIfStatements[0])
                .append("\n")
                .append("number of methods:")
                .append("\n")
                .append(numberMethods[0]);

        Messages.showMessageDialog(anActionEvent.getProject(), infoBuilder.toString(), "PSI Info", null);
    }


//## DIALOG WRAPEPR AND STORAGE SETTINGS
//    @Override
//    public void actionPerformed(@NotNull AnActionEvent e) {
//        var get = new MyIdeaDataDialogWrapper().showAndGet();
//        Messages.showMessageDialog(e.getProject(), String.valueOf(get), "Got", Messages.getInformationIcon());
//    }


//##  DIALOG EXEMPLPLE
//    @Override
//    public void actionPerformed(@NotNull AnActionEvent e) {
//        Messages.showMessageDialog(e.getProject(), "Hello", "MyIdeaDemo", Messages.getInformationIcon());
//    }


//##  FILE PICKER EXEMPLE
//    @Override
//    public void actionPerformed(@NotNull AnActionEvent e) {
//        var fileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
//
//        fileChooserDescriptor.setTitle("Picker Directory");
//        fileChooserDescriptor.setDescription("My file chooser demo");
//        FileChooser.chooseFile(fileChooserDescriptor,
//                e.getProject(),
//                null,
//                (it) -> {
//                    Messages.showMessageDialog(e.getProject(), it.getPath(), "Path", Messages.getInformationIcon());
//                }
//        );
//
//    }
}
