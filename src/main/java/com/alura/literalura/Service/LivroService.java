package com.alura.literalura.Service;

import com.alura.literalura.Model.Autor;
import com.alura.literalura.Model.DadosLivro;
import com.alura.literalura.Model.Livros;
import com.alura.literalura.Repository.AutorRepository;
import com.alura.literalura.Repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Transactional
    public void salvarLivro(DadosLivro dadosLivro) {

        Optional<Livros> livroExistente = livroRepository.findByTitulo(dadosLivro.título());

        if (livroExistente.isPresent()) {
            System.out.println("O livro '" + dadosLivro.título() + "' já está registrado no banco de dados.");
            return;
        }

        Livros livro = new Livros();
        livro.setTitulo(dadosLivro.título());
        livro.setNumeroDownloads(dadosLivro.numeroDownloads());


        livro.setIdioma(dadosLivro.idioma().isEmpty() ? null : dadosLivro.idioma().get(0));


        List<Autor> autores = dadosLivro.autor().stream().map(dadosAutor -> {
            return autorRepository.findByNome(dadosAutor.nome())
                    .orElseGet(() -> {
                        Autor novoAutor = new Autor();
                        novoAutor.setNome(dadosAutor.nome());
                        novoAutor.setAnoNascimento(dadosAutor.anoNascimento());
                        novoAutor.setAnoFalecimento(dadosAutor.anoFalecimento());
                        return novoAutor;
                    });
        }).collect(Collectors.toList());


        autorRepository.saveAll(autores.stream().filter(autor -> autor.getId() == null).collect(Collectors.toList()));
        livro.setAutores(autores);


        livroRepository.save(livro);
    }


    @Transactional(readOnly = true)
    public List<String> listarLivros() {
        List<Livros> livros = livroRepository.findAll();


        for (Livros livro : livros) {
            livro.getAutores().size();
        }


        return livros.stream().map(livro -> {
            String autores = livro.getAutores().stream()
                    .map(Autor::getNome)
                    .collect(Collectors.joining(", "));

            return "-------------- Livro ----------------\n" +
                    "Título: " + livro.getTitulo() + "\n" +
                    "Autores: " + autores + "\n" +
                    "Idioma: " + livro.getIdioma() + "\n" +
                    "Número de Downloads: " + livro.getNumeroDownloads() + "\n" +
                    "--------------------------------------";
        }).collect(Collectors.toList());
    }


    public Optional<Livros> buscarPorTitulo(String titulo) {
        return livroRepository.findByTitulo(titulo);
    }


    @Transactional(readOnly = true)
    public List<Livros> buscarLivrosPorIdioma(String idioma) {
        List<Livros> livros = livroRepository.findByIdiomaWithAutores(idioma);


        return livros;
    }
}