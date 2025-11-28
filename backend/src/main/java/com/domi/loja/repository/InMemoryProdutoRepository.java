package com.domi.loja.repository;

import com.domi.loja.domain.Produto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProdutoRepository implements ProdutoRepository {

    private final ConcurrentHashMap<String, Produto> banco = new ConcurrentHashMap<>();

    @Override
    public Produto salvar(Produto produto) {
        banco.put(produto.getId(), produto);
        return produto;
    }

    @Override
    public Optional<Produto> buscarPorId(String id) {
        return Optional.ofNullable(banco.get(id));
    }

    @Override
    public List<Produto> listarTodos() {
        return new ArrayList<>(banco.values());
    }

    @Override
    public void deletarPorId(String id) {
        banco.remove(id);
    }
}
