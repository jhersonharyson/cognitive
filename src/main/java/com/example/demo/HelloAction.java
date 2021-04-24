package com.example.demo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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

    @Override
    public void update(@NotNull AnActionEvent e) {
        this.actionPerformed3(e);
        super.update(e);
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
        final int[] numberOfBlockStatements = {0};
        final int[] numberOfCodeBlockStatements = {0};
        final int[] numberOfCatchSection = {0};
        final int[] numberOfTypeCastExpression = {0};
        final int[] numberOfWhileStatement = {0};
        final int[] numberOfImplicitVariable = {0};
        final int[] numberAnnotationMethods = {0};
        final int[] numberOfTryStatements = {0};
        final int[] numberOfLambdaExpression = {0};
        final int[] numberOfSwitchExpression = {0};
        final int[] numberOfYieldExpression = {0};

        Arrays.stream(((PsiJavaFileImpl) psiFile).getClasses()).forEach( c -> c.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitCodeBlock(PsiCodeBlock block) {
                super.visitCodeBlock(block);
                numberOfCodeBlockStatements[0]  += 1;
            }

            @Override
            public void visitBlockStatement(PsiBlockStatement statement) {
                super.visitBlockStatement(statement);
                numberOfBlockStatements[0] += 1;
            }

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

            @Override
            public void visitTryStatement(PsiTryStatement statement) {
                super.visitTryStatement(statement);
                numberOfTryStatements[0] += 1;
            }

            @Override
            public void visitCatchSection(PsiCatchSection section) {
                super.visitCatchSection(section);
                numberOfCatchSection[0] += 1;
            }

            @Override
            public void visitTypeCastExpression(PsiTypeCastExpression expression) {
                super.visitTypeCastExpression(expression);
                numberOfTypeCastExpression[0] += 1;
            }

            @Override
            public void visitWhileStatement(PsiWhileStatement statement) {
                super.visitWhileStatement(statement);
                numberOfWhileStatement[0] += 1;
            }

            @Override
            public void visitImplicitVariable(ImplicitVariable variable) {
                super.visitImplicitVariable(variable);
                numberOfImplicitVariable[0] += 1;
            }

            @Override
            public void visitAnnotationMethod(PsiAnnotationMethod method) {
                super.visitAnnotationMethod(method);
                numberAnnotationMethods[0] += 1;
            }

            @Override
            public void visitLambdaExpression(PsiLambdaExpression expression) {
                super.visitLambdaExpression(expression);
                numberOfLambdaExpression[0] += 1;
            }

            @Override
            public void visitSwitchExpression(PsiSwitchExpression expression) {
                super.visitSwitchExpression(expression);
                numberOfSwitchExpression[0] += 1;
            }

            @Override
            public void visitYieldStatement(PsiYieldStatement statement) {
                super.visitYieldStatement(statement);
                numberOfYieldExpression[0] += 1;
            }
        }));

        infoBuilder
                .append("number of if statements:")
                .append(numberOfIfStatements[0])
                .append("\n")
                .append("number of methods:")
                .append(numberMethods[0])
                .append("\n")
                .append("numberOfBlockStatements:")
                .append(numberOfBlockStatements[0])
                .append("\n")
                .append("numberOfCodeBlockStatements:")
                .append(numberOfCodeBlockStatements[0])
                .append("\n")
                .append("numberOfCatchSections:")
                .append(numberOfCatchSection[0])
                .append("\n")
                .append("numberOfTypeCastExpression:")
                .append(numberOfTypeCastExpression[0])
                .append("\n")
                .append("numberOfWhileStatement:")
                .append(numberOfWhileStatement[0])
                .append("\n")
                .append("numberOfImplicitVariable:")
                .append(numberOfImplicitVariable[0])
                .append("\n")
                .append("numberAnnotationMethods:")
                .append(numberAnnotationMethods[0])
                .append("\n")
                .append("numberOfTryStatements:")
                .append(numberOfTryStatements[0])
                .append("\n")
                .append("numberOfLambdaExpression:")
                .append(numberOfLambdaExpression[0])
                .append("\n")
                .append("numberOfYieldExpression:")
                .append(numberOfYieldExpression[0]);





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
