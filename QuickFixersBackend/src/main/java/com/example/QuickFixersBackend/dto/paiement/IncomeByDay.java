package com.example.QuickFixersBackend.dto.paiement;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class IncomeByDay implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate jour;
    private Double total;
}
