package library.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.model.entity.Lang;
import library.model.entity.PageContent;
import library.model.entity.Placement;
import library.model.repository.LangRepository;
import library.model.repository.PageContentRepository;
import library.service.PageContentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pageContentService")
@Repository
@Transactional
public class PageContentServiceImpl implements PageContentService {
    final Logger logger = LoggerFactory.getLogger(PageContentServiceImpl.class);
    @Autowired
    private PageContentRepository pageContentRepository;
    @Autowired
    private LangRepository langRepository;
    
    @Override
    @Transactional(readOnly=true)
    public List<PageContent> findByPlacement(Long placementId) {
        return pageContentRepository.findByPlacement(placementId);
    }
    
    @Override
    @Transactional(readOnly=true)
    public Map<String, List<PageContent>> findByLang(Long langId) {
        List<PageContent> pages = pageContentRepository.findByLang(langId);
        Map<String, List<PageContent>> pagesMap = new HashMap<String, List<PageContent>>();
        for (PageContent page : pages) {
            Placement placement = page.getPlacement();
            if (placement.getName() != null) {
                List<PageContent> pageList = null;
                if (pagesMap.containsKey(placement.getName())) {
                    pageList = (ArrayList) pagesMap.get(placement.getName());
                } else {
                    pageList = new ArrayList<PageContent>();
                }
                pageList.add(page);
                pagesMap.put(placement.getName(), pageList);
            }
        }
        return pagesMap;
    }

    @Override
    @Transactional(readOnly=true)
    public PageContent findById(Long id) {
        logger.info("============> Getting page by id "+  id);
        return pageContentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly=true)
    public List<PageContent> findByLangAndPlacement(Long langId, Long placementId) {
        return pageContentRepository.findByLangAndPlacement(langId, placementId);
    }

    @Override
    @Transactional(readOnly=true)
    public PageContent findByUrl(String url) {
        return pageContentRepository.findByUrl(url);
    }
    
    @Override
    public Map<String, Map<String, List<PageContent>>> getAndGroupPagesByLang() {
        Map<String, Map<String, List<PageContent>>> result = new HashMap<String, Map<String, List<PageContent>>>();
        for (Lang lang : langRepository.findAll()) {
            result.put(lang.getIso(), findByLang(lang.getId()));
        }
        logger.info("Found page content by lang, grouped by placement : " + result);
        return result;
    }
    
    @Override
    public PageContent save(PageContent page) {
        return null;
    }
    
    
    
    
    @Override
    public void delete(PageContent page)
    {
    }
}