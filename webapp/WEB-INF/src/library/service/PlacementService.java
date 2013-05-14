package library.service;

import java.util.List;

import library.model.entity.Placement;

public interface PlacementService {
    public List<Placement> findById();
    public Placement save(Placement placement);
    public void delete(Placement placement);
}