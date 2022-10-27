package com.backendsem4.backend.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("product_id")
    @Expose
    private Long productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("discount")
    @Expose
    private int discount;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    private String qrCode;
    @Null
    private String thumbnail;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("entered_date")
    @Expose
    @Temporal(TemporalType.DATE)
    private Date enteredDate;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("favorite")
    @Expose
    public boolean favorite;
    @SerializedName("category_id")
    @Expose
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
}

