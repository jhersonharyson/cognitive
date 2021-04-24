package com.cdd.service;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;

import java.util.Arrays;

import static com.intellij.openapi.ui.Messages.showMessageDialog;

public class Analyzer {


    public ComplexityCounter readPsiFile(PsiFile psiFile) {
        if (psiFile == null)
            return new ComplexityCounter();

        ComplexityCounter counter = new ComplexityCounter(new CddJsonResource());
        Arrays.stream(((PsiJavaFileImpl) psiFile).getClasses()).forEach(c -> c.accept(counter));

//        showMessageDialog(psiFile.getProject(), counter.toString(), "CDD Information", null);

        return counter;


    }
}
