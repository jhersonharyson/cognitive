package com.example.demo.hints2;


import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.codeInsight.hints.*;
import com.intellij.codeInsight.hints.presentation.AttributesTransformerPresentation;
import com.intellij.codeInsight.hints.presentation.InlayPresentation;
import com.intellij.codeInsight.hints.presentation.MouseButton;
import com.intellij.codeInsight.hints.presentation.PresentationFactory;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.util.ArrayUtil;
import com.intellij.util.SmartList;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class HintProvider implements InlayHintsProvider<HintSettings> {
    private static final SettingsKey<HintSettings> KEY = new SettingsKey<>("JavaLens");
    private HintSettings hintSettings = new HintSettings();

    @Override
    public boolean isVisibleInSettings() {
        return true;
    }

    @NotNull
    @Override
    public SettingsKey<HintSettings> getKey() {
        return KEY;
    }

    private static SettingsKey<String> key = new SettingsKey("SimpleInlayProvider");


    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getName() {
        return "Simple inlay provider";
    }

    @Nullable
    @Override
    public String getPreviewText() {
        return "preview text";
    }

    @NotNull
    @Override
    public ImmediateConfigurable createConfigurable(@NotNull HintSettings settings) {
        return new ImmediateConfigurable() {
            @NotNull
            @Override
            public JComponent createComponent(@NotNull ChangeListener listener) {
                JPanel panel = UI.PanelFactory.panel(new JLabel()).
                        withComment("This will display JSX class and method usages in editor, " +
                                "provided by the React Native Console Free plugin.").createPanel();
                return panel;
            }
        };
    }

    @NotNull
    @Override
    public HintSettings createSettings() {
        this.hintSettings.setShowUsages(true);
        return this.hintSettings;
    }

    public interface InlResult {
        void onClick(@NotNull Editor editor, @NotNull PsiElement element);

        @NotNull
        String getRegularText();

        @NotNull
        default String getHoverText() {
            return getRegularText();
        }
    }

    @NotNull
    private static InlayPresentation createPresentation(@NotNull PresentationFactory factory,
                                                        @NotNull PsiElement element,
                                                        @NotNull Editor editor,
                                                        @NotNull InlResult result) {
        //Icon icon = AllIcons.Toolwindows.ToolWindowFind;
        //Icon icon = IconLoader.getIcon("/toolwindows/toolWindowFind_dark.svg", AllIcons.class);

        InlayPresentation text = factory.smallText(result.getRegularText());

        return factory.changeOnHover(text, () -> {
            InlayPresentation onClick = factory.onClick(text, MouseButton.Left, (___, __) -> {
                result.onClick(editor, element);
                return null;
            });
            return referenceColor(onClick);
        }, __ -> true);
    }

    @NotNull
    private static InlayPresentation referenceColor(@NotNull InlayPresentation presentation) {
        return new AttributesTransformerPresentation(presentation,
                __ -> {
                    TextAttributes attributes =
                            EditorColorsManager.getInstance().getGlobalScheme().getAttributes(EditorColors.REFERENCE_HYPERLINK_COLOR).clone();
                    attributes.setEffectType(EffectType.LINE_UNDERSCORE);
                    return attributes;
                });
    }


    @Nullable
    @Override
    public InlayHintsCollector getCollectorFor(@NotNull PsiFile file,
                                               @NotNull Editor editor,
                                               @NotNull HintSettings settings,
                                               @NotNull InlayHintsSink inlayHintsSink) {
//        return new HintCollector();

        PresentationFactory factory = new PresentationFactory((EditorImpl) editor);
        return (element, editor1, sink) -> {
            if (!(element instanceof PsiMember) || element instanceof PsiTypeParameter) return true;
            PsiMember member = (PsiMember) element;
            if (member.getName() == null) return true;

            List<InlResult> hints = new SmartList<>();
            if (true/*settings.isShowUsages()*/) {
                String usagesHint = JavaTelescope.usagesHint(member, file);
                if (usagesHint != null) {
                    hints.add(new InlResult() {
                        @Override
                        public void onClick(@NotNull Editor editor, @NotNull PsiElement element) {
                            GotoDeclarationAction.startFindUsages(editor, file.getProject(), element);
                        }

                        @NotNull
                        @Override
                        public String getRegularText() {
                            return usagesHint;
                        }
                    });
                }
            }
            if (true/*settings.isShowImplementations()*/) {
                if (element instanceof PsiClass) {
                    int inheritors = JavaTelescope.collectInheritingClasses((PsiClass) element);
                    if (inheritors != 0) {
                        hints.add(new InlResult() {
                            @Override
                            public void onClick(@NotNull Editor editor, @NotNull PsiElement element) {
                                Point point = JBPopupFactory.getInstance().guessBestPopupLocation(editor).getScreenPoint();
                                MouseEvent event = new MouseEvent(new JLabel(), 0, 0, 0, point.x, point.y, 0, false);
                                GutterIconNavigationHandler<PsiElement> navigationHandler = MarkerType.SUBCLASSED_CLASS.getNavigationHandler();
                                navigationHandler.navigate(event, ((PsiClass) element).getNameIdentifier());
                            }

                            @NotNull
                            @Override
                            public String getRegularText() {
                                String prop = "{0, choice, 1#1 Implementation|2#{0,number} Implementations}";
                                return MessageFormat.format(prop, inheritors);
                            }
                        });
                    }
                }
                if (element instanceof PsiMethod) {
                    int overridings = JavaTelescope.collectOverridingMethods((PsiMethod) element);
                    if (overridings != 0) {
                        hints.add(new InlResult() {
                            @Override
                            public void onClick(@NotNull Editor editor, @NotNull PsiElement element) {
                                Point point = JBPopupFactory.getInstance().guessBestPopupLocation(editor).getScreenPoint();
                                MouseEvent event = new MouseEvent(new JLabel(), 0, 0, 0, point.x, point.y, 0, false);
                                GutterIconNavigationHandler<PsiElement> navigationHandler = MarkerType.OVERRIDDEN_METHOD.getNavigationHandler();
                                navigationHandler.navigate(event, ((PsiMethod) element).getNameIdentifier());
                            }

                            @NotNull
                            @Override
                            public String getRegularText() {
                                String prop = "{0, choice, 1#1 Implementation|2#{0,number} Implementations}";
                                return MessageFormat.format(prop, overridings);
                            }
                        });
                    }
                }
            }

            if (!hints.isEmpty()) {
                int offset = element.getTextRange().getStartOffset();
                int line = editor1.getDocument().getLineNumber(offset);
                int lineStart = editor1.getDocument().getLineStartOffset(line);
                int indent = offset - lineStart;

                InlayPresentation[] presentations = new InlayPresentation[hints.size() * 2 + 1];
                presentations[0] = factory.text(StringUtil.repeat(" ", indent));
                int o = 1;
                for (int i = 0; i < hints.size(); i++) {
                    InlResult hint = hints.get(i);
                    if (i != 0) {
                        presentations[o++] = factory.text(" ");
                    }
                    presentations[o++] = createPresentation(factory, element, editor1, hint);
                }
                presentations[o] = factory.text("          "); // placeholder for "Settings..."

                InlayPresentation seq = factory.seq(presentations);
                InlayPresentation withAppearingSettings = factory.changeOnHover(seq, () -> {
                    InlayPresentation[] trimmedSpace = Arrays.copyOf(presentations, presentations.length - 1);
                    InlayPresentation[] spaceAndSettings = {factory.text("  "), factory.text("Olá")};
                    InlayPresentation[] withSettings = ArrayUtil.mergeArrays(trimmedSpace, spaceAndSettings);
                    return factory.seq(withSettings);
                }, e -> true);
                sink.addBlockElement(lineStart, true, true, 0, withAppearingSettings);
            }
            return true;
        };
    }

    @Override
    public boolean isLanguageSupported(@NotNull Language language) {
        return language.getID().equals("JAVA");
    }
}
