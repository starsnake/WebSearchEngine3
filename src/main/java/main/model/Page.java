package main.model;

//statement.execute("CREATE TABLE pages(" +
//        "id INT NOT NULL AUTO_INCREMENT, " +
//        "path TEXT NOT NULL, " +
//        "code INT NOT NULL, " +
//        "content MEDIUMTEXT NOT NULL, " +
//        "PRIMARY KEY(id), " +
//        "UNIQUE KEY name_date(path(100)))");

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "page")
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", referencedColumnName = "id",nullable = false)
    private Site site;

    @Column(name = "path", columnDefinition = "TEXT NOT NULL")
    private String path;

    @Column(name = "code", nullable = false)
    private int code;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT NOT NULL")
    private String content;

    @Column(name = "sum_rank")
    transient private float sumRank;

    public Page(Site site, String path) {
        this.site = site;
        this.path = path;
    }
}