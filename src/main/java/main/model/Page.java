package main.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "page", indexes = @javax.persistence.Index(columnList = "path", name = "idx_page_path"))
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "site_id", foreignKey = @ForeignKey(name = "fk_page_site_id"), referencedColumnName = "id", nullable = false)
    private Site site;

    @Column(name = "path", length = 500, nullable = false)
    private String path;

    @Column(name = "code", nullable = false)
    private int code;

    @Column(name = "content", columnDefinition = "TEXT NOT NULL")
    private String content;

    public Page(Site site, String path) {
        this.site = site;
        this.path = path;
        this.code = 0;
        this.content = "";
    }
}
