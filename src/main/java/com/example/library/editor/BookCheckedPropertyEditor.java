package com.example.library.editor;


import java.util.ArrayList;
import java.util.List;

import com.example.library.model.entity.Category;
import com.example.library.model.entity.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;

public class BookCheckedPropertyEditor<T> extends CustomCollectionEditor {
    final Logger logger = LoggerFactory.getLogger(BookCheckedPropertyEditor.class); 
    public static final String TYPE_CATEGORIES = "CATEGORIES";
    public static final String TYPE_WRITERS = "WRITERS";
    private String type;
    
    public BookCheckedPropertyEditor(Class collectionType, String type) {
        super(collectionType);
        this.type = type;
    }

    // Converts a String to a Category or Writer (when submitting form)
    @Override
    public void setAsText(String text) {
        logger.info("BookCheckedPropertyEditor Received text " + text);
        String[] ids = text.split(",");
        List<Object> result = new ArrayList<Object>();
        for(String id : ids) {
            try {
                logger.info("BookCheckedPropertyEditor Converting id to "+type+" : " + id);
                long idLong = Long.parseLong(id.trim());
                if(type.equals(TYPE_CATEGORIES)) {
                    Category category = new Category();
                    category.setId(idLong);
                    ((List) result).add(category);
                } else if(type.equals(TYPE_WRITERS)) {
                    Writer writer = new Writer();
                    writer.setId(idLong);
                    ((List) result).add(writer);
                }
            } catch (NumberFormatException e) {
                logger.error("Error on formatting String ("+id+") to Long", e);
            }
        }
        setValue(result);
    }

    // @Override
    // protected Object convertElement(Object element)
    // {
        // Category category = new Category();
        // category.setId((Long)element);
        // return category;
    // }
}