package com.cdd.integrations.code.smell.jdeodorant.core.ast;

import com.intellij.psi.PsiNewExpression;

import static com.cdd.integrations.code.smell.jdeodorant.utils.PsiUtils.toPointer;

public class ArrayCreationObject extends CreationObject {

    public ArrayCreationObject(TypeObject type) {
        super(type);
    }

    public void setArrayCreation(PsiNewExpression creation) {
        this.creation = toPointer(creation);
    }
}
