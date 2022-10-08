package main.repository;

import main.model.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Integer> {
//    SELECT page.id, count(page.id) AS count_id, sum(`index`.rank) AS sum_rank, sum(lemma.frequency) AS sum_frequency
//    FROM page JOIN lemma JOIN `index` ON lemma.id = `index`.lemma_id ON page.id = `index`.page_id
//    WHERE lemma.lemma In ("долгий","день","программирование")
//    GROUP BY page.id
//    HAVING count(page.id)=3
//    order by Sum_rank DESC;
//SELECT page.id, sum(index.rank) AS sum_rank FROM page JOIN lemma JOIN index ON lemma.id = index.lemma_id ON page.id = index.page_id WHERE lemma.lemma In (?1) GROUP BY page.id HAVING count(page.id)=?2 order by Sum_rank DESC

//    SELECT `index`.page_id AS `page`, sum(`index`.rank) AS sum_rank
//    FROM lemma JOIN `index` ON lemma.id = `index`.lemma_id
//    WHERE lemma.lemma In ("долгий", "день","программирование")
//    GROUP BY `index`.page_id
//    HAVING count(`index`.page_id)=3
//    order by Sum_rank DESC;

    @Query(value = "SELECT i.page_id AS id, sum(i.`rank`) AS sum_rank " +
            "FROM lemma l JOIN `index` i ON l.id = i.lemma_id " +
            "WHERE l.lemma In (:lemmaList) " +
            "GROUP BY i.page_id " +
            "HAVING count(i.page_id) = :countPage " +
            "order by Sum_rank DESC " +
            "limit :limit offset :offset",
            countQuery = "SELECT count(i.page_id) " +
            "FROM lemma l JOIN `index` i ON l.id = i.lemma_id " +
            "WHERE l.lemma In (:lemmaList) " +
            "GROUP BY i.page_id " +
            "HAVING count(i.page_id) = :countPage", nativeQuery = true)
    List<Search> searchPage(@Param("lemmaList") List<String> lemmaList,
                            @Param("countPage") int countLemma,
                            @Param("limit") int limit,
                            @Param("offset") int offset);

    @Query(value = "SELECT i.page_id AS id, sum(i.`rank`) AS sum_rank " +
            "FROM lemma l JOIN `index` i ON l.id = i.lemma_id " +
            "WHERE l.lemma In (:lemmaList) AND l.site_id = :site " +
            "GROUP BY i.page_id " +
            "HAVING count(i.page_id) = :countPage " +
            "order by Sum_rank DESC " +
            "limit :limit offset :offset",
            countQuery = "SELECT count(i.page_id) " +
                    "FROM lemma l JOIN `index` i ON l.id = i.lemma_id " +
                    "WHERE l.lemma In (:lemmaList) " +
                    "GROUP BY i.page_id " +
                    "HAVING count(i.page_id) = :countPage", nativeQuery = true)
    List<Search> searchPage(@Param("lemmaList") List<String> lemmaList,
                            @Param("countPage") int countLemma,
                            @Param("site") int siteId,
                            @Param("limit") int limit,
                            @Param("offset") int offset);

    @Query(value = "SELECT count(c.page_id) FROM (" +
                    "SELECT i.page_id " +
                    "FROM lemma l JOIN `index` i ON l.id = i.lemma_id " +
                    "WHERE l.lemma In (:lemmaList) " +
                    "GROUP BY i.page_id " +
                    "HAVING count(i.page_id) = :countPage) c", nativeQuery = true)
    int countSearchPage(@Param("lemmaList") List<String> lemmaList,
                            @Param("countPage") int countLemma);

    @Query(value = "SELECT count(c.page_id) FROM (" +
            "SELECT i.page_id " +
            "FROM lemma l JOIN `index` i ON l.id = i.lemma_id " +
            "WHERE l.lemma In (:lemmaList) AND l.site_id = :site " +
            "GROUP BY i.page_id " +
            "HAVING count(i.page_id) = :countPage) c", nativeQuery = true)
    int countSearchPage(@Param("lemmaList") List<String> lemmaList,
                        @Param("countPage") int countLemma, @Param("site") int siteId);
}
