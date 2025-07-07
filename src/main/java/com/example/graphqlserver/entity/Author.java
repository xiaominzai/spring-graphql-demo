package com.example.graphqlserver.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 作家实体+模拟数据库操作
 * @author chenym
 * @date 2025/7/2 11:27
 * @description Author
 */
public record Author(
    Long id,
    String name,
    String email
) {

    private static final List<Author> authors = List.of(
            new Author(1L, "Joshua Bloch", "joshua@example.com"),
            new Author(2L, "Douglas Adams", "douglas@example.com"),
            new Author(3L, "Bill Bryson", "bill@example.com"),
            new Author(4L, "J. R. R. Tolkien", "tolkien@example.com")
    );


    public static Author getById(Long id) {
        return authors.stream()
                .filter(author -> author.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static Map<Long, Author> getAuthorsByIds(Set<Long> ids) {
        return authors.stream()
            .filter(a -> ids.contains(a.id()))
            .collect(Collectors.toMap(Author::id, a -> a));
    }

    public static List<Author> getAuthorById(Set<Long> ids) {
        return authors.stream()
                .filter(a -> ids.contains(a.id()))
                .toList();
    }
}
