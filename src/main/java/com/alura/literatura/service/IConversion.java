package com.alura.literatura.service;

public interface IConversion {
    <T> T convertirDatos(String json, Class<T> clase);
}
