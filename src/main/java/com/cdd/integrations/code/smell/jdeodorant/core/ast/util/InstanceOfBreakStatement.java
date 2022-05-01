package com.cdd.integrations.code.smell.jdeodorant.core.ast.util;

import com.intellij.psi.PsiBreakStatement;
import com.intellij.psi.PsiStatement;

public class InstanceOfBreakStatement implements StatementInstanceChecker {

	public boolean instanceOf(PsiStatement statement) {
		return statement instanceof PsiBreakStatement;
	}

}
