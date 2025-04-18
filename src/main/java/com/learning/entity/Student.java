package com.learning.entity;

import jakarta.persistence.*;

@Entity
//@Table(name="STUDENT", schema="SCHOOL") // pour set un autre nom que celui de l'entité pour le nom de la table
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //JPA choisit la stratégie appropriée selon la base de données utilisée.
    //@GeneratedValue(strategy = GenerationType.TABLE) //Utilise une table spéciale pour générer les identifiants uniques. C'est portable mais moins performant.
    //@GeneratedValue(strategy = GenerationType.SEQUENCE) //Utilise une séquence de la base de données (comme CREATE SEQUENCE) pour générer les identifiants. Performant si la base la supporte (ex: PostgreSQL, Oracle).
    //@GeneratedValue(strategy = GenerationType.IDENTITY) //Laisse la base gérer l’auto-incrémentation via une colonne IDENTITY. Typiquement pour MySQL, SQL Server. Génère l'id au moment de l'insertion.
    private Long id;

    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
