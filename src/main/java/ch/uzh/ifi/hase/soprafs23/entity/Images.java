package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Images implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long ImageId;
    
    public String svg;
    public String png;
    
    public String getSvg() {
        return svg;
    }
    public void setSvg(String svg) {
        this.svg = svg;
    }
    public String getPng() {
        return png;
    }
    public void setPng(String png) {
        this.png = png;
    }
}
