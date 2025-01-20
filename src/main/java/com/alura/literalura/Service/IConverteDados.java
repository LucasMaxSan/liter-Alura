package com.alura.literalura.Service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}