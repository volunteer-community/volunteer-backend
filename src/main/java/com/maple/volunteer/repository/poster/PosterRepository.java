package com.maple.volunteer.repository.poster;

import com.maple.volunteer.domain.poster.Poster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosterRepository extends JpaRepository<Poster, Long> {
}
