package com.cdd.model;

import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.util.stream.Stream;

public class RuleStatementMapper {

    @Builder
    @Data
    public static class ParameterToRuleStatement {
        private List<String> listOfParameters;
        private Statement statement;
    }

    private static final List<ParameterToRuleStatement> listOfMappers = new ArrayList<>();

    public RuleStatementMapper() {

    }

    public static void loadMapper(){
        listOfMappers.addAll(List.of(
                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("IF", "if", "IF_STATEMENT", "if_statement"))
                        .statement(Statement.IF_STATEMENT)
                        .build(),

                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("FOR", "for", "FOR_STATEMENT", "for_statement"))
                        .statement(Statement.FOR_STATEMENT)
                        .build(),

                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("FOREACH", "foreach", "FOREACH_STATEMENT", "foreach_statement"))
                        .statement(Statement.FOREACH_STATEMENT)
                        .build(),

                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("WHILE_STATEMENT", "while", "WHILE", "while_statement"))
                        .statement(Statement.WHILE_STATEMENT)
                        .build(),

                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("ANNOTATION", "annotation"))
                        .statement(Statement.ANNOTATION)
                        .build(),

                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("LOCAL_VARIABLE", "local_variable"))
                        .statement(Statement.LOCAL_VARIABLE)
                        .build(),

                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("LAMBDA_EXPRESSION", "lambda_expression", "LAMBDA", "lambda"))
                        .statement(Statement.LAMBDA_EXPRESSION)
                        .build(),

                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("TRY_STATEMENT", "try_statement", "TRY", "try"))
                        .statement(Statement.TRY_STATEMENT)
                        .build(),

                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("TYPE_CAST_EXPRESSION", "type_cast_expression", "TYPE_CAST", "type_cast", "CAST", "cast"))
                        .statement(Statement.TYPE_CAST_EXPRESSION)
                        .build(),

                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("IMPORT_STATIC_STATEMENT", "import_static_statement", "IMPORT_STATIC", "import_static"))
                        .statement(Statement.IMPORT_STATIC_STATEMENT)
                        .build(),

                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("IMPORT_STATEMENT", "import_statement", "IMPORT", "import"))
                        .statement(Statement.IMPORT_STATEMENT)
                        .build(),

                ParameterToRuleStatement
                        .builder()
                        .listOfParameters(List.of("CONTEXTUAL_COUPLING", "contextual_coupling"))
                        .statement(Statement.CONTEXTUAL_COUPLING)
                        .build()

        ));
    }

    public static ParameterToRuleStatement getByParameter(String parameter) {
        loadMapper();

        return listOfMappers
                .stream()
                .filter(mapper -> mapper.listOfParameters
                                        .stream()
                                        .anyMatch(param -> param.equals(parameter)))
                .findFirst()
                .orElse(null);
    }
}
