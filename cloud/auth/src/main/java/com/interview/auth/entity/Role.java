package com.cloud.auth.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,unique = true)
    private Long id;
    @Column(nullable = false)
    private String name;

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}