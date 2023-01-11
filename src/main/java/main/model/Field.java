package main.model;

import lombok.*;
import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "field")
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 255, nullable = true)
    private String name;

    @Column(name = "selector", length = 255, nullable = true)
    private String selector;

    @Column(name = "weight", columnDefinition = "FLOAT NOT NULL")
    private float weight;
}
