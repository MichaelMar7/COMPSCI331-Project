package proj.concert.service.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ConcertSummary {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String imageName;

    public ConcertSummary() {
    }

    public ConcertSummary(Long id, String title, String imageName) {
        this.title = title;
        this.imageName = imageName;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}
