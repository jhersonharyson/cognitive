package com.cdd.integrations.code.smell.jdeodorant.core.ast.util;

import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiSwitchStatement;

public class InstanceOfSwitchStatement implements StatementInstanceChecker {

	public boolean instanceOf(PsiStatement statement) {
		return statement instanceof PsiSwitchStatement;
	}

}
