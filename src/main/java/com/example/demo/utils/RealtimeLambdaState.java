package com.example.demo.utils;

import com.intellij.openapi.vfs.VirtualFile;

public interface RealtimeLambdaState <T> {
    T applyLimitOfComplexity(int complexity);
    T applyCurrentComplexity(int complexity);
    T applyCurrentClass(VirtualFile filename);
}
