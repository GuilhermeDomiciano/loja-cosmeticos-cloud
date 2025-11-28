package com.domi.loja.controller;

import com.domi.loja.dto.AjusteEstoqueRequest;
import com.domi.loja.dto.ProdutoRequest;
import com.domi.loja.dto.ProdutoResponse;
import com.domi.loja.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(@Valid @RequestBody ProdutoRequest request) {
        return service.criarProduto(request);
    }

    @GetMapping
    public List<ProdutoResponse> listar(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean ativo
    ) {
        return service.listarProdutos(categoria, ativo);
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable String id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(
            @PathVariable String id,
            @Valid @RequestBody ProdutoRequest request
    ) {
        return service.atualizarProduto(id, request);
    }

    @PatchMapping("/{id}/estoque")
    public ProdutoResponse ajustarEstoque(
            @PathVariable String id,
            @Valid @RequestBody AjusteEstoqueRequest request
    ) {
        return service.ajustarEstoque(id, request);
    }

    @PatchMapping("/{id}/desativar")
    public ProdutoResponse desativar(@PathVariable String id) {
        return service.desativarProduto(id);
    }
}
