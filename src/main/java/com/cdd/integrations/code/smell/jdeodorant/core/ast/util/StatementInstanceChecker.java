package com.cdd.integrations.code.smell.jdeodorant.core.ast.util;

import com.intellij.psi.PsiStatement;

interface StatementInstanceChecker {
	boolean instanceOf(PsiStatement statement);
}
