package com.cdd.integrations.code.smell.jdeodorant.ide.refactoring;

import org.jetbrains.annotations.NotNull;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.moveMethod.MoveMethodRefactoring;

public interface RefactoringVisitor<R> {
    @NotNull
    R visit(final @NotNull MoveMethodRefactoring refactoring);
}
