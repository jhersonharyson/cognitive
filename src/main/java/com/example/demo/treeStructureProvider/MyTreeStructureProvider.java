package com.example.demo.treeStructureProvider;

import com.cdd.service.AnalyzerService;
import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
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
                        var complexityCounter = new AnalyzerService().readPsiFile(PsiManager.getInstance(ProjectManager.getInstance().getDefaultProject()).findFile(file));
                        child.getPresentation().setTooltip("Points of difficulty of understanding");
                        child.getPresentation().setLocationString("55 : cognitive load");
                        child.getPresentation().setSeparatorAbove(true);
                        child.getPresentation().setPresentableText("asdasdasdasdasd");
                        child.getPresentation().setIcon(AllIcons.Idea_logo_welcome);
                        Objects.requireNonNull(child.getChildren().stream().findFirst().orElse(null)).getPresentation().setTooltip("asdasda");
                        Objects.requireNonNull(child.getElement()).getPresentation().setLocationString("54asd5as6d5");
                        child.getElement().getPresentation().setTooltip("54654654654654654");
                        added = true;
                        nodes.add(child);
                    } catch (Exception ignored) {
                        System.out.println(ignored);
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
