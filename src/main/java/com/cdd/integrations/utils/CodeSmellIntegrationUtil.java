package com.cdd.integrations.utils;

import com.cdd.integrations.code.smell.jdeodorant.IntelliJDeodorantBundle;
import com.cdd.integrations.code.smell.jdeodorant.JDeodorantFacade;
import com.cdd.integrations.code.smell.jdeodorant.core.distance.MoveMethodCandidateRefactoring;
import com.cdd.integrations.code.smell.jdeodorant.core.distance.ProjectInfo;
import com.cdd.integrations.code.smell.jdeodorant.ide.fus.collectors.IntelliJDeodorantCounterCollector;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.moveMethod.MoveMethodRefactoring;
import com.cdd.integrations.code.smell.jdeodorant.ide.ui.AbstractRefactoringPanel;
import com.cdd.integrations.code.smell.jdeodorant.utils.PsiUtils;
import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class CodeSmellIntegrationUtil {

	public static boolean taskRunning = false;

	private static class CodeSmellIntegrationUtilHolder {
		private static final CodeSmellIntegrationUtil INSTANCE = new CodeSmellIntegrationUtil();
	}

	public static CodeSmellIntegrationUtil getInstance() {
		return CodeSmellIntegrationUtilHolder.INSTANCE;
	}

	private Project project;
	private Task.Backgroundable backgroundable;

	public final List<MoveMethodRefactoring> refactorings = new ArrayList<>();

	public int numberOfFeatureEnvy = 0;
	private AnalysisScope currentFileScope;
	private ProjectInfo projectInfo;
	private final Set<String> classNamesToBeExamined = new HashSet<>();


	public CodeSmellIntegrationUtil() {
		project = ProjectManager.getInstance().getOpenProjects()[0];
		currentFileScope = getCurrentFileScope();
		backgroundable = taskDetectFeatureEnvy();
		projectInfo = new ProjectInfo(Objects.requireNonNull(currentFileScope), true);
	}

	public void detectFeatureEnvy() {

		ProjectInfo projectInfo = new ProjectInfo(Objects.requireNonNull(currentFileScope), true);

		PsiUtils.extractFiles(project).stream()
						.filter(currentFileScope::contains)
						.forEach(list ->
										Arrays.stream(list.getClasses()).map(PsiClass::getQualifiedName).forEach(classNamesToBeExamined::add));

//		backgroundable.onCancel();
		taskRunning = true;
		AbstractRefactoringPanel.runAfterCompilationCheck(backgroundable, project, projectInfo);
	}

	Task.Backgroundable taskDetectFeatureEnvy() {
		return new Task.Backgroundable(project, IntelliJDeodorantBundle.message("feature.envy.detect.indicator.status"), true) {
			@Override
			public void run(@NotNull ProgressIndicator indicator) {

				ApplicationManager.getApplication().runReadAction(() -> {
					try {

					List<MoveMethodCandidateRefactoring> candidates = JDeodorantFacade.getMoveMethodRefactoringOpportunities(projectInfo, indicator, classNamesToBeExamined);

//                    ((SmartPsiElementPointerImpl) candidates.get(0).visualizationData.getMethodToBeMoved().psiMethod).getElement()

						final List<MoveMethodRefactoring> references = candidates.stream().filter(Objects::nonNull)
										.map(x ->
														new MoveMethodRefactoring(x.getSourceMethodDeclaration(),
																		x.getTargetClass().getClassObject().getPsiClass(),
																		x.getDistinctSourceDependencies(),
																		x.getDistinctTargetDependencies()))
										.collect(Collectors.toList());

						refactorings.clear();
						refactorings.addAll(new ArrayList<>(references));
						numberOfFeatureEnvy = refactorings.size();
						IntelliJDeodorantCounterCollector.getInstance().refactoringFound(project, "move.method", references.size());

					} finally {
						taskRunning = false;
					}
				});
			}

			@Override
			public void onCancel() {
				super.onCancel();
				taskRunning = false;
			}
		};
	}


	private AnalysisScope getCurrentFileScope() {

		FileEditor currentEditor = FileEditorManager.getInstance(project).getSelectedEditor();
		if (currentEditor != null) {
			VirtualFile currentFile = currentEditor.getFile();
			PsiFile file = PsiManager.getInstance(project).findFile(currentFile);
			if (file instanceof PsiJavaFile)
				return new AnalysisScope(project, Collections.singletonList(currentFile));
		}
		return null;
	}

}
