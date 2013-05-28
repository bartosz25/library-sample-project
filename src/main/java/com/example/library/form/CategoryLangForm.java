package com.example.library.form;

import java.util.ArrayList;
import java.util.List;

import com.example.library.model.entity.CategoryLang;
import com.example.library.model.entity.ParentEntity;

public class CategoryLangForm extends ParentEntity  {
    private List<CategoryLang> categoryLang = new ArrayList<CategoryLang>();

    public List<CategoryLang> getCategoryLang() {
        return categoryLang;
    }

    public void setCategoryLang(List<CategoryLang> categoryLang) {
        this.categoryLang = categoryLang;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (CategoryLang categoryLangInst : categoryLang) {
            builder.append(categoryLangInst.toString());
        }    
        return builder.toString();
    }
}