package com.FunctionWeb.Library.handler;

import com.FunctionWeb.Library.dto.AuthorDTO;
import com.FunctionWeb.Library.repository.AuthorRepository;
import com.FunctionWeb.Library.utils.AuthorDTOEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class AuthorHandler {
    @Autowired
    private AuthorRepository authorRepository;

    public Mono<ServerResponse> getAuthorById(ServerRequest request){
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


    public Mono<ServerResponse> getAuthorsByNameRegex(ServerRequest request){
            String nameRegex = request.pathVariable("nameRegex");
            return ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(authorRepository
                            .findByNameRegex(nameRegex)
                            .map(AuthorDTOEntityConverter::entityToDTO)
                    ,AuthorDTO.class);
    }

    public Mono<ServerResponse> saveAuthor(ServerRequest request){
            Mono<AuthorDTO> authorDTOMono = request.bodyToMono(AuthorDTO.class);
            return authorDTOMono
                    .flatMap(authorDTO -> ServerResponse
                            .ok()
                            .bodyValue(authorRepository
                                    .save(AuthorDTOEntityConverter
                                            .dtoToEntity(authorDTO))
                                    .map(AuthorDTOEntityConverter::entityToDTO)
                            ));
    }

}
