package com.cdd.service;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;

import java.util.Objects;
import java.util.stream.Stream;

import static com.intellij.openapi.ui.Messages.showMessageDialog;

public class AnalyzerService {


    public ComplexityCounterService readPsiFile(PsiFile psiFile) {
        if (psiFile == null)
            return new ComplexityCounterService();

        ComplexityCounterService counter = new ComplexityCounterService(new CddJsonResourceService());
        var file = ((PsiJavaFileImpl) psiFile);
        Stream.of(file.getClasses(), Objects.requireNonNull(file.getImportList()).getAllImportStatements()).flatMap(Stream::of).forEach(c -> c.accept(counter));


//        showMessageDialog(psiFile.getProject(), counter.toString(), "CDD Information", null);

        return counter;


    }
}
