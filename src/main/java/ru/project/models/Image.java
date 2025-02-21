package ru.project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "images")
public class Image {

    @Column(name = "image_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(name = "image_data", nullable = false)
    @Lob
    private byte[] imageData;

    @Column(name = "image_name", nullable = false)
    private String imageName;


    public Image(final byte[] imageData,
                 final String imageName) {
        this.imageData = imageData;
        this.imageName = imageName;
    }
}
