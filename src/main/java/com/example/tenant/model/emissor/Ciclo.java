package com.example.tenant.model.emissor;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_CICLO")
@Data
public class Ciclo {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "IDCICLO")
    private Long id;

    @Column(name="DIACORTE")
    private int diaCorte;

    @Column(name="DIAVENCIMENTO")
    private int diaVencimento;

    @Column(name="DESCRICAO")
    private String descricao;

    @Column(name="ATIVO")
    private String ativo;

}
