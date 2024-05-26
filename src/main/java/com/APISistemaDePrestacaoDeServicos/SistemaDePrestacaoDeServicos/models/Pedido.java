package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.EstadoPedido;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;


import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity


@Getter
@Setter
@NoArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "servico_id")
    @JsonBackReference
    private Servico servico;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private EstadoPedido status;

    @OneToMany(mappedBy = "pedido")
    @JsonManagedReference
    private List<Avaliacao> avaliacoes;

    public Profissional getProfissional() {
        return (Profissional) this.servico.getProfissional();
    }

    public Pedido(Servico servico, Cliente cliente, String descricao, EstadoPedido status) {
        this.servico = servico;
        this.cliente = cliente;
        this.descricao = descricao;
        this.status = EstadoPedido.PENDENTE;
    }


}
