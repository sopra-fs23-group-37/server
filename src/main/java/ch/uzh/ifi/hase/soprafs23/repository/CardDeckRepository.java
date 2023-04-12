package ch.uzh.ifi.hase.soprafs23.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs23.entity.CardDeck;

@Repository("cardDeckRepository")
public interface CardDeckRepository extends JpaRepository<CardDeck, Long>{

    
    
}
