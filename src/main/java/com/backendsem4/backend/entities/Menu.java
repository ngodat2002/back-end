package com.backendsem4.backend.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@ToString
@Table(name="menu")
public class Menu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menu_id;
    private String menu_image;
    private String menu_name;
    @OneToOne
    private Category category;
}

