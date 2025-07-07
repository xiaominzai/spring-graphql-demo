package com.example.graphqlserver.config;

import com.example.graphqlserver.entity.Author;
import com.example.graphqlserver.entity.Book;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 批量加载器配置
 * @author chenym
 * @date 2025/7/7 11:07
 * @description 重构后的DataLoader配置类，将不同类型的加载器配置分离到独立方法中
 */
@Configuration
public class DataLoaderConfig {

    public DataLoaderConfig(BatchLoaderRegistry registry) {
        // 注册所有批量加载器
        registerAuthorDataLoader(registry);
        registerBookDataLoader(registry);
        registerBooksByAuthorDataLoader(registry);
    }

    /**
     * 注册作者批量加载器
     * 通过ID批量加载作者信息
     */
    private void registerAuthorDataLoader(BatchLoaderRegistry registry) {
        registry.forTypePair(Long.class, Author.class)
                .registerMappedBatchLoader((authorIds, env) -> 
                    Mono.just(Author.getAuthorsByIds(authorIds))
                );
    }

    /**
     * 注册书籍批量加载器
     * 通过ID批量加载书籍信息
     */
    private void registerBookDataLoader(BatchLoaderRegistry registry) {
        registry.forTypePair(Long.class, Book.class)
                .registerMappedBatchLoader((bookIds, env) -> 
                    Mono.just(Book.getBooksById(bookIds))
                );
    }

    /**
     * 注册按作者查询书籍的批量加载器
     * 通过作者ID批量加载对应的书籍列表
     */
    private void registerBooksByAuthorDataLoader(BatchLoaderRegistry registry) {
        registry.<Long, List<Book>>forName("booksByAuthor")
                .registerMappedBatchLoader((authorIdSet, env) -> 
                    Mono.just(Book.getBooksByBatchAuthorId(authorIdSet))
                );
    }
}
