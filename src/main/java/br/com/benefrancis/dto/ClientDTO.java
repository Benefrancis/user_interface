package br.com.benefrancis.dto;

import lombok.Data;

@Data
public class ClientDTO {
	private String id;
	
	private String name;

	private String redirect;

	private String scope;

	private Integer accessTokenValidity;

	private Integer refreshTokenValidity;

	private String resourceIds;

	private String grantTypes;

	private String authorities;
}
