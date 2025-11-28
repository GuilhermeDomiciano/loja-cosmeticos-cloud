public class DynamoDbProdutoRepository {
    
}
package com.domi.loja.repository;

import com.domi.loja.domain.Produto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Primary
public class DynamoDbProdutoRepository implements ProdutoRepository {

    private static final String TABLE_NAME = "produtos";

    private final DynamoDbClient dynamoDb;

    public DynamoDbProdutoRepository(DynamoDbClient dynamoDb) {
        this.dynamoDb = dynamoDb;
    }

    @Override
    public Produto salvar(Produto produto) {
        Map<String, AttributeValue> item = toItem(produto);

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDb.putItem(request);
        return produto;
    }

    @Override
    public Optional<Produto> buscarPorId(String id) {
        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.builder().s(id).build()
        );

        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        var response = dynamoDb.getItem(request);

        if (!response.hasItem() || response.item().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(fromItem(response.item()));
    }

    @Override
    public List<Produto> listarTodos() {
        ScanRequest request = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build();

        ScanResponse response = dynamoDb.scan(request);

        if (!response.hasItems()) {
            return Collections.emptyList();
        }

        return response.items().stream()
                .map(this::fromItem)
                .collect(Collectors.toList());
    }

    @Override
    public void deletarPorId(String id) {
        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.builder().s(id).build()
        );

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        dynamoDb.deleteItem(request);
    }

    // ======= Helpers de mapeamento =======

    private Map<String, AttributeValue> toItem(Produto p) {
        Map<String, AttributeValue> item = new HashMap<>();

        item.put("id", AttributeValue.builder().s(p.getId()).build());
        if (p.getNome() != null) {
            item.put("nome", AttributeValue.builder().s(p.getNome()).build());
        }
        if (p.getDescricao() != null) {
            item.put("descricao", AttributeValue.builder().s(p.getDescricao()).build());
        }
        if (p.getCategoria() != null) {
            item.put("categoria", AttributeValue.builder().s(p.getCategoria()).build());
        }
        if (p.getMarca() != null) {
            item.put("marca", AttributeValue.builder().s(p.getMarca()).build());
        }
        if (p.getPreco() != null) {
            item.put("preco", AttributeValue.builder().n(p.getPreco().toPlainString()).build());
        }
        if (p.getQuantidadeEstoque() != null) {
            item.put("quantidadeEstoque", AttributeValue.builder().n(p.getQuantidadeEstoque().toString()).build());
        }
        if (p.getAtivo() != null) {
            item.put("ativo", AttributeValue.builder().bool(p.getAtivo()).build());
        }
        if (p.getCriadoEm() != null) {
            item.put("criadoEm", AttributeValue.builder().s(p.getCriadoEm().toString()).build());
        }
        if (p.getAtualizadoEm() != null) {
            item.put("atualizadoEm", AttributeValue.builder().s(p.getAtualizadoEm().toString()).build());
        }

        return item;
    }

    private Produto fromItem(Map<String, AttributeValue> item) {
        Produto p = new Produto();

        p.setId(getString(item, "id"));
        p.setNome(getString(item, "nome"));
        p.setDescricao(getString(item, "descricao"));
        p.setCategoria(getString(item, "categoria"));
        p.setMarca(getString(item, "marca"));

        String precoStr = getNumberAsString(item, "preco");
        if (precoStr != null) {
            p.setPreco(new BigDecimal(precoStr));
        }

        String qtdStr = getNumberAsString(item, "quantidadeEstoque");
        if (qtdStr != null) {
            p.setQuantidadeEstoque(Integer.parseInt(qtdStr));
        }

        if (item.containsKey("ativo") && item.get("ativo").bool() != null) {
            p.setAtivo(item.get("ativo").bool());
        } else {
            p.setAtivo(true);
        }

        String criadoEmStr = getString(item, "criadoEm");
        if (criadoEmStr != null) {
            p.setCriadoEm(Instant.parse(criadoEmStr));
        }

        String atualizadoEmStr = getString(item, "atualizadoEm");
        if (atualizadoEmStr != null) {
            p.setAtualizadoEm(Instant.parse(atualizadoEmStr));
        }

        return p;
    }

    private String getString(Map<String, AttributeValue> item, String key) {
        if (!item.containsKey(key) || item.get(key).s() == null) {
            return null;
        }
        return item.get(key).s();
    }

    private String getNumberAsString(Map<String, AttributeValue> item, String key) {
        if (!item.containsKey(key) || item.get(key).n() == null) {
            return null;
        }
        return item.get(key).n();
    }
}
