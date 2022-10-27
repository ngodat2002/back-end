package com.backendsem4.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String content;
	@Temporal(TemporalType.DATE)
	private Date rateDate;
	private int star;
	private int status;
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne
	@JoinColumn(name = "productId")
	private Product product;
}
