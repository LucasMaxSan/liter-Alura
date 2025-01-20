package com.alura.literalura.Model;
import java.util.List;

public record Listas(
        int count,
        String next,
        String previous,
        List<DadosLivro> results
) {
}