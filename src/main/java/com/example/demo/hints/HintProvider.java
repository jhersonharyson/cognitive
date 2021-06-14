package com.example.demo.hints;


import com.cdd.model.Rule;
import com.cdd.service.Analyzer;
import com.cdd.service.CddJsonResource;
import com.cdd.service.ComplexityMetricsService;
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
import java.util.*;
import java.util.List;

import static io.grpc.internal.ConscryptLoader.isPresent;

@SuppressWarnings("UnstableApiUsage")
public class HintProvider implements InlayHintsProvider<HintSettings> {
    private static final SettingsKey<HintSettings> KEY = new SettingsKey<>("JavaLens");

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
        // <-------------------- ICON ------------------->
        //Icon icon = AllIcons.Toolwindows.ToolWindowFind;
        //Icon icon = IconLoader.getIcon("/toolwindows/toolWindowFind_dark.svg", AllIcons.class);


//         <-------------------- HOVER ------------------->
//                InlayPresentation text = factory.smallText(result.getRegularText());
//                return factory.changeOnHover(text, () -> {
//                    InlayPresentation onClick = factory.onClick(text, MouseButton.Left, (___, __) -> {
//                        result.onClick(editor, element);
//                        return null;
//                    });
//                    return referenceColor(onClick);
//                }, __ -> true);


        return factory.smallText(result.getRegularText());
    }

    @NotNull
    private static InlayPresentation referenceColor(@NotNull InlayPresentation presentation) {
        return new AttributesTransformerPresentation(presentation,
                __ -> {
                    TextAttributes attributes = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(EditorColors.DELETED_TEXT_ATTRIBUTES).clone();
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

        RealtimeState.getInstance().updateStateByFile(file.getVirtualFile());

        PresentationFactory factory = new PresentationFactory((EditorImpl) editor);

        Set<Rule> rules = new ComplexityMetricsService().getRules(new CddJsonResource().loadMetrics());

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
                    String prop = "{0, choice, 1#// 1 Complexity|2#// {0,number} Complexity}";
                    return MessageFormat.format(prop, Objects.requireNonNull(rules.stream().filter(rule -> isInstanceOf(rule, element)).findAny().orElse(null)).getCost());
                }
            });


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
//                InlayPresentation seq2 = factory.container(seq, new InlayPresentationFactory.Padding(5,5,5,5), new InlayPresentationFactory.RoundedCorners(10,10), new Color(255,0,0), 1);


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
        return ((Class) rule.getObject()).isInstance(element);
    }

}

