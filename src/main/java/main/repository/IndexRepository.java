package main.repository;

import main.model.Index;
import main.model.Lemma;
import main.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexRepository extends JpaRepository<Index, Integer> {
    List<Index> findByPageAndLemma(Page page, Lemma lemma);
    List<Index> findByLemma(Lemma lemma);
    List<Index> findByIdInAndLemma(List<Integer> list, Lemma lemma);
}
