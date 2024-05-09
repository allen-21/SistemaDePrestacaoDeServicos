package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums;

public enum Profissoes {

    LIMPEZA("Limpeza"),
    JARDINAGEM("Jardinagem"),
    MUDANCA("Mudança"),
    MANUTENCAO("Manutenção"),
    ENTREGAS("Entregas"),
    SEGURANCA_RESIDENCIAL("Segurança Residencial"),
    CANALIZACAO("Canalização"),
    INSTALACOES_ELETRICAS("Instalações Elétricas"),
    MONTAGEM_MOVEIS("Montagem de Móveis");

    private String descricao;

    Profissoes(String descricao) {
        this.descricao = descricao;
    }
}
