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
