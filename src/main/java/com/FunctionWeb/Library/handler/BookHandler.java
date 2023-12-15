package com.FunctionWeb.Library.handler;

import com.FunctionWeb.Library.dto.BookDTO;
import com.FunctionWeb.Library.repository.AuthorRepository;
import com.FunctionWeb.Library.repository.BookRepository;
import com.FunctionWeb.Library.utils.BookDTOEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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


//    public HandlerFunction<ServerResponse> saveBook(){
//        return request -> {
//            Mono<Author>
//        };
//    }

//    public Mono<ServerResponse> getBooksByAuthorName(ServerRequest request){
//            return request.bodyToMono(authorNames -> {
//                List<String> authorIds = new ArrayList<>();
//                Flux.fromIterable(authorNames)
//                        .flatMap(authorName -> authorRepository.findByName((String) authorName))
//                        .doOnNext(author -> {
//                            if(author != null){
//                                authorIds.add(author.getId());
//                            }
//                        })
//                        .then(ServerResponse
//                                .ok()
//                                .body(bookRepository
//                                        .findByAuthorIdIn(authorIds)
//                                        .map(BookDTOEntityConverter::entityToDTO)
//                                        ,BookDTO.class));
//            });
//    }
}
