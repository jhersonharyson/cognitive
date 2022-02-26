package com.cdd.service;

import com.cdd.state.JavaClassesSettings;
import com.cdd.state.JavaClassesState;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiJavaFileImpl;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RegisterQualifierService {

    public static void register(PsiClass[] files){
        var qualifiers = Arrays.stream(files).map(PsiClass::getQualifiedName).collect(Collectors.toSet());
        JavaClassesService.addQualifier(qualifiers);
    }
}
