package main.repository;

import main.model.Index;
import main.model.Lemma;
import main.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LemmaRepository extends JpaRepository<Lemma, Integer> {
    Optional<Lemma> findBySiteAndLemma(Site site, String nameLemma);
    long countBySite(Site site);

    List<Lemma> findBySiteAndLemmaIn(Site site, List<String> lemmas);

//    List<Lemma> findByIndexIn(Site site, List<Index> indexies);

    List<Lemma> findBySiteAndLemmaInOrderByFrequency(Site site, List<String> lemmas);

    List<Lemma> findByLemmaInOrderByFrequency(List<String> lemmas);

}
