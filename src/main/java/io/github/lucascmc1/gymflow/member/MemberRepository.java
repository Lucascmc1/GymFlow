package io.github.lucascmc1.gymflow.member;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    long countByStatus(MemberStatus status);

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    List<Member> findByStatusOrderByNameAsc(MemberStatus status);

    @Query("""
            SELECT m FROM Member m
            WHERE (:query IS NULL
                OR LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%'))
                OR m.cpf LIKE CONCAT('%', :query, '%')
                OR LOWER(m.email) LIKE LOWER(CONCAT('%', :query, '%')))
              AND (:status IS NULL OR m.status = :status)
            ORDER BY m.name
            """)
    List<Member> search(@Param("query") String query, @Param("status") MemberStatus status);
}
