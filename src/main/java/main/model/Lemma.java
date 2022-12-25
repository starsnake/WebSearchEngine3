package main.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

//            statement.execute("CREATE TABLE lemmas(" +
//                    "id INT NOT NULL AUTO_INCREMENT, " +
//                    "lemma VARCHAR(255) NOT NULL, " +
//                    "frequency INT NOT NULL, " +
//                    "PRIMARY KEY(id))");

@NoArgsConstructor
@Data
@Entity
@Table(name = "lemma", indexes = @javax.persistence.Index(columnList = "lemma", name = "idx_Lemma_lemma"))
public class Lemma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "site_id", foreignKey = @ForeignKey(name = "fk_lemma_site_id"), nullable = false, referencedColumnName = "id") //, nullable = false)
    private Site site;

    @Column(name = "lemma", nullable = false)
    private String lemma;

    @Column(name = "frequency", nullable = false)
    private int frequency;

//    @OneToMany(mappedBy = "lemma", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private List<Index> indexList;

    public Lemma(Site site, String lemma, int frequency) {
        this.site = site;
        this.lemma = lemma;
        this.frequency = frequency;
    }
}
