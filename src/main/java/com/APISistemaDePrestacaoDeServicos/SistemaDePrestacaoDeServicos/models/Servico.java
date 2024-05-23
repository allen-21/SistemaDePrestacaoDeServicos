package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.EstadoPedido;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @OneToMany(mappedBy = "servico")
    @JsonManagedReference
    private List<Pedido> pedidos;

    public Servico(Profissional profissional, String descricaoDoServico) {
        this.profissional = profissional;
        this.descricaoDoServico = descricaoDoServico;
    }

    public Object getProfissional() {
        return profissional;
    }


}
