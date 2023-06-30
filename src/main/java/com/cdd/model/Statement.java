package com.cdd.model;

public enum Statement {
    IF_STATEMENT("if_statement"),
    METHOD("method"),
    BLOCK_STATEMENT("block_statement"),
    CODE_BLOCK("code_block"),
    CATCH_SECTION("catch_section"),
    TYPE_CAST_EXPRESSION("type_cast_expression"),
    WHILE_STATEMENT("while_statement"),
    DO_WHILE_STATEMENT("do_while_statement"),
    ANNOTATION_METHOD("annotation_method"),
    TRY_STATEMENT("try_statement"),
    LAMBDA_EXPRESSION("lambda_expression"),
    SWITCH_EXPRESSION("switch_expression"),
    THROW_STATEMENT("throw_statement"),
    ANONYMOUS_CLASS("anonymous_class"),
    CLASS_INITIALIZER("class_initializer"),
    CLASS("class"),
    FOR_STATEMENT("for_statement"),
    FOREACH_STATEMENT("foreach_statement"),
    LOCAL_VARIABLE("local_variable"),
    METHOD_CALL_EXPRESSION("method_call_expression"),
    IMPORT_STATIC_STATEMENT("import_static_statement"),
    SUPER_EXPRESSION("super_expression"),
    SWITCH_STATEMENT("switch_statement"),
    ANNOTATION("annotation"),
    YIELD_STATEMENT("yield_statement"),
    IMPORT_STATEMENT("import_statement"),
    CONTEXTUAL_COUPLING("contextual_coupling"),
    FEATURE_ENVY("feature_envy"),
    LONG_METHOD("long_method");

    private final String statement;

    Statement(String statement) {
        this.statement = statement;
    }

    public String getDescription(){
        return this.statement;
    }
}
