package kr.co.csalgo.application.admin.dto;

import kr.co.csalgo.domain.user.type.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleDto {
	private Role role;
}

