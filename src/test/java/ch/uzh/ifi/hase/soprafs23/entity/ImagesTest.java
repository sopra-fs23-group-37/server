package ch.uzh.ifi.hase.soprafs23.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ImagesTest {
    private Images images = new Images();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        images.setPng("testPng");
        images.setSvg("testSvg");
    }

    @Test
    public void createImages_validInputs() {
        assertEquals("testPng", images.getPng());
        assertEquals("testSvg", images.getSvg());
    }
}