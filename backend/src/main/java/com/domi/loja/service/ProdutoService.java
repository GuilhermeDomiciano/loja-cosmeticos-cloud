package com.domi.loja.service;

import com.domi.loja.domain.Produto;
import com.domi.loja.dto.AjusteEstoqueRequest;
import com.domi.loja.dto.ProdutoRequest;
import com.domi.loja.dto.ProdutoResponse;
import com.domi.loja.exception.ProdutoNaoEncontradoException;
import com.domi.loja.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public ProdutoResponse criarProduto(ProdutoRequest request) {
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID().toString());
        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setCategoria(request.getCategoria());
        produto.setMarca(request.getMarca());
        produto.setPreco(request.getPreco());
        produto.setQuantidadeEstoque(
                request.getQuantidadeEstoque() != null ? request.getQuantidadeEstoque() : 0
        );
        produto.setAtivo(true);
        produto.setCriadoEm(Instant.now());
        produto.setAtualizadoEm(Instant.now());

        repository.salvar(produto);
        return toResponse(produto);
    }

    public List<ProdutoResponse> listarProdutos(String categoria, Boolean ativo) {
        return repository.listarTodos().stream()
                .filter(p -> categoria == null || categoria.equalsIgnoreCase(p.getCategoria()))
                .filter(p -> ativo == null || ativo.equals(p.getAtivo()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProdutoResponse buscarPorId(String id) {
        Produto produto = repository.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        return toResponse(produto);
    }

    public ProdutoResponse atualizarProduto(String id, ProdutoRequest request) {
        Produto produto = repository.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setCategoria(request.getCategoria());
        produto.setMarca(request.getMarca());
        produto.setPreco(request.getPreco());
        produto.setQuantidadeEstoque(
                request.getQuantidadeEstoque() != null ? request.getQuantidadeEstoque() : produto.getQuantidadeEstoque()
        );
        produto.setAtualizadoEm(Instant.now());

        repository.salvar(produto);
        return toResponse(produto);
    }

    public ProdutoResponse ajustarEstoque(String id, AjusteEstoqueRequest request) {
        Produto produto = repository.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        int atual = produto.getQuantidadeEstoque() != null ? produto.getQuantidadeEstoque() : 0;
        int novoEstoque = atual + request.getQuantidade();

        if (novoEstoque < 0) {
            throw new IllegalArgumentException("Estoque não pode ficar negativo");
        }

        produto.setQuantidadeEstoque(novoEstoque);
        produto.setAtualizadoEm(Instant.now());

        repository.salvar(produto);
        return toResponse(produto);
    }

    public ProdutoResponse desativarProduto(String id) {
        Produto produto = repository.buscarPorId(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produto.setAtivo(false);
        produto.setAtualizadoEm(Instant.now());

        repository.salvar(produto);
        return toResponse(produto);
    }

    private ProdutoResponse toResponse(Produto produto) {
        ProdutoResponse response = new ProdutoResponse();
        response.setId(produto.getId());
        response.setNome(produto.getNome());
        response.setDescricao(produto.getDescricao());
        response.setCategoria(produto.getCategoria());
        response.setMarca(produto.getMarca());
        response.setPreco(produto.getPreco());
        response.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        response.setAtivo(produto.getAtivo());
        response.setCriadoEm(produto.getCriadoEm());
        response.setAtualizadoEm(produto.getAtualizadoEm());
        return response;
    }
}
