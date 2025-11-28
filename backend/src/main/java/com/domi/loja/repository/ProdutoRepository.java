package com.domi.loja.repository;

import com.domi.loja.domain.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {

    Produto salvar(Produto produto);

    Optional<Produto> buscarPorId(String id);

    List<Produto> listarTodos();

    void deletarPorId(String id);
}
