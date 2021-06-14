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


import com.cdd.service.Analyzer;
import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.ide.projectView.impl.ProjectRootsUtil;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTask;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskState;
import com.intellij.openapi.externalSystem.service.internal.ExternalSystemExecuteTaskTask;
import com.intellij.openapi.externalSystem.service.internal.ExternalSystemProcessingManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packageDependencies.ui.PackageDependenciesNode;
import com.intellij.psi.PsiManager;
import com.intellij.ui.ColoredTreeCellRenderer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * @author Simon Jiang
 */
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
                var complexityCounter = new Analyzer().readPsiFile(PsiManager.getInstance(ProjectManager.getInstance().getDefaultProject()).findFile(file));
                data.setTooltip("Points of difficulty of understanding");
                data.setLocationString(complexityCounter.compute() + " : cognitive load");
            } catch (Exception ignored) {

            }
        }
    }

}
