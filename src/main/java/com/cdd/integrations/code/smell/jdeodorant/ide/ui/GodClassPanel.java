package com.cdd.integrations.code.smell.jdeodorant.ide.ui;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import com.cdd.integrations.code.smell.jdeodorant.IntelliJDeodorantBundle;
import com.cdd.integrations.code.smell.jdeodorant.ide.fus.collectors.IntelliJDeodorantCounterCollector;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.RefactoringType.AbstractCandidateRefactoring;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.extractClass.ExtractClassRefactoringType;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.extractClass.ExtractClassRefactoringType.AbstractExtractClassRefactoring;

import java.util.Collections;

/**
 * Panel for God Class refactoring.
 */
public class GodClassPanel extends AbstractRefactoringPanel {
    private static final String[] COLUMN_NAMES = new String[]{IntelliJDeodorantBundle.message("god.class.panel.source.class"),
            IntelliJDeodorantBundle.message("god.class.panel.extractable.concept"),
            IntelliJDeodorantBundle.message("god.class.panel.source.extracted.members")};

    private static final int REFACTOR_DEPTH = 4;

    public GodClassPanel(@NotNull AnalysisScope scope) {
        super(scope,
                "god.class.identification.indicator",
                new ExtractClassRefactoringType(),
                new GodClassTreeTableModel(Collections.emptyList(), COLUMN_NAMES),
                REFACTOR_DEPTH);
    }

    @Override
    protected void logFound(Project project, Integer total) {
        IntelliJDeodorantCounterCollector.getInstance().refactoringFound(project, "extract.class", total);
    }

    @Override
    protected void doRefactor(AbstractCandidateRefactoring candidateRefactoring) {
        AbstractExtractClassRefactoring abstractRefactoring = (AbstractExtractClassRefactoring) getAbstractRefactoringFromAbstractCandidateRefactoring(candidateRefactoring);

        Project project = scope.getProject();
        ApplicationManager.getApplication().invokeAndWait(() -> {
            removeHighlighters(project);
            GodClassUserInputDialog dialog = new GodClassUserInputDialog(abstractRefactoring, this);
            dialog.show();
            if (dialog.isOK()) {
                showRefreshingProposal();
            }
        });
    }
}
