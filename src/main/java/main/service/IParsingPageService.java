package main.service;

import main.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface IParsingPageService {
    long countSites();
    long countPage();
    long countLemma();
    long countPageBySite(Site site);
    long countLemmaBySite(Site site);

    HashMap<String, Float> getAllFields();
    boolean isExistingPage(Site site, String url);
    boolean isIndexing();
    boolean isSiteFailed(Site site);
    List<Site> getAllSites();
    Site getSite(String url);
    void saveParsing(Page page, Site site, HashMap<String, Float> ranks);
    void deleteSite(Site site);
    void deleteAllSites();
    Site saveSite(Site site);
    void createSitesFromConfig();
    void setTypeIndexing(Site site, TypeSiteIndexingStatus typeStatus);
    Page savePage(Page page);
    void saveErrorParsing(Site site, String msg);

    List<Lemma> getLemma(String siteUrl, List<String> lemmas);
    List<Index> getIndex(List<Lemma> lemmaList);
    List<Page> searchPage(List<String> lemmaList,
                          int countLemma,
                          int limit,
                          int offset);
    Page getPageById(int id);
    Optional<Site> getSiteById(int id);

    int countSearchPage(List<String> lemmaList, int countLemma, String siteUrl);

    List<Search> searchPage(List<String> lemmaList,
                            int countLemma,
                            String siteUrl,
                            int limit,
                            int offset);
    }
