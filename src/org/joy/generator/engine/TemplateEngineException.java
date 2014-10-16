package org.joy.generator.engine;


public class TemplateEngineException extends Exception {

    private static final long serialVersionUID = -5111880556567727699L;

    public TemplateEngineException(){
    }

    public TemplateEngineException(String message){
        super(message);
    }

    public TemplateEngineException(String message, Throwable cause){
        super(message, cause);
    }
}
