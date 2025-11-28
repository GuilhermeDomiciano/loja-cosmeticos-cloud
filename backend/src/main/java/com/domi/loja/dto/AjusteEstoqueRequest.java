package com.domi.loja.dto;

import jakarta.validation.constraints.NotNull;

public class AjusteEstoqueRequest {

    @NotNull
    private Integer quantidade;

    private String motivo;

    public AjusteEstoqueRequest() {
    }

    // getters e setters

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
