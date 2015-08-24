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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.joy.config.Configuration;
import org.joy.config.JdbcDrivers;
import org.joy.config.model.DatabaseElement;
import org.joy.config.model.DriverInfo;
import org.joy.exception.ApplicationException;
import org.joy.util.StringUtil;


public class DbManagementDialog extends JDialog {

    private static final Logger LOGGER           = Logger.getLogger(DbManagementDialog.class);

    private static final long serialVersionUID = -8869055101570094777L;

    private final JPanel contentPanel = new JPanel();
    private JTextField textUrl;
    private JTextField textUsername;
    private JTextField textPassword;
    private JTextField textSchema;
    private JTextField textName;
    private JComboBox driverComboBox;
    private JList historyList;
    private Configuration configuration;

    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    private int returnStatus = RET_CANCEL;
    private DatabaseElement database = null;


    /**
     * Create the dialog.
     */
    public DbManagementDialog(Configuration configuration){
        this.configuration = configuration;
        setModal(true);
        setTitle("数据库连接");
        setResizable(false);
        setBounds(100, 100, 561, 344);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setAlignmentX(0.3f);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JScrollPane leftScrollPane = new JScrollPane();
        leftScrollPane.setBounds(new Rectangle(0, 0, 100, 150));

        JPanel rightPane = new JPanel();




        GroupLayout glContentPanel = new GroupLayout(contentPanel);
        glContentPanel.setHorizontalGroup(
            glContentPanel.createParallelGroup(Alignment.TRAILING)
                .addGroup(glContentPanel.createSequentialGroup()
                    .addComponent(leftScrollPane, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(rightPane, GroupLayout.PREFERRED_SIZE, 395, GroupLayout.PREFERRED_SIZE))
        );
        glContentPanel.setVerticalGroup(
            glContentPanel.createParallelGroup(Alignment.TRAILING)
                .addComponent(leftScrollPane, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addComponent(rightPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
        );

        historyList = new JList();
        historyList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                hisListChange();
            }
        });
        historyList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                listDoubleClick(e);
            }
        });
        leftScrollPane.setViewportView(historyList);
        rightPane.setLayout(null);

        textUrl = new JTextField();
        textUrl.setBounds(74, 72, 299, 21);
        rightPane.add(textUrl);
        textUrl.setColumns(10);

        textPassword = new JTextField();
        textPassword.setBounds(74, 134, 101, 21);
        rightPane.add(textPassword);
        textPassword.setColumns(10);

        textSchema = new JTextField();
        textSchema.setBounds(74, 165, 101, 21);
        rightPane.add(textSchema);
        textSchema.setColumns(10);

        textUsername = new JTextField();
        textUsername.setBounds(74, 103, 101, 21);
        rightPane.add(textUsername);
        textUsername.setColumns(10);

        driverComboBox = new JComboBox();
        driverComboBox.setBounds(74, 41, 299, 21);
        rightPane.add(driverComboBox);

        textName = new JTextField();
        textName.setBounds(74, 10, 101, 21);
        rightPane.add(textName);

        JLabel lblName = new JLabel("Name");
        lblName.setBounds(10, 13, 54, 15);
        rightPane.add(lblName);

        JLabel lblDriver = new JLabel("Driver");
        lblDriver.setBounds(10, 44, 54, 15);
        rightPane.add(lblDriver);

        JLabel lblURL = new JLabel("URL");
        lblURL.setBounds(10, 75, 54, 15);
        rightPane.add(lblURL);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(10, 106, 54, 15);
        rightPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(10, 137, 54, 15);
        rightPane.add(lblPassword);

        JLabel lblSchema = new JLabel("Schema");
        lblSchema.setBounds(10, 168, 54, 15);
        rightPane.add(lblSchema);
        driverComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                driverComboBoxChange(e);
            }
        });
        contentPanel.setLayout(glContentPanel);

        JPanel buttonPane = new JPanel();
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));

        JPanel bottomLeftPane = new JPanel();
        FlowLayout flBottomLeftPane = (FlowLayout) bottomLeftPane.getLayout();
        flBottomLeftPane.setAlignment(FlowLayout.LEFT);
        buttonPane.add(bottomLeftPane);

        JButton btnSave = new JButton("保存");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonClick();
            }
        });
        bottomLeftPane.add(btnSave);

        JButton btnDelete = new JButton("删除");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteButtonClick();
            }
        });
        bottomLeftPane.add(btnDelete);

        JPanel bottomRightPane = new JPanel();
        FlowLayout flBottomRightPane = (FlowLayout) bottomRightPane.getLayout();
        flBottomRightPane.setAlignment(FlowLayout.RIGHT);
        buttonPane.add(bottomRightPane);

        JButton btnTest = new JButton("测试连接...");
        bottomRightPane.add(btnTest);

        JButton btnOK = new JButton("确定");
        bottomRightPane.add(btnOK);
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okButtonClick();
            }
        });
        btnOK.setActionCommand("OK");

        getRootPane().setDefaultButton(btnOK);

        JButton btnCancel = new JButton("取消");
        bottomRightPane.add(btnCancel);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
        btnCancel.setActionCommand("Cancel");

        btnTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testButtonClick(true);
            }
        });


        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        centerScreen();
        loadListAndCombobox();
    }

    public int getReturnStatus() {
        return returnStatus;
    }

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    public void centerScreen() {
        Dimension dim = getToolkit().getScreenSize();
        Rectangle abounds = getBounds();
        setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
    }

    private void loadListAndCombobox() {
        Map<String, DriverInfo> driverMap = JdbcDrivers.getJdbcDriversMap();
        Object[] keyArray = driverMap.keySet().toArray();
        Arrays.sort(keyArray);
        for  (Object key : keyArray) {
            driverComboBox.addItem(driverMap.get(key));
        }
        historyList.setListData(configuration.getConnectionHistory().toArray());
    }

    private void hisListChange(){
        DatabaseElement selDb = (DatabaseElement) historyList.getSelectedValue();
        if(selDb !=null){
            for(int i=0;i<driverComboBox.getItemCount();i++){
                DriverInfo driverInfo = (DriverInfo) driverComboBox.getItemAt(i);
                if (driverInfo.getDriverClass().equals(selDb.getDriverClass())){
                    driverComboBox.setSelectedItem(driverInfo);
                    break;
                }
            }
            textName.setText(selDb.getName());
            textUrl.setText(selDb.getConnectionUrl());
            textUsername.setText(selDb.getUsername());
            textPassword.setText(selDb.getPassword());
            textSchema.setText(selDb.getSchema());
        }

    }

    private void driverComboBoxChange(ItemEvent evt) {
        if (1 == evt.getStateChange()) {
            DriverInfo selectedItem = (DriverInfo)driverComboBox.getSelectedItem();
            textUrl.setText(selectedItem.getUrl());
        }
    }

    private DatabaseElement doConnecting(String name, String driverClass, String connectionUrl, String username, String password,
                           String schema, boolean testing){
        Connection conn = null;
        try {
            DatabaseElement dbItem = new DatabaseElement(name, driverClass, connectionUrl, username, password, schema);
            conn = dbItem.connect();
            if (conn != null) {
                if (testing) {
                    JOptionPane.showMessageDialog(this, "连接成功.", "错误", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "连接失败.", "错误", JOptionPane.INFORMATION_MESSAGE);
            }
            return dbItem;
        } catch (ApplicationException e) {
            LOGGER.info(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.info(e.getMessage(), e);
            }
        }
        return null;
    }

    private DatabaseElement testButtonClick(boolean showSuccessInfomationDialog) {
        DriverInfo selectedItem = (DriverInfo) driverComboBox.getSelectedItem();
        String name = textName.getText();
        String url = textUrl.getText();
        String username = textUsername.getText();
        String password = textPassword.getText();
        String schema = textSchema.getText();
        if (StringUtil.isEmpty(name)) {
            JOptionPane.showMessageDialog(this, "请输入 name.", "提示", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        if (StringUtil.isEmpty(url)) {
            JOptionPane.showMessageDialog(this, "请输入 URL.", "提示", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        if (StringUtil.isEmpty(username)) {
            JOptionPane.showMessageDialog(this, "请输入 Username.", "提示", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }


        return doConnecting(name, selectedItem.getDriverClass(), url, username, password, schema, showSuccessInfomationDialog);
    }

    public DatabaseElement getDatabase(){
        return database;
    }

    private void listDoubleClick(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            okButtonClick();
        }
    }

    private void okButtonClick() {
        database = testButtonClick(false);
        if (database == null) {
            return;
        }
        configuration.addHistory(database);
        doClose(RET_OK);
    }

    private void saveButtonClick() {
        DriverInfo selectedItem = (DriverInfo) driverComboBox.getSelectedItem();
        String name = textName.getText();
        String url = textUrl.getText();
        String username = textUsername.getText();
        String password = textPassword.getText();
        String schema = textSchema.getText();
        DatabaseElement dbItem = new DatabaseElement(name, selectedItem.getDriverClass(), url, username, password, schema);

        configuration.addHistory(dbItem);
        configuration.save();
        historyList.setListData(configuration.getConnectionHistory().toArray());
    }

    private void deleteButtonClick() {
        DatabaseElement selDb = (DatabaseElement) historyList.getSelectedValue();
        if(selDb!=null){
            configuration.removeHistory(selDb);
            configuration.save();
            historyList.setListData(configuration.getConnectionHistory().toArray());
        }
    }
}
