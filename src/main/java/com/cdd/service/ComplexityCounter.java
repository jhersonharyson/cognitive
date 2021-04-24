package com.cdd.service;

import com.cdd.model.CddMetrics;
import com.cdd.model.Rule;
import com.cdd.model.Statement;
import com.example.demo.utils.RealtimeState;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.*;
import com.intellij.psi.search.FilenameIndex;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class ComplexityCounter extends JavaRecursiveElementVisitor {
    HashMap<Statement, Integer> statements = new HashMap<>();

    int numberOfIfStatement = 0;
    int numberOfMethods = 0;
    int numberOfBlockStatements = 0;
    int numberOfCodeBlocks = 0;
    int numberOfCatchSections = 0;
    int numberOfTypeCastExpression = 0;
    int numberOfWhileStatements = 0;
    int numberOfAnnotationMethods = 0;
    int numberOfTryStatements = 0;
    int numberOfLambdaExpression = 0;
    int numberOfSwitchExpression = 0;
    int numberOfThrowStatement = 0;
    int numberOfAnonymousClasses = 0;
    int numberOfCLassInitializers = 0;
    int numberOfClasses = 0;
    int numberOfForStatements = 0;
    int numberOfForeachStatements = 0;
    int numberOfLocalVariables = 0;
    int numberOfMethodCallExpressions = 0;
    int numberOfImportStaticStatement = 0;
    int numberOfSuperExpressions = 0;
    int numberOfSwitchStatements = 0;
    int numberOfAnnotations = 0;
    int numberOfYieldStatements = 0;
    int numberOfDoWhileStatements = 0;

    private CddResource resource;

    public ComplexityCounter(CddResource resource) {
        super();
        this.resource = resource;
    }

    public ComplexityCounter() {
        super();
    }

    public int count() {


        return (numberOfIfStatement + numberOfMethods + numberOfBlockStatements + numberOfCodeBlocks +
                numberOfCatchSections + numberOfTypeCastExpression + numberOfWhileStatements +
                numberOfAnnotationMethods + numberOfTryStatements + numberOfLambdaExpression +
                numberOfSwitchExpression + numberOfThrowStatement + numberOfAnonymousClasses +
                numberOfCLassInitializers + numberOfClasses + numberOfForStatements + numberOfForeachStatements +
                numberOfLocalVariables + numberOfMethodCallExpressions + numberOfImportStaticStatement +
                numberOfSuperExpressions + numberOfSwitchStatements + numberOfAnnotations + numberOfYieldStatements);
    }

    public int compute() {
        CddMetrics cdd = new CddMetrics();

        if (resource == null) {
            // TODO: Error alert
            return 0;
        }

        RealtimeState.getInstance().setLimitOfComplexity(this.resource.limitOfComplexity());
        cdd.setLimitOfComplexity(this.resource.limitOfComplexity());
        cdd.setRules(new ComplexityMetricsService().getRules(this.resource.loadMetrics()));

        return cdd.getRules().stream().reduce(0, (subtotal, rule) -> subtotal + (rule.getCost() * this.statements.getOrDefault(Statement.valueOf(rule.getName()), 0)), Integer::sum);

    }

    private int computeCost(Statement statement) {
        return 0;
    }

    @Override
    public void visitElement(@NotNull PsiElement element) {
        super.visitElement(element);
    }

    @Override
    public void visitReferenceExpression(PsiReferenceExpression expression) {
        super.visitReferenceExpression(expression);
    }

    @Override
    public void visitAnonymousClass(PsiAnonymousClass aClass) {
        this.numberOfAnonymousClasses += 1;
        this.statements.put(Statement.ANONYMOUS_CLASS, this.numberOfAnonymousClasses);
        super.visitAnonymousClass(aClass);
    }

    @Override
    public void visitArrayAccessExpression(PsiArrayAccessExpression expression) {
        super.visitArrayAccessExpression(expression);
    }

    @Override
    public void visitArrayInitializerExpression(PsiArrayInitializerExpression expression) {
        super.visitArrayInitializerExpression(expression);
    }

    @Override
    public void visitAssertStatement(PsiAssertStatement statement) {
        super.visitAssertStatement(statement);
    }

    @Override
    public void visitAssignmentExpression(PsiAssignmentExpression expression) {
        super.visitAssignmentExpression(expression);
    }

    @Override
    public void visitBinaryExpression(PsiBinaryExpression expression) {
        super.visitBinaryExpression(expression);
    }

    @Override
    public void visitBlockStatement(PsiBlockStatement statement) {
        this.numberOfBlockStatements += 1;
        this.statements.put(Statement.BLOCK_STATEMENT, this.numberOfBlockStatements);
        super.visitBlockStatement(statement);
    }

    @Override
    public void visitBreakStatement(PsiBreakStatement statement) {
        super.visitBreakStatement(statement);
    }

    @Override
    public void visitYieldStatement(PsiYieldStatement statement) {
        this.numberOfYieldStatements += 1;
        this.statements.put(Statement.YIELD_STATEMENT, this.numberOfYieldStatements);
        super.visitYieldStatement(statement);
    }

    @Override
    public void visitClass(PsiClass aClass) {
        this.numberOfClasses += 1;
        this.statements.put(Statement.CLASS, this.numberOfClasses);
        super.visitClass(aClass);
    }

    @Override
    public void visitClassInitializer(PsiClassInitializer initializer) {
        this.numberOfCLassInitializers += 1;
        this.statements.put(Statement.CLASS_INITIALIZER, this.numberOfCLassInitializers);
        super.visitClassInitializer(initializer);
    }

    @Override
    public void visitClassObjectAccessExpression(PsiClassObjectAccessExpression expression) {
        super.visitClassObjectAccessExpression(expression);
    }

    @Override
    public void visitCodeBlock(PsiCodeBlock block) {
        this.numberOfCodeBlocks += 1;
        this.statements.put(Statement.CODE_BLOCK, this.numberOfCodeBlocks);
        super.visitCodeBlock(block);
    }

    @Override
    public void visitConditionalExpression(PsiConditionalExpression expression) {
        super.visitConditionalExpression(expression);
    }

    @Override
    public void visitContinueStatement(PsiContinueStatement statement) {
        super.visitContinueStatement(statement);
    }

    @Override
    public void visitDeclarationStatement(PsiDeclarationStatement statement) {
        super.visitDeclarationStatement(statement);
    }

    @Override
    public void visitDocComment(PsiDocComment comment) {
        super.visitDocComment(comment);
    }

    @Override
    public void visitDocTag(PsiDocTag tag) {
        super.visitDocTag(tag);
    }

    @Override
    public void visitDocTagValue(PsiDocTagValue value) {
        super.visitDocTagValue(value);
    }

    @Override
    public void visitDoWhileStatement(PsiDoWhileStatement statement) {
        this.numberOfDoWhileStatements += 1;
        this.statements.put(Statement.WHILE_STATEMENT, this.numberOfDoWhileStatements);
        super.visitDoWhileStatement(statement);
    }

    @Override
    public void visitEmptyStatement(PsiEmptyStatement statement) {
        super.visitEmptyStatement(statement);
    }

    @Override
    public void visitExpression(PsiExpression expression) {
        super.visitExpression(expression);
    }

    @Override
    public void visitExpressionList(PsiExpressionList list) {
        super.visitExpressionList(list);
    }

    @Override
    public void visitExpressionListStatement(PsiExpressionListStatement statement) {
        super.visitExpressionListStatement(statement);
    }

    @Override
    public void visitExpressionStatement(PsiExpressionStatement statement) {
        super.visitExpressionStatement(statement);
    }

    @Override
    public void visitField(PsiField field) {
        super.visitField(field);
    }

    @Override
    public void visitForStatement(PsiForStatement statement) {
        this.numberOfForStatements += 1;
        this.statements.put(Statement.FOR_STATEMENT, this.numberOfForStatements);
        super.visitForStatement(statement);
    }

    @Override
    public void visitForeachStatement(PsiForeachStatement statement) {
        this.numberOfForeachStatements += 1;
        this.statements.put(Statement.FOREACH_STATEMENT, this.numberOfForeachStatements);
        super.visitForeachStatement(statement);
    }

    @Override
    public void visitIdentifier(PsiIdentifier identifier) {
        super.visitIdentifier(identifier);
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        this.numberOfIfStatement += 1;
        this.statements.put(Statement.IF_STATEMENT, this.numberOfIfStatement);
        super.visitIfStatement(statement);
    }

    @Override
    public void visitImportList(PsiImportList list) {
        super.visitImportList(list);
    }

    @Override
    public void visitImportStatement(PsiImportStatement statement) {
        super.visitImportStatement(statement);
    }

    @Override
    public void visitImportStaticStatement(PsiImportStaticStatement statement) {
        this.numberOfImportStaticStatement += 1;
        this.statements.put(Statement.IMPORT_STATIC_STATEMENT, this.numberOfImportStaticStatement);
        super.visitImportStaticStatement(statement);
    }

    @Override
    public void visitInlineDocTag(PsiInlineDocTag tag) {
        super.visitInlineDocTag(tag);
    }

    @Override
    public void visitInstanceOfExpression(PsiInstanceOfExpression expression) {
        super.visitInstanceOfExpression(expression);
    }

    @Override
    public void visitJavaToken(PsiJavaToken token) {
        super.visitJavaToken(token);
    }

    @Override
    public void visitKeyword(PsiKeyword keyword) {
        super.visitKeyword(keyword);
    }

    @Override
    public void visitLabeledStatement(PsiLabeledStatement statement) {
        super.visitLabeledStatement(statement);
    }

    @Override
    public void visitLiteralExpression(PsiLiteralExpression expression) {
        super.visitLiteralExpression(expression);
    }

    @Override
    public void visitLocalVariable(PsiLocalVariable variable) {
        this.numberOfLocalVariables += 1;
        this.statements.put(Statement.LOCAL_VARIABLE, this.numberOfLocalVariables);
        super.visitLocalVariable(variable);
    }

    @Override
    public void visitMethod(PsiMethod method) {
        this.numberOfMethods += 1;
        this.statements.put(Statement.METHOD, this.numberOfMethods);
        super.visitMethod(method);
    }

    @Override
    public void visitMethodCallExpression(PsiMethodCallExpression expression) {
        this.numberOfMethodCallExpressions += 1;
        this.statements.put(Statement.METHOD_CALL_EXPRESSION, this.numberOfMethodCallExpressions);
        super.visitMethodCallExpression(expression);
    }

    @Override
    public void visitCallExpression(PsiCallExpression callExpression) {
        super.visitCallExpression(callExpression);
    }

    @Override
    public void visitModifierList(PsiModifierList list) {
        super.visitModifierList(list);
    }

    @Override
    public void visitNewExpression(PsiNewExpression expression) {
        super.visitNewExpression(expression);
    }

    @Override
    public void visitPackage(PsiPackage aPackage) {
        super.visitPackage(aPackage);
    }

    @Override
    public void visitPackageStatement(PsiPackageStatement statement) {
        super.visitPackageStatement(statement);
    }

    @Override
    public void visitParameter(PsiParameter parameter) {
        super.visitParameter(parameter);
    }

    @Override
    public void visitRecordComponent(PsiRecordComponent recordComponent) {
        super.visitRecordComponent(recordComponent);
    }

    @Override
    public void visitReceiverParameter(PsiReceiverParameter parameter) {
        super.visitReceiverParameter(parameter);
    }

    @Override
    public void visitParameterList(PsiParameterList list) {
        super.visitParameterList(list);
    }

    @Override
    public void visitRecordHeader(PsiRecordHeader recordHeader) {
        super.visitRecordHeader(recordHeader);
    }

    @Override
    public void visitParenthesizedExpression(PsiParenthesizedExpression expression) {
        super.visitParenthesizedExpression(expression);
    }

    @Override
    public void visitUnaryExpression(PsiUnaryExpression expression) {
        super.visitUnaryExpression(expression);
    }

    @Override
    public void visitPostfixExpression(PsiPostfixExpression expression) {
        super.visitPostfixExpression(expression);
    }

    @Override
    public void visitPrefixExpression(PsiPrefixExpression expression) {
        super.visitPrefixExpression(expression);
    }

    @Override
    public void visitReferenceElement(PsiJavaCodeReferenceElement reference) {
        super.visitReferenceElement(reference);
    }

    @Override
    public void visitImportStaticReferenceElement(PsiImportStaticReferenceElement reference) {
        super.visitImportStaticReferenceElement(reference);
    }

    @Override
    public void visitMethodReferenceExpression(PsiMethodReferenceExpression expression) {
        super.visitMethodReferenceExpression(expression);
    }

    @Override
    public void visitReferenceList(PsiReferenceList list) {
        super.visitReferenceList(list);
    }

    @Override
    public void visitReferenceParameterList(PsiReferenceParameterList list) {
        super.visitReferenceParameterList(list);
    }

    @Override
    public void visitTypeParameterList(PsiTypeParameterList list) {
        super.visitTypeParameterList(list);
    }

    @Override
    public void visitReturnStatement(PsiReturnStatement statement) {
        super.visitReturnStatement(statement);
    }

    @Override
    public void visitStatement(PsiStatement statement) {
        super.visitStatement(statement);
    }

    @Override
    public void visitSuperExpression(PsiSuperExpression expression) {
        this.numberOfSuperExpressions += 1;
        this.statements.put(Statement.METHOD_CALL_EXPRESSION, this.numberOfMethodCallExpressions);
        super.visitSuperExpression(expression);
    }

    @Override
    public void visitSwitchLabelStatement(PsiSwitchLabelStatement statement) {
        super.visitSwitchLabelStatement(statement);
    }

    @Override
    public void visitSwitchLabeledRuleStatement(PsiSwitchLabeledRuleStatement statement) {
        super.visitSwitchLabeledRuleStatement(statement);
    }

    @Override
    public void visitSwitchStatement(PsiSwitchStatement statement) {
        this.numberOfSwitchStatements += 1;
        this.statements.put(Statement.SWITCH_STATEMENT, this.numberOfSwitchStatements);
        super.visitSwitchStatement(statement);
    }

    @Override
    public void visitSynchronizedStatement(PsiSynchronizedStatement statement) {
        super.visitSynchronizedStatement(statement);
    }

    @Override
    public void visitThisExpression(PsiThisExpression expression) {
        super.visitThisExpression(expression);
    }

    @Override
    public void visitThrowStatement(PsiThrowStatement statement) {
        this.numberOfThrowStatement += 1;
        this.statements.put(Statement.THROW_STATEMENT, this.numberOfThrowStatement);
        super.visitThrowStatement(statement);
    }

    @Override
    public void visitTryStatement(PsiTryStatement statement) {
        this.numberOfTryStatements += 1;
        this.statements.put(Statement.TRY_STATEMENT, this.numberOfTryStatements);
        super.visitTryStatement(statement);
    }

    @Override
    public void visitCatchSection(PsiCatchSection section) {
        this.numberOfCatchSections += 1;
        this.statements.put(Statement.CATCH_SECTION, this.numberOfCatchSections);
        super.visitCatchSection(section);
    }

    @Override
    public void visitResourceList(PsiResourceList resourceList) {
        super.visitResourceList(resourceList);
    }

    @Override
    public void visitResourceVariable(PsiResourceVariable variable) {
        super.visitResourceVariable(variable);
    }

    @Override
    public void visitResourceExpression(PsiResourceExpression expression) {
        super.visitResourceExpression(expression);
    }

    @Override
    public void visitTypeElement(PsiTypeElement type) {
        super.visitTypeElement(type);
    }

    @Override
    public void visitTypeCastExpression(PsiTypeCastExpression expression) {
        this.numberOfTypeCastExpression += 1;
        this.statements.put(Statement.TYPE_CAST_EXPRESSION, this.numberOfTypeCastExpression);
        super.visitTypeCastExpression(expression);
    }

    @Override
    public void visitVariable(PsiVariable variable) {
        super.visitVariable(variable);
    }

    @Override
    public void visitWhileStatement(PsiWhileStatement statement) {
        this.numberOfWhileStatements += 1;
        this.statements.put(Statement.WHILE_STATEMENT, this.numberOfWhileStatements);
        super.visitWhileStatement(statement);
    }

    @Override
    public void visitJavaFile(PsiJavaFile file) {
        super.visitJavaFile(file);
    }

    @Override
    public void visitImplicitVariable(ImplicitVariable variable) {
//        this.numberOfImplicitVariables += 1;
        super.visitImplicitVariable(variable);
    }

    @Override
    public void visitDocToken(PsiDocToken token) {
        super.visitDocToken(token);
    }

    @Override
    public void visitTypeParameter(PsiTypeParameter classParameter) {
        super.visitTypeParameter(classParameter);
    }

    @Override
    public void visitAnnotation(PsiAnnotation annotation) {
        this.numberOfAnnotations += 1;
        this.statements.put(Statement.ANNOTATION, this.numberOfAnnotations);
        super.visitAnnotation(annotation);
    }

    @Override
    public void visitAnnotationParameterList(PsiAnnotationParameterList list) {
        super.visitAnnotationParameterList(list);
    }

    @Override
    public void visitAnnotationArrayInitializer(PsiArrayInitializerMemberValue initializer) {
        super.visitAnnotationArrayInitializer(initializer);
    }

    @Override
    public void visitNameValuePair(PsiNameValuePair pair) {
        super.visitNameValuePair(pair);
    }

    @Override
    public void visitAnnotationMethod(PsiAnnotationMethod method) {
        this.numberOfAnnotationMethods += 1;
        this.statements.put(Statement.ANNOTATION_METHOD, this.numberOfAnnotationMethods);
        super.visitAnnotationMethod(method);
    }


    @Override
    public void visitEnumConstant(PsiEnumConstant enumConstant) {
        super.visitEnumConstant(enumConstant);
    }

    @Override
    public void visitEnumConstantInitializer(PsiEnumConstantInitializer enumConstantInitializer) {
        super.visitEnumConstantInitializer(enumConstantInitializer);
    }

    @Override
    public void visitCodeFragment(JavaCodeFragment codeFragment) {
        super.visitCodeFragment(codeFragment);
    }

    @Override
    public void visitPolyadicExpression(PsiPolyadicExpression expression) {
        super.visitPolyadicExpression(expression);
    }

    @Override
    public void visitLambdaExpression(PsiLambdaExpression expression) {
        this.numberOfLambdaExpression += 1;
        this.statements.put(Statement.LAMBDA_EXPRESSION, this.numberOfLambdaExpression);
        super.visitLambdaExpression(expression);
    }

    @Override
    public void visitSwitchExpression(PsiSwitchExpression expression) {
        this.numberOfSwitchExpression += 1;
        this.statements.put(Statement.SWITCH_EXPRESSION, this.numberOfSwitchExpression);
        super.visitSwitchExpression(expression);
    }

    @Override
    public void visitModule(PsiJavaModule module) {
        super.visitModule(module);
    }

    @Override
    public void visitModuleReferenceElement(PsiJavaModuleReferenceElement refElement) {
        super.visitModuleReferenceElement(refElement);
    }

    @Override
    public void visitModuleStatement(PsiStatement statement) {
        super.visitModuleStatement(statement);
    }

    @Override
    public void visitRequiresStatement(PsiRequiresStatement statement) {
        super.visitRequiresStatement(statement);
    }

    @Override
    public void visitPackageAccessibilityStatement(PsiPackageAccessibilityStatement statement) {
        super.visitPackageAccessibilityStatement(statement);
    }

    @Override
    public void visitUsesStatement(PsiUsesStatement statement) {
        super.visitUsesStatement(statement);
    }

    @Override
    public void visitProvidesStatement(PsiProvidesStatement statement) {
        super.visitProvidesStatement(statement);
    }

    @Override
    public void visitPattern(PsiPattern pattern) {
        super.visitPattern(pattern);
    }

    @Override
    public void visitTypeTestPattern(PsiTypeTestPattern pattern) {
        super.visitTypeTestPattern(pattern);
    }

    @Override
    public void visitPatternVariable(PsiPatternVariable variable) {
        super.visitPatternVariable(variable);
    }

    @Override
    public String toString() {
        return "ComplexityCounter{" +
                "\n\tnumberOfIfStatement=" + numberOfIfStatement +
                ", \n\tnumberOfMethods=" + numberOfMethods +
                ", \n\tnumberOfBlockStatements=" + numberOfBlockStatements +
                ", \n\tnumberOfCodeBlocks=" + numberOfCodeBlocks +
                ", \n\tnumberOfCatchSections=" + numberOfCatchSections +
                ", \n\tnumberOfTypeCastExpression=" + numberOfTypeCastExpression +
                ", \n\tnumberOfWhileStatements=" + numberOfWhileStatements +
                ", \n\tnumberOfAnnotationMethods=" + numberOfAnnotationMethods +
                ", \n\tnumberOfTryStatements=" + numberOfTryStatements +
                ", \n\tnumberOfLambdaExpression=" + numberOfLambdaExpression +
                ", \n\tnumberOfSwitchExpression=" + numberOfSwitchExpression +
                ", \n\tnumberOfThrowStatement=" + numberOfThrowStatement +
                ", \n\tnumberOfAnonymousClasses=" + numberOfAnonymousClasses +
                ", \n\tnumberOfCLassInitializers=" + numberOfCLassInitializers +
                ", \n\tnumberOfClasses=" + numberOfClasses +
                ", \n\tnumberOfForStatements=" + numberOfForStatements +
                ", \n\tnumberOfForeachStatements=" + numberOfForeachStatements +
                ", \n\tnumberOfLocalVariables=" + numberOfLocalVariables +
                ", \n\tnumberOfMethodCallExpressions=" + numberOfMethodCallExpressions +
                ", \n\tnumberOfImportStaticStatement=" + numberOfImportStaticStatement +
                ", \n\tnumberOfSuperExpressions=" + numberOfSuperExpressions +
                ", \n\tnumberOfSwitchStatements=" + numberOfSwitchStatements +
                ", \n\tnumberOfAnnotations=" + numberOfAnnotations +
                ", \n\tnumberOfYieldStatements=" + numberOfYieldStatements +
                ", \n\tnumberOfDoWhileStatements=" + numberOfDoWhileStatements +
                "\n}";
    }
}
