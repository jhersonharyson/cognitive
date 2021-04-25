package com.example.demo.toolWindow;

import com.cdd.service.Analyzer;
import com.example.demo.utils.RealtimeState;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class RulesWindow {
    private JTable table;
    private JPanel window;
    private JLabel className;
    private JLabel limitOfComplexity;
    private JLabel currentComplexity;
    private JButton closeButton;


    public RulesWindow() {
        this.load();
        this.render();
    }



    public void render() {
        var dimension = new Dimension(800, 600);



        JFrame parentFrame = new JFrame();
        parentFrame.setSize(dimension);

        JDialog frame = new JDialog(parentFrame, "CDD Rules", true);

        this.window.setPreferredSize(dimension);

        this.closeButton.addActionListener(e -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            frame.setVisible(false);
            frame.setAlwaysOnTop(false);
            frame.setFocusableWindowState(false);
        });

        frame.getContentPane().add(this.window);

        frame.setSize(dimension);

        center(frame);

        frame.setResizable(false);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setFocusableWindowState(true);



    }

    public static void center(JDialog frame) {

        // get the size of the screen, on systems with multiple displays,
        // the primary display is used
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // calculate the new location of the window
        int w = frame.getSize().width;
        int h = frame.getSize().height;

        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // moves this component to a new location, the top-left corner of
        // the new location is specified by the x and y
        // parameters in the coordinate space of this component's parent
        frame.setLocation(x, y);

    }


    private void load() {
        this.loadClassName();
        this.loadLimitOfComplexity();
        this.loadCurrentComplexity();
        this.loadTable();
    }

    private void loadTable() {
        Object[] columnNames = { "Metric", "Cost", "Times", "Total" };

        var model = new DefaultTableModel(columnNames, 0);

//        model.addRow(columnNames);

        var rules = new Analyzer().readPsiFile(PsiManager.getInstance(ProjectManager.getInstance().getDefaultProject()).findFile(RealtimeState.getInstance().getVirtualFile())).getRules();

        rules.forEach(rule -> {
            Object[] row = {rule.getName(), rule.getCost(), rule.getTimes(), rule.getTimes() * rule.getCost()};
            model.addRow(row);
        });

        this.table.setModel(model);
    }

    private void loadClassName() {
        this.className.setText(RealtimeState.getInstance().getVirtualFile().getName());
    }

    private void loadLimitOfComplexity() {
        this.limitOfComplexity.setText(String.valueOf(RealtimeState.getInstance().getLimitOfComplexity()));
    }

    private void loadCurrentComplexity() {
        this.currentComplexity.setText(String.valueOf(RealtimeState.getInstance().getCurrentComplexity()));
    }


}
