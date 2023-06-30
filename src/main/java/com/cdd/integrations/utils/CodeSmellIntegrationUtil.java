package com.cdd.integrations.utils;

import com.cdd.integrations.code.smell.jdeodorant.IntelliJDeodorantBundle;
import com.cdd.integrations.code.smell.jdeodorant.JDeodorantFacade;
import com.cdd.integrations.code.smell.jdeodorant.core.ast.decomposition.cfg.ASTSlice;
import com.cdd.integrations.code.smell.jdeodorant.core.ast.decomposition.cfg.ASTSliceGroup;
import com.cdd.integrations.code.smell.jdeodorant.core.distance.MoveMethodCandidateRefactoring;
import com.cdd.integrations.code.smell.jdeodorant.core.distance.ProjectInfo;
import com.cdd.integrations.code.smell.jdeodorant.ide.fus.collectors.IntelliJDeodorantCounterCollector;
import com.cdd.integrations.code.smell.jdeodorant.ide.refactoring.moveMethod.MoveMethodRefactoring;
import com.cdd.integrations.code.smell.jdeodorant.ide.ui.AbstractRefactoringPanel;
import com.cdd.integrations.code.smell.jdeodorant.ide.ui.ExtractMethodPanel;
import com.cdd.integrations.code.smell.jdeodorant.utils.PsiUtils;
import com.cdd.service.EditorService;
import com.cdd.utils.Debouncer;
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
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cdd.integrations.code.smell.jdeodorant.JDeodorantFacade.*;
import static java.util.stream.Collectors.*;


public class CodeSmellIntegrationUtil {

	private static final int DEBOUNCE_INTERVAL = 10_000;
	private static CodeSmellIntegrationUtil INSTANCE;

	public static boolean taskRunning = false;
	private static final String FEATURE_ENVY = "feature.evy";

	private static final String LONG_METHOD = "long.method";

	public static CodeSmellIntegrationUtil getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CodeSmellIntegrationUtil();
		}
		return INSTANCE;
	}

	private final Project project;
//	private final Task.Backgroundable backgroundable;

	private final Task.Backgroundable asyncFeatureEnvyBackgroundable;
	private final Task.Backgroundable asyncLongMethodBackgroundable;

	public final List<MoveMethodRefactoring> featureEnvyRefactorings = new ArrayList<>();
	public final List<Set<ASTSlice>> longMethodRefactorings = new ArrayList<>();

	public int numberOfFeatureEnvy = 0;
	public int numberOfLongMethod = 0;
	private AnalysisScope currentFileScope;
	private ProjectInfo projectInfo;
	private final Set<String> classNamesToBeExamined = new HashSet<>();

	private final Debouncer<String> featureEnvyDebouncer;
	private final Debouncer<String> longMethodDebouncer;

	public CodeSmellIntegrationUtil() {
		project = ProjectManager.getInstance().getOpenProjects()[0];
		//currentFileScope = getCurrentFileScope();

//		backgroundable = null;//taskDetectFeatureEnvy();
		asyncFeatureEnvyBackgroundable = asyncTaskDetectFeatureEnvy();
		asyncLongMethodBackgroundable = asyncTaskDetectLongMethod();

		featureEnvyDebouncer = featureEnvyDebouncer();
		longMethodDebouncer = LongMethodDebouncer();

		projectInfo = Objects.isNull(currentFileScope) ? null : new ProjectInfo(currentFileScope, true);
	}

	private Debouncer<String> featureEnvyDebouncer() {
		return getBackgroundableDebouncer(asyncFeatureEnvyBackgroundable);
	}

	private Debouncer<String> LongMethodDebouncer() {
		return getBackgroundableDebouncer(asyncLongMethodBackgroundable);
	}

	@NotNull
	private Debouncer<String> getBackgroundableDebouncer(Task.Backgroundable task) {
		return new Debouncer<>(d -> {

			ApplicationManager.getApplication().runReadAction(() -> {
				currentFileScope = getCurrentFileScope();
				if (currentFileScope == null) return;
				ProjectInfo projectInfo = new ProjectInfo(currentFileScope, true);

				PsiUtils.extractFiles(project).stream()
								.filter(currentFileScope::contains)
								.forEach(list ->
												Arrays.stream(list.getClasses()).map(PsiClass::getQualifiedName).forEach(classNamesToBeExamined::add));
				taskRunning = true;
				AbstractRefactoringPanel.asyncRunAfterCompilationCheck(task, project, projectInfo);
			});
		}, DEBOUNCE_INTERVAL);
	}

