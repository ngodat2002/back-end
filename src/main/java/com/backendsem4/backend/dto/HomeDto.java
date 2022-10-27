package com.backendsem4.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HomeDto<T> {
    public T listBestSale;
    public T listNew;
    public T listNoiBat;

}
