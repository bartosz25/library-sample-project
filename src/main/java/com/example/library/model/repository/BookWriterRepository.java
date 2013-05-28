package com.example.library.model.repository;

import com.example.library.model.entity.BookWriter;
import com.example.library.model.entity.BookWriterPK;

import org.springframework.data.repository.CrudRepository;

public interface BookWriterRepository  extends CrudRepository<BookWriter, BookWriterPK>
{
}