package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Member findMember() {
        String query = """
                    SELECT m.*
                    FROM members m
                    JOIN member_books mb ON m.id = mb.member_id
                    JOIN books b ON mb.book_id = b.id
                    WHERE 'Romance' = ANY(b.genre)
                    ORDER BY b.publication_date ASC, m.membership_date DESC
                    LIMIT 1
                """;

        List<Member> members = entityManager.createNativeQuery(query, Member.class).getResultList();
        return members.isEmpty() ? null : members.get(0);
    }

    @Override
    public List<Member> findMembers() {
        String query = """
                    SELECT m.*
                    FROM members m
                    LEFT JOIN member_books mb ON m.id = mb.member_id
                    WHERE EXTRACT(YEAR FROM m.membership_date) = 2023
                    AND mb.member_id IS NULL
                """;

        return entityManager.createNativeQuery(query, Member.class).getResultList();
    }
}
