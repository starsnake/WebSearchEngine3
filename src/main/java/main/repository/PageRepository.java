package main.repository;

import main.model.Page;
import main.model.Search;
import main.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    Page findPageByPathAndSite(String url, Site site);
    long countBySite(Site site);
    void deleteBySite(Site site);

    //    SELECT page.id, count(page.id) AS count_id, sum(`index`.rank) AS sum_rank, sum(lemma.frequency) AS sum_frequency
//    FROM page JOIN lemma JOIN `index` ON lemma.id = `index`.lemma_id ON page.id = `index`.page_id
//    WHERE lemma.lemma In ("долгий","день","программирование")
//    GROUP BY page.id
//    HAVING count(page.id)=3
//    order by Sum_rank DESC;
//SELECT page.id, sum(index.rank) AS sum_rank FROM page JOIN lemma JOIN index ON lemma.id = index.lemma_id ON page.id = index.page_id WHERE lemma.lemma In (?1) GROUP BY page.id HAVING count(page.id)=?2 order by Sum_rank DESC
    @Query(value = "SELECT p.id, sum(i.rank) AS sum_rank " +
            "FROM page p JOIN lemma l JOIN index i ON l.id = i.lemma_id ON p.id = i.page_id " +
            "WHERE l.lemma In (:lemmaList) " +
            "GROUP BY p.id " +
            "HAVING count(p.id)= :countPage) " +
            "order by Sum_rank DESC " +
            "limit :limit offset :offset", nativeQuery = true)
    List<Page> searchPage(@Param("lemmaList") List<String> lemmaList,
                            @Param("countPage") int countLemma,
                            @Param("limit") int limit,
                            @Param("offset") int offset);

}
