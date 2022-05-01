package com.example.demo.hints;


import com.cdd.integrations.utils.CodeSmellIntegrationUtil;
import com.cdd.model.Rule;
import com.cdd.model.Statement;
import com.cdd.service.CddJsonResourceService;
import com.cdd.service.ComplexityMetricsService;
import com.cdd.utils.ContextualCouplingUtils;
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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.ui.JBColor;
import com.intellij.util.ArrayUtil;
import com.intellij.util.SmartList;
import com.intellij.util.ui.UI;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

import static io.grpc.internal.ConscryptLoader.isPresent;


public class HintProvider implements InlayHintsProvider<HintSettings> {
    private static final SettingsKey<HintSettings> KEY = new SettingsKey<>("JavaLens");
    private static final JBColor errorColor = new JBColor(new Color(159, 106, 49), new Color(159, 106, 49));
    private static final JBColor highlightColor = new JBColor(new Color(203, 192, 169), new Color(203, 192, 169));
    private static final JBColor defaultColor = new JBColor(new Color(141, 141, 138), new Color(141, 141, 138));

    @Override
    public boolean isVisibleInSettings() {
        return true;
    }

    @NotNull
    @Override
    public SettingsKey<HintSettings> getKey() {
        return KEY;
    }

    private static SettingsKey<String> key = new SettingsKey<>("SimpleInlayProvider");


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
                                                        @NotNull InlResult result,
                                                        @Nullable String hover) {
        // <-------------------- ICON ------------------->
        //Icon icon = AllIcons.Toolwindows.ToolWindowFind;
        //Icon icon = IconLoader.getIcon("/toolwindows/toolWindowFind_dark.svg", AllIcons.class);


        //<-------------------- HOVER ------------------->
        InlayPresentation text = factory.smallText(result.getRegularText() + " ");

        var hovered = factory.changeOnHover(text, () -> {
            InlayPresentation onHover = factory.smallText(result.getRegularText() + "  :  " + hover + " ");
            return referenceColor(onHover, highlightColor);
        }, __ -> true);



        var currentComplexity = RealtimeState.getInstance().getCurrentComplexity();
        var limitOfComplexity = RealtimeState.getInstance().getLimitOfComplexity();

        var color  = defaultColor;

        if (currentComplexity >= limitOfComplexity)
            color = errorColor;

        return referenceColor(factory.roundWithBackgroundAndSmallInset(hovered), color);
    }

    @NotNull
    private static InlayPresentation referenceColor(@NotNull InlayPresentation presentation, Color color) {
        return new AttributesTransformerPresentation(presentation,
                __ -> {
                    TextAttributes attributes = new TextAttributes();
                    attributes.setForegroundColor(new JBColor(color, color));
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
        RealtimeState.getInstance().updateStateByFile(file.getVirtualFile());

        PresentationFactory factory = new PresentationFactory((EditorImpl) editor);

        Set<Rule> rules;
        try {
            rules = new ComplexityMetricsService().getRules(new CddJsonResourceService().loadMetrics());
        } catch (ResourceNotFoundException exception) {
            return (element, editor1, sink) -> true;
        }

        if(!CodeSmellIntegrationUtil.taskRunning) {
            var codeSmell = CodeSmellIntegrationUtil.getInstance();
            codeSmell.detectFeatureEnvy();
        }

        return (element, editor1, sink) -> {

            if (!RealtimeState.getInstance().isShowComplexityInTheCode())
                return true;


            if (!this.verifyElementType(rules, element))
                return true;


            List<InlResult> hints = new SmartList<>();


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
                    String prop = "{0, choice, 1# 1 Complexity|2# {0,number} Complexity}";
                    return MessageFormat.format(prop, Objects.requireNonNull(rules.stream().filter(
                            rule -> isInstanceOf(rule, element)).findAny().orElse(null)).getCost());
                }
            });


            if (!hints.isEmpty()) {
                int offset = element.getTextRange().getStartOffset();
                int line = editor1.getDocument().getLineNumber(offset);
                int lineStart = editor1.getDocument().getLineStartOffset(line);
                int indent = offset - lineStart;

                // <-- NEW INDENTATION -->
                var lineText = editor1.getDocument().getText(new TextRange(lineStart, offset)).split("");
                var padding = 0;
                for (String s : lineText) {
                    if (!s.equals(" ")) {
                        break;
                    }
                    padding++;
                }
                // <-- NEW INDENTATION -->


                InlayPresentation[] presentations = new InlayPresentation[hints.size() * 2 + 1];


//                presentations[0] = factory.textSpacePlaceholder(indent, false);
                // <-- NEW INDENTATION -->
                presentations[0] = factory.textSpacePlaceholder(padding, false);
                // <-- NEW INDENTATION -->

                int o = 1;
                for (int i = 0; i < hints.size(); i++) {
                    InlResult hint = hints.get(i);
                    if (i != 0) {
                        presentations[o++] = factory.text(" ");
                    }
                    presentations[o++] = createPresentation(factory, element, editor1, hint, this.getRuleName(rules, element));
                }
                presentations[o] = factory.textSpacePlaceholder(10, false); // placeholder for "Settings..."

                Icon icon = AllIcons.Actions.MoveDown;

                InlayPresentation seq = factory.seq(presentations);
                //InlayPresentation seq2 = factory.container(seq, new InlayPresentationFactory.Padding(5,5,5,5), new InlayPresentationFactory.RoundedCorners(10,10), new Color(255,0,0), 1);


//                <----------------- HOVER -------------------->
//                InlayPresentation withAppearingSettings = factory.changeOnHover(seq, () -> {
//                    InlayPresentation[] trimmedSpace = Arrays.copyOf(presentations, presentations.length - 1);
//                    InlayPresentation[] spaceAndSettings = {factory.textSpacePlaceholder(1, false), factory.icon(icon)};
//                    InlayPresentation[] withSettings = ArrayUtil.mergeArrays(trimmedSpace, spaceAndSettings);
//                    return factory.seq(withSettings);
//                }, e -> true);
//
//                sink.addBlockElement(lineStart, true, true, 0, withAppearingSettings);

                sink.addBlockElement(lineStart, true, true, 0, seq);
            }
            return true;
        };
    }



    @Override
    public boolean isLanguageSupported(@NotNull Language language) {
        return language.getID().equals("JAVA");
    }

    private boolean verifyElementType(Set<Rule> rules, Object element) {
        return rules.stream().anyMatch(rule -> this.isInstanceOf(rule, element));
    }

    private boolean isInstanceOf(Rule rule, Object element) {
        final var psiElement = (PsiElement) element;

        if(rule.getObject() instanceof List<?> && Statement.CONTEXTUAL_COUPLING.name().equals(rule.getName())) {
          return  ((List<?>) rule.getObject()).stream().anyMatch(object -> {

              final var isInstanceToAnalyze = ((Class) object).isInstance(element);

              if(isInstanceToAnalyze)
                return ContextualCouplingUtils.isContextualCoupling(psiElement.getText());

              return false;
          });
        }

        if(Statement.FEATURE_ENVY.name().equals(rule.getName())) {
            if (ObjectUtils.isNotEmpty(CodeSmellIntegrationUtil.getInstance().refactorings) && psiElement instanceof PsiMethod) {
                return CodeSmellIntegrationUtil.getInstance().refactorings
                        .stream().anyMatch(refactoring -> refactoring.getMethod().equals(psiElement));
            }
            return false;
        }

        return ((Class) rule.getObject()).isInstance(element);
    }

    private String getRuleName(Set<Rule> rules, Object element) {
        return rules.stream().filter(rule -> this.isInstanceOf(rule, element)).findFirst().map(Rule::getName).orElse("CDD Complexity").toLowerCase();
    }

}

