package com.FunctionWeb.Library.repository;

import com.FunctionWeb.Library.model.Book;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Flux<Book> findByGenre(String genre);

    @Query("{ genre : ?0 , copiesAvailable : { $gt : ?1} }")
    Flux<Book> findByGenreAndCopiesAvailable(String genre, int copiesAvailable);

    Flux<Book> findByAuthorIdIn(List<String> authorIds);
}
