package com.cdd.service;

import com.cdd.model.Rule;
import com.cdd.model.Statement;
import com.intellij.psi.*;

import java.util.*;
import java.util.stream.Stream;

public class ComplexityMetricsService {

    public Set<Rule> getRules(Set<Rule> rules) {
        Set<Rule> listOfRules = new HashSet<>();

        rules.forEach(r -> {
            var rule = new Rule();
            rule.setName(r.getName());
            rule.setCost(r.getCost());

            if (Statement.IF_STATEMENT.name().equals(r.getName())) {
                rule.setObject(PsiIfStatement.class);
            } else if (Statement.FOR_STATEMENT.name().equals(r.getName())) {
                rule.setObject(PsiForStatement.class);
            } else if (Statement.BLOCK_STATEMENT.name().equals(r.getName())) {
                rule.setObject(PsiBlockStatement.class);
            } else if (Statement.ANNOTATION.name().equals(r.getName())) {
                rule.setObject(PsiAnnotation.class);
            } else if (Statement.ANNOTATION_METHOD.name().equals(r.getName())) {
                rule.setObject(PsiAnnotationMethod.class);
            } else if (Statement.ANONYMOUS_CLASS.name().equals(r.getName())) {
                rule.setObject(PsiAnonymousClass.class);
            } else if (Statement.CATCH_SECTION.name().equals(r.getName())) {
                rule.setObject(PsiCatchSection.class);
            } else if (Statement.CLASS_INITIALIZER.name().equals(r.getName())) {
                rule.setObject(PsiClassInitializer.class);
            } else if (Statement.CODE_BLOCK.name().equals(r.getName())) {
                rule.setObject(PsiCodeBlock.class);
            } else if (Statement.FOREACH_STATEMENT.name().equals(r.getName())) {
                rule.setObject(PsiForeachStatement.class);
            } else if (Statement.LOCAL_VARIABLE.name().equals(r.getName())) {
                rule.setObject(PsiLocalVariable.class);
            } else if (Statement.YIELD_STATEMENT.name().equals(r.getName())) {
                rule.setObject(PsiYieldStatement.class);
            } else if (Statement.WHILE_STATEMENT.name().equals(r.getName())) {
                rule.setObject(PsiWhileStatement.class);
            } else if (Statement.DO_WHILE_STATEMENT.name().equals(rule.getName())) {
                rule.setObject(PsiDoWhileStatement.class);
            } else if (Statement.TYPE_CAST_EXPRESSION.name().equals(r.getName())) {
                rule.setObject(PsiTypeCastExpression.class);
            } else if (Statement.TRY_STATEMENT.name().equals(r.getName())) {
                rule.setObject(PsiTryStatement.class);
            } else if (Statement.THROW_STATEMENT.name().equals(r.getName())) {
                rule.setObject(PsiThrowStatement.class);
            } else if (Statement.SWITCH_STATEMENT.name().equals(r.getName())) {
                rule.setObject(PsiSwitchStatement.class);
            } else if (Statement.SWITCH_EXPRESSION.name().equals(r.getName())) {
                rule.setObject(PsiSwitchExpression.class);
            } else if (Statement.SUPER_EXPRESSION.name().equals(r.getName())) {
                rule.setObject(PsiSuperExpression.class);
            } else if (Statement.METHOD_CALL_EXPRESSION.name().equals(r.getName())) {
                rule.setObject(PsiMethodCallExpression.class);
            } else if (Statement.CLASS.name().equals(r.getName())) {
                rule.setObject(PsiClass.class);
            } else if (Statement.LAMBDA_EXPRESSION.name().equals(r.getName())) {
                rule.setObject(PsiLambdaExpression.class);
            } else if (Statement.IMPORT_STATEMENT.name().equals(r.getName())) {
                rule.setObject(PsiImportStatement.class);
            } else if (Statement.IMPORT_STATIC_STATEMENT.name().equals(r.getName())) {
                rule.setObject(PsiImportStaticStatement.class);
            } else if (Statement.CONTEXTUAL_COUPLING.name().equals(r.getName())) {
                //rule.setObject(Arrays.asList(PsiTypeElement.class, PsiParameter.class, PsiTypeCastExpression.class, PsiDeclarationStatement.class));
                rule.setObject(Arrays.asList(PsiImportStatement.class, PsiImportStaticStatement.class)) ;
            } else if (Statement.FEATURE_ENVY.name().equals(r.getName())) {
                rule.setObject(PsiMethod.class);
            }

            if (rule.getObject() != null)
                listOfRules.add(rule);
        });

        return listOfRules;
    }


