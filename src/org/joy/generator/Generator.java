package org.joy.generator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.joy.config.Configuration;
import org.joy.config.TypeMapping;
import org.joy.config.model.DatabaseElement;
import org.joy.db.Database;
import org.joy.db.DatabaseFactory;
import org.joy.db.model.Column;
import org.joy.db.model.Table;
import org.joy.generator.ui.TreeNodeData;
import org.joy.generator.ui.component.ComboBoxEditor;
import org.joy.generator.ui.component.EditableTable;
import org.joy.task.CloseConnectionTask;
import org.joy.task.TaskListener;
import org.joy.util.ClassloaderUtility;
import org.joy.util.ObjectFactory;
import org.joy.util.StringUtil;

public class Generator extends JFrame {

    private static final long      serialVersionUID = -7813705897974255551L;

    private static Font            font             = new Font("宋体", Font.PLAIN, 12);
    private String[]               headers          = { "字段名", "字段类型", "JAVA类型", "大小", "主键", "唯一", "外键", "可空", "默认值",
                        "注释"                       };

    private JPanel                 contentPane;
    private JSplitPane      contentSplitPane;
    private JMenuItem              mntmConnect;
    private JMenuItem              mntmDisconnect;

    private DefaultTreeModel       tablesTreeModel;
    private JTree                  tablesTree;

    private DefaultMutableTreeNode tablesNode;
    private DefaultMutableTreeNode viewsNode;

    private ImageIcon              folderIcon       = createImageIcon("icon/folder.png");
    private ImageIcon              tableIcon        = createImageIcon("icon/table.png");
    private ImageIcon              viewIcon         = createImageIcon("icon/view.png");

    private JPopupMenu             tablesTreePopupMenu;
    private JMenuItem              mntmTableInfo;
    private JScrollPane            rightScrollPane;
    private EditableTable          tableGrid;
    private DefaultTableModel      tableGridModel;

    private JSplitPane             rightSplitPane;
    private JPanel                 rightTopPanel;
    private JButton                btnGenerate;