//	public void detectFeatureEnvy() {
//		currentFileScope = getCurrentFileScope();
//		if (currentFileScope == null) return;
//		ProjectInfo projectInfo = new ProjectInfo(currentFileScope, true);
//
//		PsiUtils.extractFiles(project).stream()
//						.filter(currentFileScope::contains)
//						.forEach(list ->
//										Arrays.stream(list.getClasses()).map(PsiClass::getQualifiedName).forEach(classNamesToBeExamined::add));
//
////		backgroundable.onCancel();
//		taskRunning = true;
//		AbstractRefactoringPanel.runAfterCompilationCheck(backgroundable, project, projectInfo);
//	}

	public void asyncDetectFeatureEnvy() {
		ApplicationManager.getApplication().runReadAction(() -> {
			try {
				featureEnvyDebouncer.call(FEATURE_ENVY);
			} catch (Throwable ignored) {
				System.out.println("ignored 2323");
			}
		});
	}

	public void asyncDetectLongMethod() {
		ApplicationManager.getApplication().runReadAction(() -> {
			try {
				longMethodDebouncer.call(LONG_METHOD);
			} catch (Throwable ignored) {
				System.out.println("ignored 2323132342");
			}
		});
	}

	public void asyncDetect() {
		asyncDetectFeatureEnvy();
		asyncDetectLongMethod();
	}

