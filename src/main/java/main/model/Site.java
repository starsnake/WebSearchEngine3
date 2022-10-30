package main.model;

import lombok.*;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

//        id INT NOT NULL AUTO_INCREMENT;
//        status ENUM('INDEXING', 'INDEXED', 'FAILED') NOT NULL — текущий статус полной индексации сайта, отражающий готовность поискового движка осуществлять поиск по сайту — индексация или переиндексация в процессе, сайт полностью проиндексирован (готов к поиску) или не удалось проиндексировать (сайт не готов к поиску и не будет до устранения ошибок и перезапуска индексации);
//        status_time DATETIME NOT NULL — дата и время статуса (в случае статуса INDEXING дата и время должны обновляться регулярно при добавлении каждой новой страницы в индекс);
//        last_error TEXT — текст ошибки индексации или NULL, если её не было;
//        url VARCHAR(255) NOT NULL — адрес главной страницы сайта;
//        name VARCHAR(255) NOT NULL — имя сайта.


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "site")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED')")
    private TypeSiteIndexingStatus status;

    @Column(name = "status_time", columnDefinition = "DATETIME NOT NULL")
    private Date statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "site", cascade  = CascadeType.REMOVE, orphanRemoval = true)
    private List<Page> indexPage;

    @OneToMany(mappedBy = "site", cascade  = CascadeType.REMOVE, orphanRemoval = true)
    private List<Lemma> indexLemma;

    public Site(String url, String name) {
        this.url = url;
        this.name = name;
    }
}
