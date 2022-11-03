package com.example.demo.projectViewWatchDecorator;

/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */


import com.cdd.integrations.utils.CodeSmellIntegrationUtil;
import com.cdd.service.AnalyzerService;
import com.cdd.service.RegisterQualifierService;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packageDependencies.ui.PackageDependenciesNode;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.ui.ColoredTreeCellRenderer;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.awt.*;
import java.util.Objects;


/**
 * @author Simon Jiang
 */
@Slf4j
public class ProjectViewWatchDecorator implements ProjectViewNodeDecorator {

    @Override
    public void decorate(PackageDependenciesNode node, ColoredTreeCellRenderer cellRenderer) {
    }

//    @Override
//    @SuppressWarnings("rawtypes")
//    public void decorate(ProjectViewNode node, PresentationData data) {
//        Project project = node.getProject();
//
//        if (project == null) {
//            return;
//        }
//
//        VirtualFile virtualFile = node.getVirtualFile();
//
//        if (virtualFile == null) {
//            return;
//        }
//
//        String canonicalPath = virtualFile.getCanonicalPath();
//
//        if (canonicalPath == null) {
//            return;
//        }
//
//        if (ProjectRootsUtil.isModuleContentRoot(virtualFile, project)) {
//
//
//                            data.setLocationString("[watching]");
//
//
//
//        }
//    }

    public void decorate(ProjectViewNode node, PresentationData data) {
        Project project = node.getProject();

        if (project == null) {
            return;
        }

        VirtualFile file = node.getVirtualFile();

        if (file == null) {
            return;
        }

        String canonicalPath = file.getCanonicalPath();

        if (canonicalPath == null) {
            return;
        }


        if (!file.isDirectory() && file.getFileType() instanceof JavaFileType) {
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


            } catch (ResourceNotFoundException ignored)  {
               log.info("resource not found");
            } catch (NullPointerException e){
                log.info("null pointer ", e);
            }
        }
    }

}
