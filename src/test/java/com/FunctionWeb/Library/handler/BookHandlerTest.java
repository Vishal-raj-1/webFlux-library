package com.FunctionWeb.Library.handler;

import com.FunctionWeb.Library.dto.BookDTO;
import com.FunctionWeb.Library.dto.SaveBookDTO;
import com.FunctionWeb.Library.model.Author;
import com.FunctionWeb.Library.model.Book;
import com.FunctionWeb.Library.repository.AuthorRepository;
import com.FunctionWeb.Library.repository.BookRepository;
import com.FunctionWeb.Library.routes.BookRouter;
import com.FunctionWeb.Library.utils.BookDTOEntityConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
public class BookHandlerTest {
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookRouter bookRouter;

    @Autowired
    private AuthorRepository authorRepository;

    private RouterFunction<ServerResponse> routes;

    @BeforeEach
    void setUp(){
        routes = bookRouter.bookRoutes();
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book("Book1", "Genre1", "Author1", 5);
        Book book2 = new Book("Book2", "Genre2", "Author2", 10);

        when(bookRepository.findAll()).thenReturn(Flux.just(book1, book2));

        webTestClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDTO.class)
                .hasSize(2);
    }

    @Test
    void testGetBookById() {
        String bookId = "1";
        BookDTO book = new BookDTO(bookId, "Genre1", "author1", 5);

        when(bookRepository.findById(bookId)).thenReturn(Mono.just(BookDTOEntityConverter.dtoToEntity(book)));

        webTestClient.get()
                .uri("/api/books/{id}", bookId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDTO.class)
                .isEqualTo(book);
    }


    @Test
    void testGetBooksByGenre() {
        String genre = "Mystery";
        Book book1 = new Book("Book1", "Mystery", "Author1", 5);
        Book book2 = new Book("Book2", "Mystery", "Author2", 2);

        when(bookRepository.findByGenre(genre)).thenReturn(Flux.just(book1, book2));

        webTestClient.get()
                .uri("/api/books/byGenre?genre={genre}", genre)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDTO.class)
                .hasSize(2);
    }

    @Test
    void testGetBooksByGenreAndCopiesAvailable() {
        String genre = "Mystery";
        int copiesAvailable = 3;
        Book book1 = new Book("Book1", "Mystery", "Author1", 5);
        Book book2 = new Book("Book2", "Mystery", "Author2", 10);

        when(bookRepository.findByGenreAndCopiesAvailable(genre, copiesAvailable)).thenReturn(Flux.just(book1, book2));

        webTestClient.get()
                .uri("/api/books/byGenreAndCopiesAvailable?genre={genre}&copiesAvailable={copiesAvailable}", genre, copiesAvailable)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDTO.class)
                .hasSize(2);
    }

    @Test
    void testSaveBook() {
        BookDTO bookDTO = new BookDTO("Book1", "Mystery", "1", 5);

        SaveBookDTO saveBookDTO = new SaveBookDTO();
        saveBookDTO.setAuthorName("Author1");
        saveBookDTO.setBook(bookDTO);

        Author author = new Author("1", "Author1");

        when(authorRepository.findByName("Author1")).thenReturn(Mono.just(author));

        when(bookRepository.save(any()))
                .thenAnswer(invocation -> {
                    Book savedBook = invocation.getArgument(0);
                    return Mono.just(savedBook);
                });

        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(saveBookDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDTO.class)
                .isEqualTo(bookDTO);
    }
}
