package com.cdd.utils;

import com.cdd.model.Statement;
import com.cdd.service.CddJsonResourceService;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@NoArgsConstructor
public class ContextualCouplingUtils {
    public static final String IMPORT_STATEMENT = "import";
    public static final String WHITE_SPACE_STATEMENT = " ";
    public static final String STATIC_STATEMENT = "static";
    public static final String SEMICOLON_STATEMENT = ";";

    public static boolean isContextualCoupling(String importStatement) {

        var statement = importStatement
                .replace(IMPORT_STATEMENT, "")
                .replace(WHITE_SPACE_STATEMENT, "")
                .replace(STATIC_STATEMENT, "")
                .replace(SEMICOLON_STATEMENT, "");

        var rules = new CddJsonResourceService().loadMetrics();

        if(ObjectUtils.isEmpty(rules))
            return false;

        var contextualCouplingRule = rules.stream()
                .filter(rule -> Statement.CONTEXTUAL_COUPLING.name().equals(rule.getName()))
                .findFirst()
                .orElse(null);

        if (ObjectUtils.isNotEmpty(contextualCouplingRule)) {
            return contextualCouplingRule.getParameters().stream().anyMatch(statement::matches);
        }

        return false;
    }
}
