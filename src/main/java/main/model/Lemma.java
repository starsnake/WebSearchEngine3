package main.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;

//            statement.execute("CREATE TABLE lemmas(" +
//                    "id INT NOT NULL AUTO_INCREMENT, " +
//                    "lemma VARCHAR(255) NOT NULL, " +
//                    "frequency INT NOT NULL, " +
//                    "PRIMARY KEY(id))");

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "lemma")
public class Lemma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
    private Site site;

    @Column(name = "lemma",length = 255, nullable = false)
    private String lemma;

    @Column(name = "frequency", nullable = false)
    private int frequency;

    public Lemma(Site site, String lemma, int frequency) {
        this.site = site;
        this.lemma = lemma;
        this.frequency = frequency;
    }

}
