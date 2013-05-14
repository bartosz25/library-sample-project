package library.model.repository;

import library.model.entity.BookWriter;
import library.model.entity.BookWriterPK;

import org.springframework.data.repository.CrudRepository;

public interface BookWriterRepository  extends CrudRepository<BookWriter, BookWriterPK>
{
}