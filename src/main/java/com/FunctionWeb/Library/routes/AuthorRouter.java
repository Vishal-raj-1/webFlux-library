package com.FunctionWeb.Library.routes;

import com.FunctionWeb.Library.handler.AuthorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;


@Configuration
public class AuthorRouter {

    @Autowired
    private AuthorHandler authorHandler;

    @Bean
    public RouterFunction<ServerResponse> authorRoutes() {
        return nest(path("/authors"),
                route(GET("/all"), authorHandler::getAllAuthors)
                        .andRoute(GET("/{id}"), authorHandler::getAuthorById)
                        .andRoute(GET("/byName/{nameRegex}"), authorHandler::getAuthorsByNameRegex)
                        .andRoute(POST("/save").and(accept(MediaType.APPLICATION_JSON)), authorHandler::saveAuthor)
        );
    }

}

