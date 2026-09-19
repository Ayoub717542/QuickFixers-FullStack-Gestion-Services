package com.example.QuickFixersBackend.dto.paiement;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor

public class IncomeByDay {
    private LocalDate jour;
    private Double total;
}
