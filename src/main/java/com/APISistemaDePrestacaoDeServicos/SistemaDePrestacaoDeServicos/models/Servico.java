package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    private String descricaoDoServico;

    public Servico(Profissional profissional, String descricaoDoServico) {
        this.profissional = profissional;
        this.descricaoDoServico = descricaoDoServico;
    }



    public Object getProfissional() {
        return profissional;
    }


}
