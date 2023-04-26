package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Card implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    private String code;
    
    private String image;
    private Images images;
    private String value;
    private String suit;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getSuit() {
        return suit;
    }
    public void setSuit(String suit) {
        this.suit = suit;
    }
    public Images getImages() {
        return images;
    }
    public void setImages(Images images) {
        this.images = images;
    }
}

