package main.model;

//        statement.execute("CREATE TABLE indexes(" +
//                "id INT NOT NULL AUTO_INCREMENT, " +
//                "page_id INT NOT NULL, " +
//                "lemma_id INT NOT NULL, " +
//                "`rank` FLOAT NOT NULL, " +
//                "PRIMARY KEY(id), " +
//                "INDEX FK_Lemma_idx (lemma_id ASC) VISIBLE, " +
//                "INDEX FK_page_idx (page_id ASC) VISIBLE, " +
//                "CONSTRAINT FK_Lemma FOREIGN KEY (lemma_id) " +
//                "REFERENCES lemma (id) " +
//                "ON DELETE NO ACTION ON UPDATE NO ACTION, " +
//                "CONSTRAINT FK_page FOREIGN KEY (page_id) " +
//                "REFERENCES page (id) " +
//                "ON DELETE NO ACTION ON UPDATE NO ACTION)");

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "`index`")
public class Index {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "page_id", referencedColumnName = "id", nullable = false)
    private Page page;

    @ManyToOne(fetch = FetchType.LAZY) //, cascade = CascadeType.REMOVE)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "lemma_id", referencedColumnName = "id", nullable = false)
    private Lemma lemma;

    @Column(name = "`rank`", columnDefinition = "FLOAT NOT NULL")
    private float rank;

    public Index(Page page, Lemma lemma, float rank) {
        this.page = page;
        this.lemma = lemma;
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Index index)) return false;
        return getPage().equals(index.getPage()) && getLemma().equals(index.getLemma());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPage(), getLemma());
    }
}
