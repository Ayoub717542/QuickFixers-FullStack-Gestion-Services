package com.example.QuickFixersBackend.entity;

import com.example.QuickFixersBackend.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("SUPPORT")
public class Support extends Person {

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    public Support(String nom, String prenom, String email, String password, ServiceType serviceType) {
        super(nom, prenom, email, password);
        this.serviceType = serviceType;
    }
}