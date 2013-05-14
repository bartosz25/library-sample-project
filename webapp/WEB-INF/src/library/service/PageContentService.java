package library.service;

import java.util.List;
import java.util.Map;

import library.model.entity.PageContent;

public interface PageContentService {
    public PageContent findByUrl(String url);
    public PageContent findById(Long id);
    public Map<String, List<PageContent>> findByLang(Long langId);
    public List<PageContent> findByPlacement(Long placementId);
    public List<PageContent> findByLangAndPlacement(Long langId, Long placementId);
    public PageContent save(PageContent page);
    public Map<String, Map<String, List<PageContent>>> getAndGroupPagesByLang();
    public void delete(PageContent page);
}