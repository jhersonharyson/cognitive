package com.cdd.integrations.code.smell.jdeodorant.core.ast.util;

import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiNewExpression;

public class InstanceOfArrayCreation implements ExpressionInstanceChecker {

	public boolean instanceOf(PsiExpression expression) {
		return expression instanceof PsiNewExpression;
	}

}
