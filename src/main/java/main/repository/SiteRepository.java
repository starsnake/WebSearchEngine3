package main.repository;

import main.model.Page;
import main.model.Site;
import main.model.TypeSiteIndexingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {
    Site findByUrl(String url);
    List<Site> findByStatus(TypeSiteIndexingStatus status);
}
