package org.joy.generator.ui;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class TreeNodeData implements Serializable {

  private static final long serialVersionUID = 6843072103831482105L;

  private String text;
  
  private ImageIcon icon;
  
  private String type;
  
  public TreeNodeData(String text, ImageIcon icon, String type){
    this.text = text;
    this.icon = icon;
    this.type = type;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public ImageIcon getIcon() {
    return icon;
  }

  public void setIcon(ImageIcon icon) {
    this.icon = icon;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
  
  public String toString(){
    return text;
  }
}
