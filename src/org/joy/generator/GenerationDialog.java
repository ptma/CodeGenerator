/*
 * Copyright 2014 ptma@163.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joy.generator;

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.joy.config.Configuration;
import org.joy.config.model.TemplateElement;
import org.joy.db.model.Table;
import org.joy.generator.engine.EngineBuilder;
import org.joy.generator.engine.TemplateEngine;
import org.joy.generator.ui.component.CheckBoxList;
import org.joy.util.StringUtil;

public class GenerationDialog extends JDialog {

    private static final Logger LOGGER           = Logger.getLogger(GenerationDialog.class);

    private static final long serialVersionUID = 6159091936841897188L;

    private final JPanel      contentPanel     = new JPanel();
    private JTextField        textTargetProject;
    private JTextField        textBasePackage;
    private JTextField        textModuleName;
    private CheckBoxList      templatesList;

    private Configuration     configuration;
    private Table             tableModel;
    private JTextField        textTableName;
    private JTextField        textTableAlias;
    private EngineBuilder     engineBuilder;

    /**
     * Create the dialog.
     */
    public GenerationDialog(Configuration configuration, Table tableModel, String classPath){

        setModal(true);
        setTitle("生成代码");
        setResizable(false);
        setBounds(100, 100, 419, 485);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblTargetProject = new JLabel("代码保存路径");
        lblTargetProject.setHorizontalAlignment(SwingConstants.TRAILING);
        lblTargetProject.setBounds(10, 13, 72, 15);
        contentPanel.add(lblTargetProject);

        textTargetProject = new JTextField();
        textTargetProject.setBounds(92, 10, 311, 21);
        contentPanel.add(textTargetProject);
        textTargetProject.setColumns(10);

        textBasePackage = new JTextField();
        textBasePackage.setBounds(92, 41, 311, 21);
        contentPanel.add(textBasePackage);
        textBasePackage.setColumns(10);

        textModuleName = new JTextField();
        textModuleName.setBounds(92, 72, 130, 21);
        contentPanel.add(textModuleName);
        textModuleName.setColumns(10);

        JLabel lblBasePackage = new JLabel("基准包");
        lblBasePackage.setHorizontalAlignment(SwingConstants.TRAILING);
        lblBasePackage.setBounds(28, 44, 54, 15);
        contentPanel.add(lblBasePackage);

        JLabel lblModuleName = new JLabel("模块名");
        lblModuleName.setHorizontalAlignment(SwingConstants.TRAILING);
        lblModuleName.setBounds(28, 75, 54, 15);
        contentPanel.add(lblModuleName);

        JPanel panel = new JPanel();
        panel.setBounds(92, 103, 311, 234);
        contentPanel.add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane);

        templatesList = new CheckBoxList();
        templatesList.setBounds(0, 0, 1, 1);
        scrollPane.setViewportView(templatesList);

        JLabel lblTemplates = new JLabel("代码模板");
        lblTemplates.setHorizontalAlignment(SwingConstants.TRAILING);
        lblTemplates.setBounds(28, 103, 54, 15);
        contentPanel.add(lblTemplates);

        textTableName = new JTextField();
        textTableName.setEditable(false);
        textTableName.setBounds(92, 347, 130, 21);
        contentPanel.add(textTableName);
        textTableName.setColumns(10);

        textTableAlias = new JTextField();
        textTableAlias.setBounds(92, 378, 130, 21);
        contentPanel.add(textTableAlias);
        textTableAlias.setColumns(10);

        JLabel lblTableName = new JLabel("表名");
        lblTableName.setHorizontalAlignment(SwingConstants.TRAILING);
        lblTableName.setBounds(28, 350, 54, 15);
        contentPanel.add(lblTableName);

        JLabel lblTableAlias = new JLabel("别名");
        lblTableAlias.setHorizontalAlignment(SwingConstants.TRAILING);
        lblTableAlias.setBounds(28, 381, 54, 15);
        contentPanel.add(lblTableAlias);
        JPanel buttonPane = new JPanel();
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        JButton btnOK = new JButton("生成");
        buttonPane.add(btnOK);
        btnOK.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                okButtonClick();
            }
        });
        btnOK.setMnemonic(KeyEvent.VK_ENTER);
        btnOK.setActionCommand("OK");
        getRootPane().setDefaultButton(btnOK);

        JButton btnCancel = new JButton("取消");
        buttonPane.add(btnCancel);
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doClose();
            }
        });
        btnCancel.setMnemonic(KeyEvent.VK_CANCEL);
        btnCancel.setActionCommand("Cancel");

        this.configuration = configuration;
        this.tableModel = tableModel;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        centerScreen();
        loadConfiguration();

        engineBuilder = new EngineBuilder(classPath);
    }

    public void centerScreen() {
        Dimension dim = getToolkit().getScreenSize();
        Rectangle abounds = getBounds();
        setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
    }

    private void loadConfiguration() {
        textTargetProject.setText(configuration.getTargetProject());
        textBasePackage.setText(configuration.getBasePackage());
        textModuleName.setText(configuration.getModuleName());

        templatesList.setListData(configuration.getTemplates().toArray());

        templatesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        for (int i = 0; i < configuration.getTemplates().size(); i++) {
            TemplateElement templateElement = configuration.getTemplates().get(i);
            if (templateElement.isSelected()) {
                templatesList.addSelectionInterval(i, i);
            }
        }

        textTableName.setText(tableModel.getTableName());
        textTableAlias.setText(tableModel.getTableAlias());
    }

    private void doClose() {
        setVisible(false);
        dispose();
    }

    private void okButtonClick() {
        Object[] selectedValues = templatesList.getSelectedValues();
        if (selectedValues.length==0) {
            JOptionPane.showMessageDialog(this, "请选择模板.", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String targetProject = textTargetProject.getText();
        if (StringUtil.isEmpty(targetProject)) {
            JOptionPane.showMessageDialog(this, "请输入代码保存路径.", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String basePackage = textBasePackage.getText();
        if (StringUtil.isEmpty(basePackage)) {
            JOptionPane.showMessageDialog(this, "请输入基准包.", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String moduleName = textModuleName.getText();
        if (StringUtil.isEmpty(moduleName)) {
            JOptionPane.showMessageDialog(this, "请输入模块名.", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String tableAlias = textTableAlias.getText();
        if (StringUtil.isEmpty(moduleName)) {
            JOptionPane.showMessageDialog(this, "请输入表别名.", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        processSelectedTemplates(selectedValues, targetProject, basePackage, moduleName, tableAlias);
    }

    private void processSelectedTemplates(Object[] selectedTemplateElements, String targetProject, String basePackage, String moduleName, String tableAlias){
        configuration.setTargetProject(targetProject);
        configuration.setBasePackage(basePackage);
        configuration.setModuleName(moduleName);

        tableModel.setTableAlias(tableAlias);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("targetProject", configuration.getTargetProject());
        model.put("basePackage", configuration.getBasePackage());
        model.put("moduleName", configuration.getModuleName());
        model.put("table", tableModel);

        for (TemplateElement templateElement : configuration.getTemplates()) {
            templateElement.setSelected(false);
        }


        int count = selectedTemplateElements.length;
        for (int i=0;i<count; i++) {
            try {
                TemplateElement templateElement = (TemplateElement) selectedTemplateElements[i];
                templateElement.setSelected(true);

                TemplateEngine templateEngine = engineBuilder.getTemplateEngine(templateElement.getEngine());
                if(templateEngine==null){
                    JOptionPane.showMessageDialog(this, "没有对应的模板引擎: "+templateElement.getEngine(), "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    templateEngine.processToFile(model, templateElement);
                }
            } catch (Exception e) {
                LOGGER.info(e.getMessage(), e);
                JOptionPane.showMessageDialog(this, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
        configuration.save();
        JOptionPane.showMessageDialog(this, "生成完毕.", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}
