package com.learning.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
//@Table(name="STUDENT", schema="SCHOOL") // pour set un autre nom que celui de l'entité pour le nom de la table
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //JPA choisit la stratégie appropriée selon la base de données utilisée.
    //@GeneratedValue(strategy = GenerationType.TABLE) //Utilise une table spéciale pour générer les identifiants uniques. C'est portable mais moins performant.
    //@GeneratedValue(strategy = GenerationType.SEQUENCE) //Utilise une séquence de la base de données (comme CREATE SEQUENCE) pour générer les identifiants. Performant si la base la supporte (ex: PostgreSQL, Oracle).
    //@GeneratedValue(strategy = GenerationType.IDENTITY) //Laisse la base gérer l’auto-incrémentation via une colonne IDENTITY. Typiquement pour MySQL, SQL Server. Génère l'id au moment de l'insertion.
    private Long id;

    //@Column(name="STUDENT_NAME", length=50, nullable=false, unique=false) // pour custom la colonne de la table
    private String name;

    @Transient // pour ne pas persister ce champ
    private Integer age;

    //@Temporal(TemporalType.DATE) // depuis JPA 3.1 le support existe pour les LocalDate, LocalTime, LocalDateTime
    //private Date birthDate;
    private LocalDate birthDate;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
