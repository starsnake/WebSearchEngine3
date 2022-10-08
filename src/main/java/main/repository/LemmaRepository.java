package main.repository;

import main.model.Lemma;
import main.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LemmaRepository extends JpaRepository<Lemma, Integer> {
    Lemma findBySiteAndLemma(Site site, String nameLemma);
    long countBySite(Site site);

    List<Lemma> findBySiteAndLemmaIn(Site site, List<String> lemmas);

    List<Lemma> findBySiteAndLemmaInOrderByFrequency(Site site, List<String> lemmas);
    List<Lemma> findByLemmaInOrderByFrequency(List<String> lemmas);
}
