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

    @SuppressWarnings("rawtypes")
    public CheckBoxList(final Vector listData){
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

    static class CheckBoxRenderer extends JCheckBox implements ListCellRenderer {

        private static final long serialVersionUID = -8835050180063363917L;

        @Override
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

        @Override
        protected MouseInputListener createMouseInputListener() {
            return this;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            CheckBoxList.this.requestFocus();
        }

        @Override
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

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

    }

}
