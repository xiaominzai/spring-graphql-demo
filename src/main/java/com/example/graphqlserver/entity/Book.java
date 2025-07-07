package com.example.graphqlserver.entity;

import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 书本+模拟数据库操作
 * @author chenym
 * @date 2025/7/2 11:24
 * @description Book
 */
public record Book(
    Long id,
    String title,
    String isbn,
    Long authorId // 一对一
) {
    public static final List<Book> books = List.of(
        new Book(1L, "Java 编程思想", "9787111213826", 1L), // author 将通过 DataLoader 加载
        new Book(2L, "Effective Java", "9780134685991", 2L),
        new Book(3L, "银河系漫游指南", "9780345391803", 3L),
        new Book(4L, "万物简史", "9787532738841", 3L),
        new Book(5L, "星辰记", "9787508439101", 4L),
        new Book(6L, "Python 编程思想", "9787508439102", 4L)
    );

    public static List<Book> getBooks(int page, int size, String titleFilter) {
        return books.stream()
            .filter(b -> titleFilter == null || b.title().contains(titleFilter))
            .skip((long) (page - 1) * size)
            .limit(size)
            .toList();
    }

    public static Book getById(Long id) {
        return books.stream().filter(b -> b.id().equals(id)).findFirst().orElse(null);
    }

    public static List<Book> getBooksByTitle(String title) {
        return books.stream()
            .filter(b -> b.title().toLowerCase().contains(title.toLowerCase()))
            .toList();
    }

    public static List<Book> getBooksByAuthorId(Long authorId) {
        // 根据作者ID返回对应的书籍
        // 这里需要根据实际的业务逻辑来映射作者和书籍的关系
        return switch (authorId.intValue()) {
            case 1 -> List.of(books.get(0)); // Joshua Bloch -> Java 编程思想
            case 2 -> List.of(books.get(1)); // Douglas Adams -> Effective Java
            case 3 -> List.of(books.get(2), books.get(3)); // Bill Bryson -> 银河系漫游指南, 万物简史
            case 4 -> List.of(books.get(4), books.get(5)); // J. R. R. Tolkien -> 星辰记, Python 编程思想
            default -> List.of();
        };
    }

    public static Map<Long, List<Book>> getBooksByBatchAuthorId(Set<Long> authorIdList) {
        if (CollectionUtils.isEmpty(authorIdList)) {
            return Map.of();
        }
        Map<Long, List<Book>> result = new HashMap<>(authorIdList.size());
        for (Long authorId : authorIdList) {
            List<Book> bookList = getBooksByAuthorId(authorId);
            result.put(authorId, bookList);
        }
        return result;
    }

    public static Map<Long, Book> getBooksById(Set<Long> bookIdList) {
        if (CollectionUtils.isEmpty(bookIdList)) {
            return Map.of();
        }
        return books.stream()
                .filter(b -> bookIdList.contains(b.id()))
                .collect(Collectors.toMap(Book::id, Function.identity()));
    }
}
