package com.example.graphqlserver.controller;

import com.example.graphqlserver.entity.Author;
import com.example.graphqlserver.entity.Book;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 书本控制器
 * @author chenym
 * @date 2025/7/2 11:28
 * @description BookController
 */
@Controller
public class BookController {

    /**
     * 通过id查找书本
     * @param id 书本id
     * @return 书本
     */
    @QueryMapping
    public Book bookById(@Argument Long id) {
        return Book.getById(id);
    }

    @QueryMapping
    public List<Book> books(@Argument int page, @Argument int size, @Argument String titleFilter) {
        return Book.getBooks(page, size, titleFilter);
    }

    @QueryMapping
    public List<Book> booksByTitle(@Argument String title) {
        return Book.getBooksByTitle(title);
    }

//    @SchemaMapping
//    public CompletableFuture<List<Book>> books(Author author) {
//          // 非批量写法
//        return CompletableFuture.completedFuture(Book.getBooksByAuthorId(author.id()));
//    }

    /**
     * 通过作家查找书本，并且指定DataLoader。这个主要使用于子列表的情况
     * @param author 作家
     * @param booksByAuthor 加载器，命名的名字就是对应的加载器名称。对应：booksByAuthor
     * @return 结果
     */
    @SchemaMapping
    public CompletableFuture<List<Book>> books(Author author, DataLoader<Long, List<Book>> booksByAuthor) {
        return booksByAuthor.load(author.id());
    }
}
