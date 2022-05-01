package com.cdd.integrations.code.smell.jdeodorant.ide.ui;

import com.intellij.openapi.project.Project;
import com.cdd.integrations.code.smell.jdeodorant.IntelliJDeodorantBundle;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.RefactoringType.AbstractCandidateRefactoringGroup;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.typeStateChecking.TypeCheckRefactoringType;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.typeStateChecking.TypeCheckRefactoringType.AbstractTypeCheckCandidateRefactoring;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.typeStateChecking.TypeCheckRefactoringType.AbstractTypeCheckCandidateRefactoringGroup;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.typeStateChecking.TypeCheckElimination;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.typeStateChecking.TypeCheckEliminationGroup;

import java.util.List;

public class TypeCheckingTreeTableModel extends AbstractTreeTableModel {
    public TypeCheckingTreeTableModel(List<AbstractCandidateRefactoringGroup> candidateRefactoringGroups, String[] columnNames, Project project) {
        super(candidateRefactoringGroups, columnNames, new TypeCheckRefactoringType(project));
    }

    @Override
    public Object getValueAt(Object o, int index) {
        if (o instanceof AbstractTypeCheckCandidateRefactoringGroup) {
            AbstractTypeCheckCandidateRefactoringGroup abstractGroup = (AbstractTypeCheckCandidateRefactoringGroup) o;
            TypeCheckEliminationGroup group = (TypeCheckEliminationGroup) abstractGroup.getCandidateRefactoringGroup();

            switch (index) {
                case 0:
                    return group.toString();
                case 2:
                    return Integer.toString(group.getGroupSizeAtSystemLevel());
                case 3:
                    return Double.toString(group.getAverageGroupSizeAtClassLevel());
                case 4:
                    return String.format("%.2f", group.getAverageNumberOfStatementsInGroup());
            }
        }

        if (o instanceof AbstractTypeCheckCandidateRefactoring) {
            AbstractTypeCheckCandidateRefactoring abstractTypeCheckElimination = (AbstractTypeCheckCandidateRefactoring) o;
            TypeCheckElimination typeCheckElimination = (TypeCheckElimination) abstractTypeCheckElimination.getCandidateRefactoring();
            switch (index) {
                case 0:
                    return typeCheckElimination.toString();
                case 1:
                    if (typeCheckElimination.getExistingInheritanceTree() == null) {
                        return IntelliJDeodorantBundle.message("replace.type.code.with.state.strategy.name");
                    }
                    return IntelliJDeodorantBundle.message("replace.conditional.with.polymorphism.name");
                case 3:
                    return Integer.toString(typeCheckElimination.getGroupSizeAtClassLevel());
                case 4:
                    return String.format("%.2f", typeCheckElimination.getAverageNumberOfStatements());
            }
        }
        return "";
    }
}
