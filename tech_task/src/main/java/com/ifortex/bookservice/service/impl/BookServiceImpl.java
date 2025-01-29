package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.service.BookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<String, Long> getBooks() {
        String query = """
                    SELECT g.genre, COUNT(b.id) as book_count
                    FROM books b, LATERAL unnest(b.genre) as g(genre)
                    GROUP BY g.genre
                    ORDER BY book_count DESC
                """;

        List<Object[]> results = entityManager.createNativeQuery(query).getResultList();
        return results.stream().collect(Collectors.toMap(
                row -> (String) row[0],
                row -> ((Number) row[1]).longValue(),
                (v1, v2) -> v1, LinkedHashMap::new
        ));
    }
}
