package org.joy.generator.ui.component;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class EditableTable extends JTable {

    private static final long serialVersionUID = -9082130706292915465L;
    int                       myCol            = -1;
    TableCellEditor           myEditor;                                 // 边框编辑器

    public void setComboCell(int col, TableCellEditor editor) {
        this.myCol = col;
        this.myEditor = editor;
    }

    public TableCellEditor getCellEditor(int row, int col) {
        if (col == myCol && myEditor != null) return myEditor;
        return super.getCellEditor(row, col);
    }

    /**
     * 返回数据类型
     */
    public Class<?> getColumnClass(int myCol) {
        return getValueAt(0, myCol).getClass();
    }

}
