package com.FunctionWeb.Library.routes;

import com.FunctionWeb.Library.handler.AuthorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class AuthorRouter {

    @Autowired
    private AuthorHandler authorHandler;

    @Bean
    public RouterFunction<ServerResponse> authorRoutes() {
        return route()
                .nest(path("/api/authors"), builder ->
                        builder.GET("/byNameRegex", authorHandler::getAuthorsByNameRegex)
                                .GET("/{id}", authorHandler::getAuthorById)
                                .GET("", authorHandler::getAllAuthors)
                                .POST("", authorHandler::saveAuthor))
                .build();
    }
}

