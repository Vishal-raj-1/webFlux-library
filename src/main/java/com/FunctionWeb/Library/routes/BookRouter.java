package com.FunctionWeb.Library.routes;

import com.FunctionWeb.Library.handler.BookHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;

@Configuration
public class BookRouter {
    @Autowired
    private BookHandler bookHandler;
    @Bean
    public RouterFunction<ServerResponse> bookRoutes() {
        return nest(path("/api/books"),
                route(GET("/"), bookHandler::getAllBooks)
                        .andRoute(GET("/{id}"), bookHandler::getBookById)
                        .andRoute(GET("/byGenre"), bookHandler::getBooksByGenre)
                        .andRoute(GET("/byGenreAndCopiesAvailable"), bookHandler::getBooksByGenreAndCopiesAvailable)
//                        .andRoute(POST("/byAuthorName").and(accept(MediaType.APPLICATION_JSON)), bookHandler.getBooksByAuthorName())
        );
    }


}
