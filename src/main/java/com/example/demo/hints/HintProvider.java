package com.example.demo.hints;


import com.cdd.service.Analyzer;
import com.example.demo.utils.RealtimeState;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.codeInsight.hints.*;
import com.intellij.codeInsight.hints.presentation.AttributesTransformerPresentation;
import com.intellij.codeInsight.hints.presentation.InlayPresentation;
import com.intellij.codeInsight.hints.presentation.MouseButton;
import com.intellij.codeInsight.hints.presentation.PresentationFactory;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction;
import com.intellij.icons.AllIcons;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.ui.popup.JBPopupFactory;
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
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class HintProvider implements InlayHintsProvider<HintSettings> {
    private static final SettingsKey<HintSettings> KEY = new SettingsKey<>("JavaLens");

    private int numberOfComplexity = 0;

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
        return new HintSettings();
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
                    TextAttributes attributes = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(EditorColors.BREADCRUMBS_HOVERED).clone();
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

        RealtimeState.getInstance().setFile(file.getVirtualFile());

        RealtimeState.getInstance().setCurrentComplexity(new Analyzer().readPsiFile(file).compute());

        PresentationFactory factory = new PresentationFactory((EditorImpl) editor);

        return (element, editor1, sink) -> {

            if (!(element instanceof PsiMember || element instanceof PsiIfStatement) || element instanceof PsiTypeParameter)
                return true;

            PsiMember member = null;
            PsiIfStatement statement = null;

            if (element instanceof PsiIfStatement) {
                statement = (PsiIfStatement) element;
            }

            if (element instanceof PsiMember) {
                member = (PsiMember) element;
            }


            if (member != null && Objects.requireNonNull(member).getName() == null) return true;
            if (statement != null && Objects.requireNonNull(statement).getText() == null) return true;


            List<InlResult> hints = new SmartList<>();

            if (element instanceof PsiMember/*settings.isShowUsages()*/) {
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
            if (element instanceof PsiMember/*settings.isShowImplementations()*/) {
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
            if (element instanceof PsiIfStatement) {
                hints.add(new InlResult() {
                    @Override
                    public void onClick(@NotNull Editor editor, @NotNull PsiElement element) {
                        Point point = JBPopupFactory.getInstance().guessBestPopupLocation(editor).getScreenPoint();
                        MouseEvent event = new MouseEvent(new JLabel(), 0, 0, 0, point.x, point.y, 0, false);
//                            GutterIconNavigationHandler<PsiElement> navigationHandler = MarkerType..getNavigationHandler();
//                            navigationHandler.navigate(event, ((PsiMethod) element).getNameIdentifier());
                    }

                    @NotNull
                    @Override
                    public String getRegularText() {
//                        RealtimeState.getInstance().setCurrentComplexity(numberOfComplexity);
                        String prop = "{0, choice, 1#// 1 Complexity|2#// {0,number} Complexity}";
                        return MessageFormat.format(prop, ++numberOfComplexity);
                    }
                });

            }

            if (!hints.isEmpty()) {
                int offset = element.getTextRange().getStartOffset();
                int line = editor1.getDocument().getLineNumber(offset);
                int lineStart = editor1.getDocument().getLineStartOffset(line);
                int indent = offset - lineStart;

                InlayPresentation[] presentations = new InlayPresentation[hints.size() * 2 + 1];
                presentations[0] = factory.textSpacePlaceholder(indent, false);
                int o = 1;
                for (int i = 0; i < hints.size(); i++) {
                    InlResult hint = hints.get(i);
                    if (i != 0) {
                        presentations[o++] = factory.text(" ");
                    }
                    presentations[o++] = createPresentation(factory, element, editor1, hint);
                }
                presentations[o] = factory.textSpacePlaceholder(10, false); // placeholder for "Settings..."

                Icon icon = AllIcons.Actions.MoveDown;

                InlayPresentation seq = factory.seq(presentations);
                InlayPresentation withAppearingSettings = factory.changeOnHover(seq, () -> {
                    InlayPresentation[] trimmedSpace = Arrays.copyOf(presentations, presentations.length - 1);
                    InlayPresentation[] spaceAndSettings = {factory.textSpacePlaceholder(1, false), factory.icon(icon)};
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
        this.numberOfComplexity = 0;
        return language.getID().equals("JAVA");
    }
}

