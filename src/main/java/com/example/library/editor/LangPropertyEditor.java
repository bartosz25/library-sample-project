package com.example.library.editor;

import java.beans.PropertyEditorSupport;

import com.example.library.model.entity.Lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LangPropertyEditor extends PropertyEditorSupport
{
    final Logger logger = LoggerFactory.getLogger(LangPropertyEditor.class);
    
    public void setAsText(String text) {
        logger.info("==============> Received text " + text);
        setValue(new Lang());
    }
    
    protected Object convertElement(Object element) {
        logger.info("=============> converting " + element);
        return new Lang();
    }
}