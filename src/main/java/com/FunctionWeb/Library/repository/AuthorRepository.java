package com.FunctionWeb.Library.repository;

import com.FunctionWeb.Library.model.Author;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    Mono<Author> findByName(String authorName);

    @Query("{ name : { $regex : ?0 }}")
    Flux<Author> findByNameRegex(String nameRegex);
}
