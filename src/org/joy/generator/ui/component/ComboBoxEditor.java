package org.joy.generator.ui.component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class ComboBoxEditor extends DefaultCellEditor {

  private static final long serialVersionUID = 4918413294514340108L;

  public ComboBoxEditor(JCheckBox checkBox) {
    super(checkBox);
  }

  public ComboBoxEditor(String[] value) {
    super(new JComboBox(value));
  }
  
}
