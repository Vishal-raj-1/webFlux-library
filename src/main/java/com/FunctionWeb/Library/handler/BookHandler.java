package com.FunctionWeb.Library.handler;

import com.FunctionWeb.Library.dto.BookDTO;
import com.FunctionWeb.Library.dto.SaveBookDTO;
import com.FunctionWeb.Library.model.Author;
import com.FunctionWeb.Library.repository.AuthorRepository;
import com.FunctionWeb.Library.repository.BookRepository;
import com.FunctionWeb.Library.utils.BookDTOEntityConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookHandler {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookHandler(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Mono<ServerResponse> getAllBooks(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookRepository
                                .findAll()
                                .map(BookDTOEntityConverter::entityToDTO)
                        , BookDTO.class);
    }


    public Mono<ServerResponse> getBookById(ServerRequest request){
            String id = request.pathVariable("id");
            return bookRepository
                    .findById(id)
                    .flatMap(book -> ServerResponse
                                        .ok()
                                        .bodyValue(BookDTOEntityConverter.entityToDTO(book)))
                    .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getBooksByGenre(ServerRequest request){
            String genre = request.queryParam("genre").orElse("");
            return ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bookRepository
                            .findByGenre(genre)
                            .map(BookDTOEntityConverter::entityToDTO)
                            ,BookDTO.class);
    }

    public Mono<ServerResponse> getBooksByGenreAndCopiesAvailable(ServerRequest request){
            String genre = request.queryParam("genre").orElse("");
            int copiesAvailable = Integer.parseInt(request.queryParam("copiesAvailable").orElse("0"));

            return ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bookRepository
                            .findByGenreAndCopiesAvailable(genre, copiesAvailable)
                            .map(BookDTOEntityConverter::entityToDTO)
                            ,BookDTO.class);
    }


    public Mono<ServerResponse> saveBook(ServerRequest request){
        return request.bodyToMono(SaveBookDTO.class)
                .flatMap(saveBookDTO -> {
                    String authorName = saveBookDTO.getAuthorName();
                    BookDTO book = saveBookDTO.getBook();

                    return authorRepository.findByName(authorName)
                            .switchIfEmpty(Mono.error(new NoSuchElementException("Author Named " + authorName + " not found")))
                            .flatMap(author -> {
                                book.setAuthorId(author.getId());
                                return bookRepository.save(BookDTOEntityConverter.dtoToEntity(book));
                            }).flatMap(savedBook -> ServerResponse
                                    .status(HttpStatus.CREATED)
                                    .bodyValue(BookDTOEntityConverter.entityToDTO(savedBook)));
                });
    }

    public Mono<ServerResponse> getBooksByAuthorNames(ServerRequest request) {
        List<String> authorNames = request.queryParam("authorNames")
                .map(names -> Arrays.asList(names.split(",")))
                .orElse(Collections.emptyList());

        Flux<String> authorIdsFlux = Flux.fromIterable(authorNames)
                .flatMap(authorName -> authorRepository.findByName(authorName)
                        .map(Author::getId)
                        .defaultIfEmpty(""));

        Flux<BookDTO> booksFlux = authorIdsFlux.collectList()
                .flatMapMany(bookRepository::findByAuthorIdIn)
                .map(BookDTOEntityConverter::entityToDTO);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(booksFlux.collectList(), new ParameterizedTypeReference<List<BookDTO>>() {});
    }

}
