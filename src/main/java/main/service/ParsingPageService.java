package main.service;

import main.config.SiteConfig;
import main.model.*;
import main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ParsingPageService implements IParsingPageService{
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private LemmaRepository lemmaRepository;
    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private SiteConfig siteConfig;

    @Override
    public long countSites() {
        return siteRepository.count();
    }

    @Override
    public long countPage() {
        return pageRepository.count();
    }

    @Override
    public long countLemma() {
        return lemmaRepository.count();
    }

    @Override
    public long countPageBySite(Site site) {
        return pageRepository.countBySite(site);
    }

    @Override
    public long countLemmaBySite(Site site) {
        return lemmaRepository.countBySite(site);
    }

    @Override
    public HashMap<String, Float> getAllFields() {
        HashMap<String, Float> list = new HashMap<>();
        fieldRepository.findAll().forEach(p->list.put(p.getName(), p.getWeight()));
        return list;
    }

    @Override
    @Transactional
    public void saveParsing(Page page, Site site, HashMap<String, Float> ranks){
        List<Index> indexList = new ArrayList<>();
        HashMap<String, Lemma> lemmaMap = new HashMap<>();
        List<String> list = ranks.keySet().stream().toList();
        for (String key : list) {
            lemmaMap.put(key, new Lemma(site, key, 1));
        }
        List<Lemma> findLemma = saveLemmas(site, list, lemmaMap);
        lemmaRepository.flush();
        for(Lemma lemma : findLemma){
            Index index = new Index(page, lemma, ranks.get(lemma.getLemma()));
            indexList.add(index);
        }
        indexRepository.saveAll(indexList);
    }

    private synchronized List<Lemma> saveLemmas(Site site, List<String> list, HashMap<String, Lemma> lemmaMap){
        List<Lemma> findLemma = lemmaRepository.findBySiteAndLemmaIn(site, list);
        for (Lemma lemma : findLemma) {
            if (lemmaMap.containsKey(lemma.getLemma())) {
                lemma.setFrequency(lemma.getFrequency() + 1);
                lemmaMap.replace(lemma.getLemma(), lemma);
            }
        }
//        return lemmaMap.values().stream().toList();
        return lemmaRepository.saveAll(lemmaMap.values());
    }

    @Override
    public boolean isExistingPage(Site site, String url){
        Optional<Page> pageOptional = pageRepository.findPageByPathAndSite(url, site);
        return pageOptional.isPresent();
    }

    @Override
    public boolean isSiteFailed(Site site) {
        return siteRepository.findByUrl(site.getUrl())
                .getStatus()
                .equals(TypeSiteIndexingStatus.FAILED);
    }

    @Override
    public List<Site> getAllSites() {
        return new ArrayList<>(siteRepository.findAll());
    }

    @Override
    public void deleteSite(Site site) {
//        pageRepository.deleteBySite(site);
        siteRepository.delete(site);
    }
    @Override
    public void createSitesFromConfig() {
        for(Site site : siteConfig.getSites()) {
            String url = site.getUrl().replaceFirst("www.", "");
            site.setUrl(url);
            site.setStatus(TypeSiteIndexingStatus.INDEXING);
            site.setStatusTime(new Date());
            siteRepository.save(site);
        }
    }

    @Override
    public Site saveSite(Site site) {
        site.setStatusTime(new Date());
        return siteRepository.save(site);
    }

    @Override
    public void setTypeIndexing(Site site, TypeSiteIndexingStatus typeStatus) {
        site.setStatus(typeStatus);
        site.setStatusTime(new Date());
        siteRepository.save(site);
    }
    @Override
    public boolean isIndexing() {
        List<Site> list = siteRepository.findByStatus(TypeSiteIndexingStatus.INDEXING);
        return !list.isEmpty();
    }

    @Override
    public synchronized Page savePage(Page page){
        if(!isExistingPage(page.getSite(), page.getPath())){
            return pageRepository.save(page);
        }
        return null;
    }

    @Override
    public Site getSite(String url) {
        return siteRepository.findByUrl(url);
    }

    @Override
    public void saveErrorParsing(Site site, String msg) {
        site.setLastError(msg);
        site.setStatusTime(new Date());
        siteRepository.save(site);
    }

    @Override
    public List<Lemma> getLemma(String siteUrl, List<String> lemmas) {
        if(siteUrl.equals("")){
            return lemmaRepository.findByLemmaInOrderByFrequency(lemmas);
        }
        else {
            Site site = siteRepository.findByUrl(siteUrl);
            return lemmaRepository.findBySiteAndLemmaInOrderByFrequency(site, lemmas);
        }
    }

//    SELECT page.id, count(page.id) AS count_id, sum(`index`.rank) AS sum_rank, sum(lemma.frequency) AS sum_frequency
//    FROM page JOIN lemma JOIN `index` ON lemma.id = `index`.lemma_id ON page.id = `index`.page_id
//    WHERE lemma.lemma In ("долгий","день","программирование")
//    GROUP BY page.id
//    HAVING count(page.id)=3
//    order by Sum_rank DESC;

    @Override
    public List<Index> getIndex(List<Lemma> lemmaList) {
        List<Index> indexList = new ArrayList<>();
        for (Lemma lemma : lemmaList) {
            if(indexList.isEmpty()) {
                indexList = indexRepository.findByLemma(lemma);
            }
            else {
                List<Integer> list = new ArrayList<>();
                for(Index index : indexList){
                    list.add(index.getId());
                }
                //list = indexList.stream().forEach(p->{return p.getId();});
                indexList = indexRepository.findByIdInAndLemma(list, lemma);
            }
        }
        return indexList;
    }

    @Override
    public List<Page> searchPage(List<String> lemmaList, int countLemma, int limit, int offset) {
        return pageRepository.searchPage(lemmaList, countLemma, limit, offset);
    }

    @Override
    public Page getPageById(int id) {
        Optional<Page> pageOptional = pageRepository.findById(id);
        if(pageOptional.isEmpty()){
            return new Page();
        }
        return pageOptional.get();
    }

    @Override
    public Optional<Site> getSiteById(int id) {
        return siteRepository.findById(id);
    }

    @Override
    public int countSearchPage(List<String> lemmaList, int countLemma, String siteUrl) {
        if(siteUrl.isEmpty()){
            return searchRepository.countSearchPage(lemmaList, countLemma);
        }
        Site site = siteRepository.findByUrl(siteUrl);
        return searchRepository.countSearchPage(lemmaList, countLemma, site.getId());
    }

    @Override
    public List<Search> searchPage(List<String> lemmaList, int countLemma, String siteUrl, int limit, int offset) {
        if(siteUrl.isEmpty()){
            return searchRepository.searchPage(lemmaList, countLemma, limit, offset);
        }
        Site site = siteRepository.findByUrl(siteUrl);
        return searchRepository.searchPage(lemmaList, countLemma, site.getId(), limit, offset);
    }
}
