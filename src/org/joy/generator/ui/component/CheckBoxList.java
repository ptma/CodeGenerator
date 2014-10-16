package org.joy.generator.ui.component;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicListUI;

public class CheckBoxList extends JList {

    private static final long serialVersionUID = -5413051943282511041L;

    public CheckBoxList(){
        super();
        initCheckBox();
    }

    public CheckBoxList(final Vector<?> listData){
        super(listData);
        initCheckBox();
    }

    public CheckBoxList(final Object[] listData){
        super(listData);
        initCheckBox();
    }

    public CheckBoxList(ListModel dataModel){
        super(dataModel);
        initCheckBox();
    }

    private void initCheckBox() {
        this.setCellRenderer(new CheckBoxRenderer());
        this.setUI(new CheckBoxListUI());
    }

    class CheckBoxRenderer extends JCheckBox implements ListCellRenderer {

        private static final long serialVersionUID = -8835050180063363917L;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {

            this.setSelected(isSelected);
            this.setText(value.toString());
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return this;
        }
    }

    class CheckBoxListUI extends BasicListUI implements MouseInputListener {

        protected MouseInputListener createMouseInputListener() {
            return this;
        }

        public void mouseClicked(MouseEvent e) {
            CheckBoxList.this.requestFocus();
        }

        public void mousePressed(MouseEvent e) {
            int row = CheckBoxList.this.locationToIndex(e.getPoint());

            boolean temp = CheckBoxList.this.getSelectionModel().isSelectedIndex(row);

            if (!temp) {
                CheckBoxList.this.addSelectionInterval(row, row);
            } else {
                CheckBoxList.this.removeSelectionInterval(row, row);
            }
            CheckBoxList.this.requestFocus();
        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }

        public void mouseDragged(MouseEvent e) {

        }

        public void mouseMoved(MouseEvent e) {

        }

    }

}
