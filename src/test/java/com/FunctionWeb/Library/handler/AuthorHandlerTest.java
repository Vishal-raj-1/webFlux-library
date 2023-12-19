package com.FunctionWeb.Library.handler;

import com.FunctionWeb.Library.dto.AuthorDTO;
import com.FunctionWeb.Library.model.Author;
import com.FunctionWeb.Library.repository.AuthorRepository;
import com.FunctionWeb.Library.routes.AuthorRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@SpringBootTest
@AutoConfigureWebTestClient
class AuthorHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorRouter authorRouter;

    private RouterFunction<ServerResponse> routes;

    @BeforeEach
    void setUp() {
        routes = authorRouter.authorRoutes();
    }

    @Test
    void testGetAuthorById() {
        String authorId = "1";
        Mono<Author> authorMono = Mono.just( new Author(authorId, "John Doe"));

        when(authorRepository.findById(authorId)).thenReturn(authorMono);

        webTestClient
                .get()
                .uri("/api/authors/{id}", authorId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthorDTO.class)
                .value(authorDTO -> {
                    assertEquals(authorId, authorDTO.getId());
                    assertEquals("John Doe", authorDTO.getName());
                });
    }

    @Test
    void testGetAllAuthors() {
        Flux<Author> authorFlux = Flux.just(
                new Author("1", "John Doe"),
                new Author("2", "Emily White")
        );

        when(authorRepository.findAll()).thenReturn(authorFlux);

        webTestClient
                .get()
                .uri("/api/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDTO.class)
                .hasSize(2);
    }

    @Test
    void testGetAuthorsByNameRegex() {
        String nameRegex = "John";
        Flux<Author> authorFlux = Flux.just(
                new Author("1", "John Doe"),
                new Author("2", "John Smith")
        );

        when(authorRepository.findByNameRegex(nameRegex)).thenReturn(authorFlux);

        webTestClient
                .get()
                .uri("/api/authors/byNameRegex?nameRegex={nameRegex}", nameRegex)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDTO.class)
                .hasSize(2);
    }

    @Test
    void testSaveAuthor() {
        AuthorDTO newAuthorDTO = new AuthorDTO("3", "Vishal Rajput");

        when(authorRepository.save(any())).thenAnswer(invocation -> {
            Author savedAuthor = invocation.getArgument(0);
            return Mono.just(savedAuthor);
        });

        webTestClient
                .post()
                .uri("/api/authors")
                .bodyValue(newAuthorDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthorDTO.class)
                .value(authorDTO -> {
                    assertEquals("3", authorDTO.getId());
                    assertEquals("Vishal Rajput", authorDTO.getName());
                });
    }

}
