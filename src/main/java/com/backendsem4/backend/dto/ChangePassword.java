package com.backendsem4.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {
	@NotEmpty
	@Length(min = 6)
	private String newPassword;
	@NotEmpty
	@Length(min = 6)
	private String confirmPassword;
}
