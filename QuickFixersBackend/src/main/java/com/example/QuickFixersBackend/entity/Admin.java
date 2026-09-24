package com.example.QuickFixersBackend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("ADMIN")
public class Admin extends Person {

    public Admin(String nom, String prenom, String email, String password) {
        super(nom, prenom, email, password);
    }
}