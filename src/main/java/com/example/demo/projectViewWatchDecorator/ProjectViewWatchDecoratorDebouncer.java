package com.example.demo.projectViewWatchDecorator;

import com.cdd.service.AnalyzerService;
import com.cdd.service.RegisterQualifierService;
import com.cdd.utils.Debouncer;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import groovy.lang.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.awt.*;
import java.util.Objects;

@Slf4j
@Singleton
public class ProjectViewWatchDecoratorDebouncer  {

    private static final int DEBOUNCE_INTERVAL = 3000;

    private Debouncer<String> getBackgroundableDebouncer(Project project, VirtualFile file, PresentationData data) {
        return new Debouncer<>(d -> {

            ApplicationManager.getApplication().invokeLater(() -> {
                try {
                    var complexityCounter = new AnalyzerService().readPsiFile(PsiManager.getInstance(project).findFile(file));
                    var currentComplexity = complexityCounter.compute();
                    var limitOfComplexity = complexityCounter.getMetrics().getLimitOfComplexity();

                    if (currentComplexity * 1.3 >= limitOfComplexity)
                        data.setForcedTextForeground(new Color(173, 178, 42));

                    if (currentComplexity >= limitOfComplexity)
                        data.setForcedTextForeground(new Color(159, 106, 49));

                    data.setTooltip("Points of difficulty of understanding");
                    data.setLocationString(currentComplexity + " : cognitive load");


                    RegisterQualifierService.register(((PsiJavaFileImpl) Objects.requireNonNull(PsiManager.getInstance(project).findFile(file))).getClasses());
                } catch (ResourceNotFoundException ignored) {
                    log.info("resource not found");
                } catch (NullPointerException e) {
                    log.info("null pointer ", e);
                }
            });
        }, DEBOUNCE_INTERVAL);
    }

}
