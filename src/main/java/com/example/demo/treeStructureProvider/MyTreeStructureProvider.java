package com.example.demo.treeStructureProvider;

import com.cdd.service.AnalyzerService;
import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MyTreeStructureProvider implements TreeStructureProvider {

    @NotNull
    @Override
    public Collection<AbstractTreeNode<?>> modify(@NotNull AbstractTreeNode<?> parent,
                                                  @NotNull Collection<AbstractTreeNode<?>> children,
                                                  ViewSettings settings) {
        ArrayList<AbstractTreeNode<?>> nodes = new ArrayList<>();

        for (AbstractTreeNode<?> child : children) {
            var added = false;
            if (child instanceof PsiFileNode) {
                VirtualFile file = ((PsiFileNode) child).getVirtualFile();
                if (file != null && !file.isDirectory() && (file.getFileType() instanceof JavaFileType)) {
                    try {

//                        var complexityCounter = new AnalyzerService().readPsiFile(PsiManager.getInstance(parent.getProject()).findFile(file));
                        child.getPresentation().setTooltip("Points of difficulty of understanding");
                        child.getPresentation().setLocationString("XX : cognitive load");
                        child.getPresentation().setSeparatorAbove(true);
                        child.getPresentation().setPresentableText("test1");
                        child.getPresentation().setIcon(AllIcons.Idea_logo_welcome);
                        Objects.requireNonNull(child.getChildren().stream().findFirst().orElse(null)).getPresentation().setTooltip("asdasda");
                        Objects.requireNonNull(child.getElement()).getPresentation().setLocationString("test2");
                        child.getElement().getPresentation().setTooltip("test3");
                        added = true;
                        nodes.add(child);
                    } catch (Exception ignored) {
                        System.out.println(ignored+"123 "+file);
                    }
                }
            }
            if(!added)
                nodes.add(child);
        }
        return nodes;
    }

    @Nullable
    @Override
    public Object getData(@NotNull Collection<AbstractTreeNode<?>> selected, @NotNull String dataId) {
        return null;
    }

}
