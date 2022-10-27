package com.backendsem4.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author DongTHD
 *
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "favorites")
public class Favorite implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long favoriteId;

	@ManyToOne()
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne()
	@JoinColumn(name = "productId")
	private Product product;

}
