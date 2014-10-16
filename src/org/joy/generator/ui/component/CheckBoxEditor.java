package org.joy.generator.ui.component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;

public class CheckBoxEditor extends DefaultCellEditor {

  private static final long serialVersionUID = -2478942982494661133L;

  public CheckBoxEditor(JCheckBox checkBox) {
    super(checkBox);
  }

  public CheckBoxEditor() {
    super(new JCheckBox());
  }
  
}
