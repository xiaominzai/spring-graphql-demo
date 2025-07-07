package com.example.graphqlserver.controller;

import com.example.graphqlserver.entity.Author;
import com.example.graphqlserver.entity.Book;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CompletableFuture;

/**
 * 作家控制器
 * @author chenym
 * @date 2025/7/2 11:28
 * @description AuthorController
 */
@Controller
public class AuthorController {



    @SchemaMapping
    public CompletableFuture<Author> author(Book book, DataLoader<Long, Author> authorLoader) {
        //  这里会调用多次，但是authorLoader会做聚合
        return authorLoader.load(book.authorId());
    }
} 