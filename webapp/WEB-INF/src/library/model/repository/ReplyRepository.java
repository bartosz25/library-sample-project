package library.model.repository;

import library.model.entity.Reply;

import org.springframework.data.repository.CrudRepository;

public interface ReplyRepository  extends CrudRepository<Reply, Long>
{    
}