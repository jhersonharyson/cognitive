package com.cdd.service;
// 1 complexity

import com.cdd.integrations.utils.CodeSmellIntegrationUtil;
import com.cdd.model.CddMetrics;
import com.cdd.model.Rule;
import com.cdd.model.Statement;
import com.cdd.utils.ContextualCouplingUtils;
import com.example.demo.utils.RealtimeState;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ComplexityCounterService extends JavaRecursiveElementVisitor {
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
    int numberOfImportStatement = 0;
    int numberOfContextualCoupling = 0;
    int numberOfFeatureEnvy = 0;

    private CddResource resource;

    public ComplexityCounterService(CddResource resource) {
        super();
        this.resource = resource;
    }

    public ComplexityCounterService() {
        super();
    }

    public int count() {
        return (numberOfIfStatement + numberOfMethods + numberOfBlockStatements + numberOfCodeBlocks +
                numberOfCatchSections + numberOfTypeCastExpression + numberOfWhileStatements +
                numberOfAnnotationMethods + numberOfTryStatements + numberOfLambdaExpression +
                numberOfSwitchExpression + numberOfThrowStatement + numberOfAnonymousClasses +
                numberOfCLassInitializers + numberOfClasses + numberOfForStatements + numberOfForeachStatements +
                numberOfLocalVariables + numberOfMethodCallExpressions + numberOfImportStaticStatement +
                numberOfSuperExpressions + numberOfSwitchStatements + numberOfAnnotations + numberOfYieldStatements +
                numberOfImportStatement + numberOfContextualCoupling + numberOfFeatureEnvy);
    }

    public int compute() {
        if (resource == null) {
            // TODO: Error alert
            throw new ResourceNotFoundException("Resource not found");
        }

        var metrics = this.getMetrics();


        try{

            if(!CodeSmellIntegrationUtil.taskRunning){
                var codeSmell = CodeSmellIntegrationUtil.getInstance();
                codeSmell.detectFeatureEnvy();
            }


        }catch (Exception ignore){

        }





        RealtimeState.getInstance().setLimitOfComplexity(metrics.getLimitOfComplexity());
        return metrics.getRules().stream().reduce(0, (subtotal, rule) -> subtotal + (rule.getCost() * this.statements.getOrDefault(Statement.valueOf(rule.getName()), 0)), Integer::sum);
    }

    public CddMetrics getMetrics() {
        if (resource == null) {
            // TODO: Error alert
            throw new ResourceNotFoundException("Resource not found");
        }

        CddMetrics metrics = new CddMetrics();
        metrics.setLimitOfComplexity(this.resource.limitOfComplexity());
        metrics.setRules(new ComplexityMetricsService().getRules(this.resource.loadMetrics()));
        return metrics;
    }

    public Set<Rule> getRules() throws ResourceNotFoundException {
        if (resource == null) {
            // TODO: Error alert
            throw new ResourceNotFoundException("Resource not found");
        }

        return new ComplexityMetricsService().getRules(this.resource.loadMetrics()).stream().map(rule -> {
            rule.setTimes(this.statements.getOrDefault(Statement.valueOf(rule.getName()), 0));
            return rule;
        }).collect(Collectors.toSet());
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
//        var assignStatementText = statement.getNode().getText();
//        if (assignStatementText.startsWith("var ") && (assignStatementText.contains("=new ") || assignStatementText.contains(" new "))) {
//            try {
//                var assign = assignStatementText.split("=")[1];
//
//                var sourceAssign = assign.split("\\(.*?\\)")[0];
//
//                var classNameWithOffset = sourceAssign.substring(sourceAssign.indexOf("new "));
//
//                var className = classNameWithOffset
//                        .substring(0, classNameWithOffset.contains("(") ? classNameWithOffset.indexOf("(") : classNameWithOffset.length())
//                        .replace("new ", "").trim();
//                var file = (PsiJavaFileImpl) statement.getContainingFile();
//                var classPackage = file.getPackageName() + "." + className;
//                if (isProjectClass(classPackage) && !Arrays.stream(file.getClasses()).anyMatch( psiClass -> isSameClass(psiClass.getQualifiedName()))) {
//                    // context coupling
//                    this.numberOfContextualCoupling += 1;
//                    this.statements.put(Statement.CONTEXTUAL_COUPLING, this.numberOfContextualCoupling);
//                }
//            } catch (Exception ignored) {
//            }
//        }
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
        this.numberOfImportStatement += 1;
        this.statements.put(Statement.IMPORT_STATEMENT, this.numberOfImportStatement);


        // FOR CONTEXTUAL_COUPLING
        try {
            var rules = new CddJsonResourceService().loadMetrics();

            var contextualCouplingRule = rules.stream()
                    .filter(rule -> Statement.CONTEXTUAL_COUPLING.name().equals(rule.getName()))
                    .findFirst()
                    .orElse(null);

            if (ObjectUtils.isNotEmpty(contextualCouplingRule)) {

                if (ContextualCouplingUtils.isContextualCoupling(statement.getText())) {
                    this.numberOfContextualCoupling += 1;
                    this.statements.put(Statement.CONTEXTUAL_COUPLING, this.numberOfContextualCoupling);
                }

            }
        } catch (ResourceNotFoundException ignore) {
            log.info("resource not found");
        }

        super.visitImportStatement(statement);
    }

    @Override
    public void visitImportStaticStatement(PsiImportStaticStatement statement) {
        this.numberOfImportStaticStatement += 1;
        this.statements.put(Statement.IMPORT_STATIC_STATEMENT, this.numberOfImportStaticStatement);

        // FOR CONTEXTUAL_COUPLING
        try {
            var rules = new CddJsonResourceService().loadMetrics();

            var contextualCouplingRule = rules.stream()
                    .filter(rule -> Statement.CONTEXTUAL_COUPLING.name().equals(rule.getName()))
                    .findFirst()
                    .orElse(null);

            if (ObjectUtils.isNotEmpty(contextualCouplingRule)) {

                if (ContextualCouplingUtils.isContextualCoupling(statement.getText())) {
                    this.numberOfContextualCoupling += 1;
                    this.statements.put(Statement.CONTEXTUAL_COUPLING, this.numberOfContextualCoupling);
                }

            }
        } catch (ResourceNotFoundException ignore) {
            log.info("resource not found");
        }

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
//        if(!isJavaClass(variable.getText())) {
//            this.numberOfContextualCoupling += 1;
//            this.statements.put(Statement.CONTEXTUAL_COUPLING, this.numberOfContextualCoupling);
//        }
        this.numberOfLocalVariables += 1;
        this.statements.put(Statement.LOCAL_VARIABLE, this.numberOfLocalVariables);
        super.visitLocalVariable(variable);
    }

    @Override
    public void visitMethod(PsiMethod method) {
        this.numberOfMethods += 1;
        this.statements.put(Statement.METHOD, this.numberOfMethods);

            if (ObjectUtils.isNotEmpty(CodeSmellIntegrationUtil.getInstance().refactorings)) {
                if(CodeSmellIntegrationUtil.getInstance().refactorings
                        .stream().anyMatch(refactoring -> refactoring.getMethod().equals(method))){
                    this.numberOfFeatureEnvy += 1;
                    this.statements.put(Statement.FEATURE_ENVY, this.numberOfFeatureEnvy);

                }
            }

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
//         var assignStatementText = expression.getNode().getText();
//        if (assignStatementText.startsWith("var ") && (assignStatementText.contains("=new ") || assignStatementText.contains(" new "))) {
//            try {
//                var assign = assignStatementText.split("=")[1];
//
//                var sourceAssign = assign.split("\\(.*?\\)")[0];
//
//                var classNameWithOffset = sourceAssign.substring(sourceAssign.indexOf("new "));
//
//                var className = classNameWithOffset
//                        .substring(0, classNameWithOffset.contains("(") ? classNameWithOffset.indexOf("(") : classNameWithOffset.length())
//                        .replace("new ", "").trim();
//                var file = (PsiJavaFileImpl) expression.getContainingFile();
//                var classPackage = file.getPackageName() + "." + className;
//                if (isProjectClass(classPackage) && !Arrays.stream(file.getClasses()).anyMatch( psiClass -> isSameClass(psiClass.getQualifiedName()))) {
//                    // context coupling
//                    this.numberOfContextualCoupling += 1;
//                    this.statements.put(Statement.CONTEXTUAL_COUPLING, this.numberOfContextualCoupling);
//                }
//            } catch (Exception ignored) {
//
//            }
//        }

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
//        var file = (PsiJavaFileImpl) parameter.getContainingFile();
//        var classPackage = file.getPackageName() + "." + parameter.getText();
//
//        if (isProjectClass(classPackage) && !Arrays.stream(file.getClasses()).anyMatch( psiClass -> isSameClass(psiClass.getQualifiedName()))) {
//            // context coupling
//            this.numberOfContextualCoupling += 1;
//            this.statements.put(Statement.CONTEXTUAL_COUPLING, this.numberOfContextualCoupling);
//        }
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
//        var file = (PsiJavaFileImpl) type.getContainingFile();
//        var classPackage = file.getPackageName() + "." + type.getText();
//        if (isProjectClass(classPackage) && !Arrays.stream(file.getClasses()).anyMatch( psiClass -> isSameClass(psiClass.getQualifiedName())) ) {
//            // context coupling
//            this.numberOfContextualCoupling += 1;
//            this.statements.put(Statement.CONTEXTUAL_COUPLING, this.numberOfContextualCoupling);
//        }
        super.visitTypeElement(type);
    }

    @Override
    public void visitTypeCastExpression(PsiTypeCastExpression expression) {
//        var file = (PsiJavaFileImpl) expression.getContainingFile();
//        var classPackage = file.getPackageName() + "." + expression.getText();
//        if (isProjectClass(classPackage) && !Arrays.stream(file.getClasses()).anyMatch( psiClass -> isSameClass(psiClass.getQualifiedName()))) {
//            // context coupling
//            this.numberOfContextualCoupling += 1;
//            this.statements.put(Statement.CONTEXTUAL_COUPLING, this.numberOfContextualCoupling);
//        }
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
                ", \n\tnumberOfImportStatement=" + numberOfImportStatement +
                ", \n\tnumberOfContextualCoupling=" + numberOfContextualCoupling +
                "\n}";
    }
}
