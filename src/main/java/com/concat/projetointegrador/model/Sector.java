package com.concat.projetointegrador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@AllArgsConstructor
@Entity
@Table(name = "sector")
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Warehouse warehouse;

    @Column(nullable = false)
    private Integer capacity;

    @OneToOne
    private Supervisor supervisor;

    @Enumerated(EnumType.STRING)
    private Category category;

    public Sector() {

    }
}