//	Task.Backgroundable taskDetectFeatureEnvy() {
//		return new Task.Backgroundable(project, IntelliJDeodorantBundle.message("feature.envy.detect.indicator.status"), true) {
//			@Override
//			public void run(@NotNull ProgressIndicator indicator) {
//
//				ApplicationManager.getApplication().runReadAction(() -> {
//					try {
//
//						if (ObjectUtils.isEmpty(projectInfo)) {
//							projectInfo = new ProjectInfo(Objects.requireNonNull(currentFileScope), true);
//						}
//
//						List<MoveMethodCandidateRefactoring> candidates = JDeodorantFacade.getMoveMethodRefactoringOpportunities(projectInfo, indicator, classNamesToBeExamined);
//
////                    ((SmartPsiElementPointerImpl) candidates.get(0).visualizationData.getMethodToBeMoved().psiMethod).getElement()
//
//						final List<MoveMethodRefactoring> references = candidates.stream().filter(Objects::nonNull)
//										.map(x ->
//														new MoveMethodRefactoring(x.getSourceMethodDeclaration(),
//																		x.getTargetClass().getClassObject().getPsiClass(),
//																		x.getDistinctSourceDependencies(),
//																		x.getDistinctTargetDependencies()))
//										.collect(Collectors.toList());
//
//						refactorings.clear();
//						refactorings.addAll(new ArrayList<>(references));
//						numberOfFeatureEnvy = refactorings.size();
//						IntelliJDeodorantCounterCollector.getInstance().refactoringFound(project, "move.method", references.size());
//
//					} finally {
//						taskRunning = false;
//					}
//				});
//			}
//
//			@Override
//			public void onCancel() {
//				super.onCancel();
//				taskRunning = false;
//			}
//		};
//	}


	Task.Backgroundable asyncTaskDetectFeatureEnvy() {
		return new Task.Backgroundable(project, IntelliJDeodorantBundle.message("feature.envy.detect.indicator.status"), true) {
			@Override
			public void run(@NotNull ProgressIndicator indicator) {

				ApplicationManager.getApplication().runReadAction(() -> {
						try {

							if(ObjectUtils.isEmpty(projectInfo)){
								projectInfo = new ProjectInfo(Objects.requireNonNull(currentFileScope), true);
							}

//							PsiUtils.extractFiles(project).stream()
//									.filter(file -> currentFileScope.contains(file))
//									.forEach(list ->
//											Arrays.stream(list.getClasses()).map(PsiClass::getQualifiedName).forEach(classNamesToBeExamined::add));

							List<MoveMethodCandidateRefactoring> candidates = JDeodorantFacade.getMoveMethodRefactoringOpportunities(projectInfo, indicator, classNamesToBeExamined);


//                    ((SmartPsiElementPointerImpl) candidates.get(0).visualizationData.getMethodToBeMoved().psiMethod).getElement()
							final List<MoveMethodRefactoring> references = candidates.stream().filter(Objects::nonNull)
											.map(x ->
															new MoveMethodRefactoring(x.getSourceMethodDeclaration(),
																			x.getTargetClass().getClassObject().getPsiClass(),
																			x.getDistinctSourceDependencies(),
																			x.getDistinctTargetDependencies()))
											.collect(Collectors.toList());

							featureEnvyRefactorings.clear();
							featureEnvyRefactorings.addAll(new ArrayList<>(references));
							numberOfFeatureEnvy = featureEnvyRefactorings.size();
							IntelliJDeodorantCounterCollector.getInstance().refactoringFound(project, "move.method", references.size());
							EditorService.syncFiles();
						} catch (Throwable ignored){
							System.out.println("ignored 2234234"+ ignored);
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

	Task.Backgroundable asyncTaskDetectLongMethod() {
		return new Task.Backgroundable(project, IntelliJDeodorantBundle.message("long.method.detect.indicator.status"), true) {
			@Override
			public void run(@NotNull ProgressIndicator indicator) {

				ApplicationManager.getApplication().runReadAction(() -> {
					try{

						if(ObjectUtils.isEmpty(projectInfo)){
							projectInfo = new ProjectInfo(Objects.requireNonNull(currentFileScope), true);
						}

						PsiUtils.extractFiles(project).stream()
								.filter(file -> currentFileScope.contains(file))
								.forEach(list ->
										Arrays.stream(list.getClasses()).map(PsiClass::getQualifiedName).forEach(classNamesToBeExamined::add));

					Set<ASTSliceGroup> candidates = getExtractMethodRefactoringOpportunities(projectInfo, indicator);
//					final List<ExtractMethodCandidateGroup> extractMethodCandidateGroups = candidates.stream()
//							.map(sliceGroup ->
//									sliceGroup.getCandidates().stream()
//											.filter(c -> new ExtractMethodPanel(currentFileScope).canBeExtracted(c))
//											.collect(toSet()))
//							.filter(set -> !set.isEmpty())
//							.map(ExtractMethodCandidateGroup::new)
//							.sorted(Comparator.comparing(ExtractMethodCandidateGroup::getDescription))
//							.collect(toList());

					var nonNullCandidates = candidates.stream().filter(Objects::nonNull);
					var canBeExtracted = nonNullCandidates.map(sliceGroup ->
							sliceGroup.getCandidates().stream()
									.filter(c -> new ExtractMethodPanel(currentFileScope).canBeExtracted(c))
									.collect(toSet()));

					final List<Set<ASTSlice>> references = canBeExtracted.filter(set -> !set.isEmpty()).collect(toList());
//					var b = a.map(ExtractMethodCandidateGroup::new)
//							.sorted(Comparator.comparing(ExtractMethodCandidateGroup::getDescription))
//							.collect(toList());

//
//					treeTableModel.setCandidateRefactoringGroups(extractMethodCandidateGroups);
//					ApplicationManager.getApplication().invokeLater(() -> showRefactoringsTable());
//					IntelliJDeodorantCounterCollector.getInstance().refactoringFound(project, "extract.method", extractMethodCandidateGroups.size());
//
//
						longMethodRefactorings.clear();
						longMethodRefactorings.addAll(references);
						numberOfLongMethod = longMethodRefactorings.size();
						IntelliJDeodorantCounterCollector.getInstance().refactoringFound(project, "move.method", references.size());
						EditorService.syncFiles();
					} catch (Throwable ignored){
						System.out.println("ignored 2234234"+ ignored);
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


	Task.Backgroundable asyncTaskDetectTypeStateChecking() {
		return new Task.Backgroundable(project, IntelliJDeodorantBundle.message("type.state.checking.identification.indicator.status"), true) {
			@Override
			public void run(@NotNull ProgressIndicator indicator) {

				ApplicationManager.getApplication().runReadAction(() -> {
					try{

						if(ObjectUtils.isEmpty(projectInfo)){
							projectInfo = new ProjectInfo(Objects.requireNonNull(currentFileScope), true);
						}

						PsiUtils.extractFiles(project).stream()
								.filter(file -> currentFileScope.contains(file))
								.forEach(list ->
										Arrays.stream(list.getClasses()).map(PsiClass::getQualifiedName).forEach(classNamesToBeExamined::add));

						Set<ASTSliceGroup> candidates = getExtractMethodRefactoringOpportunities(projectInfo, indicator);


						var nonNullCandidates = candidates.stream().filter(Objects::nonNull);
						var canBeExtracted = nonNullCandidates.map(sliceGroup ->
								sliceGroup.getCandidates().stream()
										.filter(c -> new ExtractMethodPanel(currentFileScope).canBeExtracted(c))
										.collect(toSet()));

						final List<Set<ASTSlice>> references = canBeExtracted.filter(set -> !set.isEmpty()).collect(toList());

						longMethodRefactorings.clear();
						longMethodRefactorings.addAll(references);
						numberOfLongMethod = longMethodRefactorings.size();
						IntelliJDeodorantCounterCollector.getInstance().refactoringFound(project, "move.method", references.size());
						EditorService.syncFiles();
					} catch (Throwable ignored){
						System.out.println("ignored 2234234"+ ignored);
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
