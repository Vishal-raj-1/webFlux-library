package com.FunctionWeb.Library.handler;

import com.FunctionWeb.Library.dto.AuthorDTO;
import com.FunctionWeb.Library.repository.AuthorRepository;
import com.FunctionWeb.Library.utils.AuthorDTOEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class AuthorHandler {
    @Autowired
    private AuthorRepository authorRepository;

    public Mono<ServerResponse> getAuthorById(ServerRequest request) {
        String id = request.pathVariable("id");
        return authorRepository
                .findById(id)
                .flatMap(author -> ServerResponse
                        .ok()
                        .bodyValue(AuthorDTOEntityConverter.entityToDTO(author)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getAllAuthors(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository
                                .findAll()
                                .map(AuthorDTOEntityConverter::entityToDTO)
                        , AuthorDTO.class);
    }

    public Mono<ServerResponse> getAuthorsByNameRegex(ServerRequest request) {
        String nameRegex = request.queryParam("nameRegex").orElse("");

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository.findByNameRegex(nameRegex)
                        .map(AuthorDTOEntityConverter::entityToDTO), AuthorDTO.class);
    }


    public Mono<ServerResponse> saveAuthor(ServerRequest request) {
        Mono<AuthorDTO> authorDTOMono = request.bodyToMono(AuthorDTO.class);

        return authorDTOMono.flatMap(authorDTO -> {
            Mono<AuthorDTO> savedAuthorMono = Mono.fromCallable(() ->
                            AuthorDTOEntityConverter.dtoToEntity(authorDTO))
                    .flatMap(authorRepository::save)
                    .map(AuthorDTOEntityConverter::entityToDTO);

            return ServerResponse.ok().body(savedAuthorMono, AuthorDTO.class);
        }).onErrorResume(e -> {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .bodyValue("Error saving author: " + e.getMessage());
        });
    }
}
