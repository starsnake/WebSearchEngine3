package main.model;

//statement.execute("CREATE TABLE pages(" +
//        "id INT NOT NULL AUTO_INCREMENT, " +
//        "path TEXT NOT NULL, " +
//        "code INT NOT NULL, " +
//        "content MEDIUMTEXT NOT NULL, " +
//        "PRIMARY KEY(id), " +
//        "UNIQUE KEY name_date(path(100)))");

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "page", indexes = @javax.persistence.Index(columnList = "path", name = "idx_page_path"))
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER) //, cascade = CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "site_id", foreignKey = @ForeignKey(name = "fk_page_site_id"), referencedColumnName = "id", nullable = false)
    private Site site;

//    @Column(name = "path", columnDefinition = "TEXT CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NOT NULL")
    @Column(name = "path", length = 500, nullable = false)
    private String path;

    @Column(name = "code", nullable = false)
    private int code;

//    @Column(name = "content", columnDefinition = "MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NOT NULL")
    @Column(name = "content", columnDefinition = "TEXT NOT NULL")
    private String content;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(name = "`index`",
//            joinColumns = {@JoinColumn(name = "page_id")},
//            inverseJoinColumns = {@JoinColumn(name = "lemma_id")})
//    private List<Lemma> lemmaList;


//    @OneToMany(mappedBy = "page", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private List<Index> indexList;

    public Page(Site site, String path) {
        this.site = site;
        this.path = path;
        this.code = 0;
        this.content = "";
    }
}
