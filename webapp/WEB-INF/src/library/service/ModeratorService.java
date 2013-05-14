package library.service;

import java.util.List;

import library.model.entity.Moderator;

public interface ModeratorService {
    public List<Moderator> findById();
    public Moderator save(Moderator moderator);
    public void delete(Moderator moderator);
}