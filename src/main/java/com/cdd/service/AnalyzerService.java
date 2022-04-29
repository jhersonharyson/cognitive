package com.cdd.service;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.util.Objects;
import java.util.stream.Stream;

import static com.intellij.openapi.ui.Messages.showMessageDialog;

public class AnalyzerService {


    public ComplexityCounterService readPsiFile(PsiFile psiFile) {
        if (psiFile == null)
            return new ComplexityCounterService();

        ComplexityCounterService counter = new ComplexityCounterService(new CddJsonResourceService());

        if(ObjectUtils.isEmpty(counter.getRules())){
            throw new ResourceNotFoundException("resource not found !");
        }

        var file = ((PsiJavaFileImpl) psiFile);
        Stream.of(file.getClasses(), Objects.requireNonNull(file.getImportList()).getAllImportStatements()).flatMap(Stream::of).forEach(c -> c.accept(counter));


//        showMessageDialog(psiFile.getProject(), counter.toString(), "CDD Information", null);

        return counter;


    }
}
