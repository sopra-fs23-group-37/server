package ch.uzh.ifi.hase.soprafs23.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs23.entity.Images;

@Repository("imageRepository")
public interface ImageRepository extends JpaRepository<Images, Long>{

    //Image findbyImageId(Long imageId);
}
