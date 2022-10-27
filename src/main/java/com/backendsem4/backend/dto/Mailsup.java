package com.backendsem4.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mailsup {
    String name;
    String email;
    String title;
    String content;

}