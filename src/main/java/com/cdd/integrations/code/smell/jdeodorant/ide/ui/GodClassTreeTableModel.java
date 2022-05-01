package com.cdd.integrations.code.smell.jdeodorant.ide.ui;

import com.cdd.integrations.code.smell.jdeodorant.core.distance.ExtractClassCandidateGroup;
import com.cdd.integrations.code.smell.jdeodorant.core.distance.ExtractClassCandidateRefactoring;
import com.cdd.integrations.code.smell.jdeodorant.core.distance.ExtractedConcept;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.RefactoringType.AbstractCandidateRefactoringGroup;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.extractClass.ExtractClassRefactoringType;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.extractClass.ExtractClassRefactoringType.AbstractExtractClassCandidateRefactoring;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.extractClass.ExtractClassRefactoringType.AbstractExtractClassCandidateRefactoringGroup;

import java.util.ArrayList;
import java.util.List;

public class GodClassTreeTableModel extends AbstractTreeTableModel {
    public GodClassTreeTableModel(List<AbstractCandidateRefactoringGroup> candidateRefactoringGroups, String[] columnNames) {
        super(candidateRefactoringGroups, columnNames, new ExtractClassRefactoringType());
    }

    @Override
    public Object getValueAt(Object o, int index) {
        if (o instanceof AbstractExtractClassCandidateRefactoringGroup) {
            AbstractExtractClassCandidateRefactoringGroup abstractExtractClassCandidateRefactoringGroup =
                    (AbstractExtractClassCandidateRefactoringGroup) o;
            ExtractClassCandidateGroup group = (ExtractClassCandidateGroup) abstractExtractClassCandidateRefactoringGroup.getCandidateRefactoringGroup();

            if (index == 0) {
                return group.getSource();
            } else {
                return "";
            }
        } else if (o instanceof AbstractExtractClassCandidateRefactoring) {
            AbstractExtractClassCandidateRefactoring abstractCandidateRefactoring = (AbstractExtractClassCandidateRefactoring) o;
            ExtractClassCandidateRefactoring candidateRefactoring = (ExtractClassCandidateRefactoring) abstractCandidateRefactoring.getCandidateRefactoring();
            switch (index) {
                case 0:
                    return "";
                case 1:
                    return candidateRefactoring.getSourceEntity();
                case 2:
                    return candidateRefactoring.getExtractedFieldFragments().size() + "/" + candidateRefactoring.getExtractedMethods().size();
            }
        }

        return "";
    }

    @Override
    public List<?> getChildren(Object parent) {
        if (parent instanceof AbstractExtractClassCandidateRefactoringGroup) {
            AbstractExtractClassCandidateRefactoringGroup abstractGroup = (AbstractExtractClassCandidateRefactoringGroup) parent;
            ExtractClassCandidateGroup group = (ExtractClassCandidateGroup) abstractGroup.getCandidateRefactoringGroup();

            return group.getExtractedConcepts();
        } else if (parent instanceof ExtractedConceptAndChildren) {
            ExtractedConceptAndChildren concept = (ExtractedConceptAndChildren) parent;
            return concept.children;
        } else {
            return super.getChildren(parent);
        }
    }

    @Override
    public Object getChild(Object parent, int index) {
        Object child = super.getChild(parent, index);

        if (parent instanceof AbstractExtractClassCandidateRefactoringGroup
                && child instanceof ExtractedConcept) {
            return new ExtractedConceptAndChildren((ExtractedConcept) child);
        } else {
            return child;
        }
    }

    private static class ExtractedConceptAndChildren {
        private final ExtractedConcept extractedConcept;
        private final List<AbstractExtractClassCandidateRefactoring> children;

        private ExtractedConceptAndChildren(ExtractedConcept extractedConcept) {
            this.extractedConcept = extractedConcept;
            ExtractClassCandidateRefactoring[] refactorings = extractedConcept.getConceptClusters().toArray(new ExtractClassCandidateRefactoring[0]);
            children = new ArrayList<>();
            for (ExtractClassCandidateRefactoring refactoring : refactorings) {
                children.add(new AbstractExtractClassCandidateRefactoring(refactoring));
            }
        }

        @Override
        public String toString() {
            return extractedConcept.toString();
        }

        @Override
        public int hashCode() {
            return extractedConcept.hashCode();
        }
    }
}
