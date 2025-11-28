package com.domi.loja.repository;

import com.domi.loja.domain.Produto;
import com.domi.loja.entity.ProdutoEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary
public class JpaProdutoRepository implements ProdutoRepository {

    private final ProdutoJpaRepository jpaRepository;

    public JpaProdutoRepository(ProdutoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Produto salvar(Produto produto) {
        ProdutoEntity entity = toEntity(produto);
        ProdutoEntity salvo = jpaRepository.save(entity);
        return toDomain(salvo);
    }

    @Override
    public Optional<Produto> buscarPorId(String id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Produto> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deletarPorId(String id) {
        jpaRepository.deleteById(id);
    }

    // ====== mapeamento ======

    private ProdutoEntity toEntity(Produto p) {
        ProdutoEntity e = new ProdutoEntity();
        e.setId(p.getId());
        e.setNome(p.getNome());
        e.setDescricao(p.getDescricao());
        e.setCategoria(p.getCategoria());
        e.setMarca(p.getMarca());
        e.setPreco(p.getPreco());
        e.setQuantidadeEstoque(p.getQuantidadeEstoque());
        e.setAtivo(p.getAtivo());
        e.setCriadoEm(p.getCriadoEm());
        e.setAtualizadoEm(p.getAtualizadoEm());
        return e;
    }

    private Produto toDomain(ProdutoEntity e) {
        Produto p = new Produto();
        p.setId(e.getId());
        p.setNome(e.getNome());
        p.setDescricao(e.getDescricao());
        p.setCategoria(e.getCategoria());
        p.setMarca(e.getMarca());
        p.setPreco(e.getPreco());
        p.setQuantidadeEstoque(e.getQuantidadeEstoque());
        p.setAtivo(e.getAtivo());
        p.setCriadoEm(e.getCriadoEm());
        p.setAtualizadoEm(e.getAtualizadoEm());
        return p;
    }
}

