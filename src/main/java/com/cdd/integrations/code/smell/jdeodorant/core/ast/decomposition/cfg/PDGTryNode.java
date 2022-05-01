package com.cdd.integrations.code.smell.jdeodorant.core.ast.decomposition.cfg;

import com.cdd.integrations.code.smell.jdeodorant.core.ast.FieldObject;
import com.cdd.integrations.code.smell.jdeodorant.core.ast.VariableDeclarationObject;
import com.cdd.integrations.code.smell.jdeodorant.core.ast.decomposition.AbstractStatement;
import com.cdd.integrations.code.smell.jdeodorant.core.ast.decomposition.CatchClauseObject;
import com.cdd.integrations.code.smell.jdeodorant.core.ast.decomposition.TryStatementObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PDGTryNode extends PDGBlockNode {
    PDGTryNode(CFGTryNode cfgTryNode, Set<VariableDeclarationObject> variableDeclarationsInMethod,
               Set<FieldObject> fieldsAccessedInMethod) {
        super(cfgTryNode, variableDeclarationsInMethod, fieldsAccessedInMethod);
        this.controlParent = cfgTryNode.getControlParent();
        determineDefinedAndUsedVariables();
    }

    boolean hasFinallyClauseClosingVariable(AbstractVariable variable) {
        return ((CFGTryNode) getCFGNode()).hasFinallyClauseClosingVariable(variable);
    }

    protected void determineDefinedAndUsedVariables() {
        super.determineDefinedAndUsedVariables();
        CFGNode cfgNode = getCFGNode();
        if (cfgNode.getStatement() instanceof TryStatementObject) {
            TryStatementObject tryStatement = (TryStatementObject) cfgNode.getStatement();
            List<AbstractStatement> statementsInCatchClausesAndFinallyBlock = new ArrayList<>();
            for (CatchClauseObject catchClause : tryStatement.getCatchClauses()) {
                statementsInCatchClausesAndFinallyBlock.add(catchClause.getBody());
            }
            if (tryStatement.getFinallyClause() != null) {
                statementsInCatchClausesAndFinallyBlock.add(tryStatement.getFinallyClause());
            }
            for (AbstractStatement statement : statementsInCatchClausesAndFinallyBlock) {
                definedVariables.addAll(statement.getDefinedLocalVariables());
                usedVariables.addAll(statement.getUsedLocalVariables());
            }
        }
    }
}
