package com.example.demo;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;




public class MyIdeaDataDialogWrapper extends DialogWrapper {
    JPanel panel = new JPanel(new GridBagLayout());
    JTextField textMode = new JTextField();
    JTextField textUsername = new JTextField();
    JTextField textPassword = new JPasswordField();

    protected MyIdeaDataDialogWrapper() {
        super(true);
        init();
        super.setTitle("My Idea Demo Title 2");
        var state = MyIdeaDemoPluginSettings.getInstance().getState();
        if(state != null){
            textMode.setText(state.mode);
        }

//        super.getContentPane().setPreferredSize(new Dimension(400, 400));
//        super.getContentPanel().add(this.createCenterPanel());
//        super.setOKButtonText("OK");
//        super.myOKAction.setEnabled(true);
//        super.myOKAction.
    }



    @Override
    protected @Nullable JComponent createCenterPanel() {

        var gb = new GridBag()
                .setDefaultInsets(JBUI.insets(0, 4, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_VGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);



        panel.setPreferredSize(new Dimension(400, 200));
        panel.setSize(new Dimension(400, 200));

        panel.add(label("Mode"), gb.nextLine().next().weightx(0.2));
        panel.add(textMode, gb.nextLine().next().weightx(0.8));

        panel.add(label("Username"), gb.nextLine().next().weightx(0.2));
        panel.add(textUsername, gb.nextLine().next().weightx(0.8));

        panel.add(label("Password"), gb.nextLine().next().weightx(0.2));
        panel.add(textPassword, gb.nextLine().next().weightx(0.8));

        panel.setVisible(true);
        pack();

        return panel;
    }

    private JComponent label(String text) {
        JBLabel label = new JBLabel(text);
        label.setComponentStyle(UIUtil.ComponentStyle.SMALL);
        label.setFontColor(UIUtil.FontColor.BRIGHTER);
        label.setBorder(JBUI.Borders.empty(0, 5, 2, 8));

        return label;
    }

    @Override
    protected void doOKAction() {
        var username = textUsername.getText();
        var password = textPassword.getText();
        var mode = textMode.getText();

        var state = MyIdeaDemoPluginSettings.getInstance().getState();
        if(state != null){
            state.mode = mode;
        }

        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }

}
