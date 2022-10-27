package com.backendsem4.backend.repository;

import com.backendsem4.backend.entities.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {
}