    private Configuration          configuration;
    private TypeMapping            typeMapping;
    private DbManagementDialog     dbManagementDialog;
    private DatabaseElement        databaseElement;
    private Connection             connection;
    private Table                  tableModel;
    private String                 classPath;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    Generator frame = new Generator();
                    frame.setVisible(true);
                    frame.centerScreen();
                    frame.contentSplitPane.setDividerLocation(0.25);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    /**
     * Create the frame.
     */
    public Generator(){
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 886, 566);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnNewMenu = new JMenu("File");
        menuBar.add(mnNewMenu);

        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onExitActionPerformed(e);
            }
        });

        mntmConnect = new JMenuItem("Connect...");
        mntmConnect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onConnectActionPerformed(e);
            }
        });
        mnNewMenu.add(mntmConnect);

        mntmDisconnect = new JMenuItem("DisConnect");
        mntmDisconnect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                disConnection();
            }
        });
        mnNewMenu.add(mntmDisconnect);
        mnNewMenu.addSeparator();
        mnNewMenu.add(menuItemExit);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        contentSplitPane = new JSplitPane();
        contentSplitPane.setDividerSize(6);
        contentSplitPane.setDividerLocation(200);
        contentPane.add(contentSplitPane, BorderLayout.CENTER);

        TreeNodeData nodedata = new TreeNodeData("root", folderIcon, "");
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(nodedata);

        nodedata = new TreeNodeData("表", folderIcon, "");
        tablesNode = new DefaultMutableTreeNode(nodedata);
        rootNode.add(tablesNode);
        nodedata = new TreeNodeData("视图", folderIcon, "");
        viewsNode = new DefaultMutableTreeNode(nodedata);
        rootNode.add(viewsNode);

        JScrollPane leftScrollPane = new JScrollPane();
        contentSplitPane.setLeftComponent(leftScrollPane);

        leftScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tablesTreeModel = new DefaultTreeModel(rootNode);
        tablesTree = new javax.swing.JTree(tablesTreeModel);
        tablesTree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                onTablesTreeMouseReleased(e);
            }

            public void mousePressed(MouseEvent e) {
                onTablesTreeMousePressed(e);
            }
        });

        tablesTree.setRootVisible(false);
        tablesTree.setShowsRootHandles(true);
        leftScrollPane.setViewportView(tablesTree);
        tablesTree.setCellRenderer(new DefaultTreeCellRenderer() {

            private static final long serialVersionUID = 1033798115180836224L;

            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                          boolean leaf,

                                                          int row, boolean hasFocus) {

                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                if (value instanceof DefaultMutableTreeNode) {
                    Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
                    setIcon(((TreeNodeData) userObject).getIcon());
                } else {
                    setIcon(createImageIcon("./icon/folder.png"));
                }
                return this;
            }
        });

        tablesTreePopupMenu = new JPopupMenu();

        mntmTableInfo = new JMenuItem("获取表信息");
        tablesTreePopupMenu.add(mntmTableInfo);
        mntmTableInfo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loadDatabaseTable();
            }
        });

        rightSplitPane = new JSplitPane();
        rightSplitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        rightSplitPane.setDividerSize(6);
        rightSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        rightSplitPane.setDividerLocation(30);
        rightSplitPane.setEnabled(false);
        contentSplitPane.setRightComponent(rightSplitPane);

        rightScrollPane = new JScrollPane();
        rightScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        rightSplitPane.setBottomComponent(rightScrollPane);

        rightScrollPane.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                resizeTableGrid(true);
            }
        });

        tableGrid = new EditableTable();
        rightScrollPane.setViewportView(tableGrid);

        rightTopPanel = new JPanel();
        rightTopPanel.setAlignmentY(0.0f);
        rightTopPanel.setAlignmentX(0.0f);
        rightTopPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        rightTopPanel.setPreferredSize(new Dimension(300, 100));
        rightSplitPane.setTopComponent(rightTopPanel);
        rightTopPanel.setLayout(new BorderLayout(0, 0));

        btnGenerate = new JButton("生成...");
        btnGenerate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onGenerateActionPerformed(e);
            }
        });
        btnGenerate.setEnabled(false);
        rightTopPanel.add(btnGenerate);



        mntmConnect.setEnabled(true);
        mntmDisconnect.setEnabled(false);
        InitGlobalFont(font);
        File f = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
        classPath = f.getParentFile().getPath() + File.separator;

        configuration = new Configuration(classPath);
        try {
            configuration.loadConfiguration();
            if (configuration.getClassPathEntries().size() > 0) {
                ClassLoader classLoader = ClassloaderUtility.getCustomClassloader(classPath,
                                                                                  configuration.getClassPathEntries());
                ObjectFactory.addExternalClassLoader(classLoader);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }

        typeMapping = new TypeMapping(classPath);
        try {
            typeMapping.loadMappgin();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }

        drawTableGrid();
    }

    private void loadTableTree(String schema) {
        String schemaPattern = null;
        try {
            tablesNode.removeAllChildren();
            viewsNode.removeAllChildren();
            if(StringUtil.isNotEmpty(schema)){
                schemaPattern = schema;
            }
            ResultSet rs = connection.getMetaData().getTables(null, schemaPattern, "%", null);
            while (rs.next()) {
                TreeNodeData data;
                DefaultMutableTreeNode tableNode;
                String tableSchema = rs.getString(2);
                String tableName = rs.getString(3);
                if(StringUtil.isNotEmpty(tableSchema)){
                    tableName = tableSchema + "." + tableName;
                }
                if ("VIEW".equalsIgnoreCase(rs.getString(4))) {
                    data = new TreeNodeData(tableName, viewIcon, tableName);
                    tableNode = new DefaultMutableTreeNode(data);
                    viewsNode.add(tableNode);
                } else {
                    data = new TreeNodeData(tableName, tableIcon, tableName);
                    tableNode = new DefaultMutableTreeNode(data);
                    tablesNode.add(tableNode);
                }
            }
            tablesTreeModel.reload();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void centerScreen() {
        Dimension dim = getToolkit().getScreenSize();
        Rectangle abounds = getBounds();
        setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
    }

    public void disConnection() {

        CloseConnectionTask cct = new CloseConnectionTask(connection);
        cct.addTaskListener(new TaskListener() {

            public void taskFinished() {
                mntmConnect.setEnabled(true);
                mntmDisconnect.setEnabled(false);
                tablesNode.removeAllChildren();
                viewsNode.removeAllChildren();
                tablesTreeModel.reload();
                tableModel = null;
                tableGridModel.setRowCount(0);
                btnGenerate.setEnabled(false);
            }

            public void taskStatus(Object obj) {

            }

            public void taskResult(Object obj) {

            }

            public void taskError(Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cct.start();
    }

    private void shutdown() {
        disConnection();
        dispose();
    }

    private void onConnectActionPerformed(java.awt.event.ActionEvent evt) {
        if (dbManagementDialog == null) {
            dbManagementDialog = new DbManagementDialog(configuration);
        }
        dbManagementDialog.setVisible(true);
        databaseElement = dbManagementDialog.getDatabase();
        if (dbManagementDialog.getReturnStatus() == DbManagementDialog.RET_OK && databaseElement != null) {
            try {
                disConnection();
                connection = databaseElement.connect();
                if (connection != null) {
                    mntmConnect.setEnabled(false);
                    mntmDisconnect.setEnabled(true);
                    loadTableTree(databaseElement.getSchema());
                }
            } catch (ClassNotFoundException cfe) {
                JOptionPane.showMessageDialog(this, "找不到你选择的 JDBC Driver 类.", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onExitActionPerformed(java.awt.event.ActionEvent evt) {
        this.shutdown();
    }

    private void onTablesTreeMouseReleased(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            TreePath selPath = tablesTree.getPathForLocation(evt.getX(), evt.getY());
            tablesTree.setSelectionPath(selPath);
            if (selPath == null || selPath.getPath() == null) {
                return;
            }
            if (selPath.getPath().length == 3) {
                tablesTreePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }

    private void onTablesTreeMousePressed(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            loadDatabaseTable();
        }
    }

    private void resizeTableGrid(boolean bool) {
        Dimension containerwidth = null;
        if (!bool) {
            containerwidth = rightScrollPane.getPreferredSize();
        } else {
            containerwidth = rightScrollPane.getSize();
        }
        int allwidth = tableGrid.getIntercellSpacing().width;
        for (int j = 0; j < tableGrid.getColumnCount(); j++) {
            int max = 0;
            for (int i = 0; i < tableGrid.getRowCount(); i++) {
                Object cellValue = tableGrid.getValueAt(i, j);
                if (cellValue == null) {
                    cellValue = "";
                }
                int width = tableGrid.getCellRenderer(i, j).getTableCellRendererComponent(tableGrid, cellValue, false,
                                                                                          false, i, j).getPreferredSize().width;
                if (width > max) {
                    max = width;
                }
            }
            int headerwidth = tableGrid.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(tableGrid,
                                                                                                            tableGrid.getColumnModel().getColumn(j).getIdentifier(),
                                                                                                            false,
                                                                                                            false, -1,
                                                                                                            j).getPreferredSize().width;
            max += headerwidth;
            tableGrid.getColumnModel().getColumn(j).setPreferredWidth(max);
            allwidth += max + tableGrid.getIntercellSpacing().width;
        }
        allwidth += tableGrid.getIntercellSpacing().width;
        if (allwidth > containerwidth.width) {
            tableGrid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        } else {
            tableGrid.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        }
    }

    private void drawTableGrid() {

        Object[][] cellData = new Object[0][headers.length];
        tableGridModel = new DefaultTableModel(cellData, headers) {

            private static final long serialVersionUID = 880033063879582590L;

            public boolean isCellEditable(int row, int column) {
                if (column == 2 || column == 7 || column == 9) {
                    return true;
                } else return false;
            }
        };
        tableGridModel.addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getLastRow() >= 0) {
                    String columnName = tableGrid.getValueAt(e.getLastRow(), 0).toString();
                    String value = tableGrid.getValueAt(e.getLastRow(), e.getColumn()).toString();
                    Column column = tableModel.getColumn(columnName);
                    if (column != null) {
                        if (e.getColumn() == 2) {
                            column.setJavaType(value);
                        } else if (e.getColumn() == 7) {
                            column.setNullable(Boolean.parseBoolean(value));
                        } else if (e.getColumn() == 9) {
                            column.setRemarks(value);
                        }
                    }
                }
            }
        });
        tableGrid.setModel(tableGridModel);
        tableGrid.setRowHeight(22);

        ((EditableTable) tableGrid).setComboCell(2, new ComboBoxEditor(typeMapping.getAllJavaTypes()));// 第2列为下拉

        resizeTableGrid(true);
    }

    private void loadDatabaseTable() {
        TreePath selPath = tablesTree.getSelectionPath();
        if (selPath == null || selPath.getPath() == null) {
            return;
        }
        if (selPath.getPath().length == 3) {
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selPath.getPath()[2];
            String nodeText = ((TreeNodeData) selNode.getUserObject()).getText();
            String tableSchema = null;
            String tableName = nodeText;
            if (nodeText.indexOf(".")>0) {
                tableName = nodeText.substring(nodeText.indexOf(".")+1);
                tableSchema = nodeText.substring(0, nodeText.indexOf("."));
            }
            loadTableGridData(tableSchema, tableName);
        }
    }

    private void loadTableGridData(String tableSchema, String tableName) {
        try {
            Database db = DatabaseFactory.createDatabase(connection, typeMapping);
            tableModel = db.getTable(null, tableSchema, tableName);
            Map<String, Column> columnMap = new LinkedHashMap<String, Column>();
            List<Column> keyCols = tableModel.getPrimaryKeys();
            for (Column col : keyCols) {
                columnMap.put(col.getColumnName(), col);
            }
            List<Column> baseCols = tableModel.getColumns();
            for (Column col : baseCols) {
                columnMap.put(col.getColumnName(), col);
            }

            Object[][] cellData = new Object[columnMap.size()][headers.length];
            Set<Entry<String, Column>> entrySet = columnMap.entrySet();
            int row = 0;
            for (Entry<String, Column> entry : entrySet) {
                Column item = entry.getValue();
                int col = 0;
                cellData[row][col++] = item.getColumnName();
                cellData[row][col++] = item.getJdbcTypeName();
                cellData[row][col++] = item.getJavaType();
                cellData[row][col++] = item.getSize();
                cellData[row][col++] = item.isPrimaryKey();

                cellData[row][col++] = item.isUnique();
                cellData[row][col++] = item.isForeignKey();
                cellData[row][col++] = item.isNullable();
                cellData[row][col++] = item.getDefaultValue();
                cellData[row][col++] = item.getRemarks();
                row++;
            }
            tableGridModel.setDataVector(cellData, headers);
            resizeTableGrid(true);
            btnGenerate.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onGenerateActionPerformed(java.awt.event.ActionEvent evt) {
        GenerationDialog generationDialog = new GenerationDialog(configuration, tableModel, classPath);
        generationDialog.setVisible(true);
    }

}
