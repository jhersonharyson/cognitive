package com.example.demo.hints2;

import com.intellij.codeInsight.hints.InlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.codeInsight.hints.presentation.InlayPresentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("UnstableApiUsage")
public class HintCollector implements InlayHintsCollector {
    static AtomicLong counter = new AtomicLong();
    static long id = counter.getAndIncrement();

    @Override
    public boolean collect(@NotNull PsiElement element, @NotNull Editor editor, @NotNull InlayHintsSink inlayHintsSink) {
        if (false) return true;

        var inlays = editor.getInlayModel().getInlineElementAt(new VisualPosition(5, 4));
        if(inlays != null){
            InlayPresentation inlayPresentation = new Presentation(editor, (Inlay) editor.getInlayModel().getInlineElementsInRange(element.getTextRange().getStartOffset() +1, element.getTextRange().getEndOffset() -1).get(0));
            inlayHintsSink.addBlockElement(element.getTextRange().getEndOffset(), true, false, 0, inlayPresentation);
        }
        return true;
    }

}
