package ch.uzh.ifi.hase.soprafs23.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs23.entity.Card;

@Repository("cardRepository")
public interface CardRepository extends JpaRepository<Card, Long>{

    
}