    public static Set<Object> getListOfStatements(Set<Rule> rules) {
        Set<Object> statements = new HashSet<>();

        rules.forEach(rule -> {
            if (Statement.IF_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiIfStatement.class);
            } else if (Statement.FOR_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiForStatement.class);
            } else if (Statement.BLOCK_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiBlockStatement.class);
            } else if (Statement.ANNOTATION.name().equals(rule.getName())) {
                statements.add(PsiAnnotation.class);
            } else if (Statement.ANNOTATION_METHOD.name().equals(rule.getName())) {
                statements.add(PsiAnnotationMethod.class);
            } else if (Statement.ANONYMOUS_CLASS.name().equals(rule.getName())) {
                statements.add(PsiAnonymousClass.class);
            } else if (Statement.CATCH_SECTION.name().equals(rule.getName())) {
                statements.add(PsiCatchSection.class);
            } else if (Statement.CLASS_INITIALIZER.name().equals(rule.getName())) {
                statements.add(PsiClassInitializer.class);
            } else if (Statement.CODE_BLOCK.name().equals(rule.getName())) {
                statements.add(PsiCodeBlock.class);
            } else if (Statement.FOREACH_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiForeachStatement.class);
            } else if (Statement.LOCAL_VARIABLE.name().equals(rule.getName())) {
                statements.add(PsiLocalVariable.class);
            } else if (Statement.YIELD_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiYieldStatement.class);
            } else if (Statement.WHILE_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiWhileStatement.class);
            } else if (Statement.DO_WHILE_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiDoWhileStatement.class);
            } else if (Statement.TYPE_CAST_EXPRESSION.name().equals(rule.getName())) {
                statements.add(PsiTypeCastExpression.class);
            } else if (Statement.TRY_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiTryStatement.class);
            } else if (Statement.THROW_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiThrowStatement.class);
            } else if (Statement.SWITCH_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiSwitchStatement.class);
            } else if (Statement.SWITCH_EXPRESSION.name().equals(rule.getName())) {
                statements.add(PsiSwitchExpression.class);
            } else if (Statement.SUPER_EXPRESSION.name().equals(rule.getName())) {
                statements.add(PsiSuperExpression.class);
            } else if (Statement.METHOD_CALL_EXPRESSION.name().equals(rule.getName())) {
                statements.add(PsiMethodCallExpression.class);
            } else if (Statement.CLASS.name().equals(rule.getName())) {
                statements.add(PsiClass.class);
            } else if (Statement.LAMBDA_EXPRESSION.name().equals(rule.getName())) {
                statements.add(PsiLambdaExpression.class);
            } else if (Statement.IMPORT_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiImportStatement.class);
            } else if (Statement.IMPORT_STATIC_STATEMENT.name().equals(rule.getName())) {
                statements.add(PsiImportStaticStatement.class);
            }

            else if (Statement.CONTEXTUAL_COUPLING.name().equals(rule.getName())) {
//                statements.add(PsiTypeElement.class);
//                statements.add(PsiParameter.class);
//                statements.add(PsiTypeCastExpression.class);
//                statements.add(PsiDeclarationStatement.class);
                statements.add(PsiImportStatement.class);
                statements.add(PsiImportStaticStatement.class);
            }

            else if (Statement.FEATURE_ENVY.name().equals(rule.getName())) {
                statements.add(PsiMethod.class);
            }
        });

        return statements;
    }

    public void hasRule(Rule rule) {

    }
}
