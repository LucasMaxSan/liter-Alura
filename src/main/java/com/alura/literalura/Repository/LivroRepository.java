package com.alura.literalura.Repository;

import com.alura.literalura.Model.Livros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livros, Long> {
    Optional<Livros> findByTitulo(String titulo);


    List<Livros> findByIdioma(String idioma);


    @Query("SELECT l FROM Livro l JOIN FETCH l.autores WHERE l.idioma = :idioma")
    List<Livros> findByIdiomaWithAutores(@Param("idioma") String idioma);
}