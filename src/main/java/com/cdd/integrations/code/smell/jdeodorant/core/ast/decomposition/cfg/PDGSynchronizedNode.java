package com.cdd.integrations.code.smell.jdeodorant.core.ast.decomposition.cfg;

import com.cdd.integrations.code.smell.jdeodorant.core.ast.FieldObject;
import com.cdd.integrations.code.smell.jdeodorant.core.ast.VariableDeclarationObject;

import java.util.Set;

class PDGSynchronizedNode extends PDGBlockNode {
    PDGSynchronizedNode(CFGSynchronizedNode cfgSynchronizedNode, Set<VariableDeclarationObject> variableDeclarationsInMethod,
                        Set<FieldObject> fieldsAccessedInMethod) {
        super(cfgSynchronizedNode, variableDeclarationsInMethod, fieldsAccessedInMethod);
        this.controlParent = cfgSynchronizedNode.getControlParent();
        determineDefinedAndUsedVariables();
    }
}
