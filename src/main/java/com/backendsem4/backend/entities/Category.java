package com.backendsem4.backend.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @Column(name = "categoryName")
    private String categoryName;
    @Column(name = "categoryImage")
    private String categoryImage;
}
