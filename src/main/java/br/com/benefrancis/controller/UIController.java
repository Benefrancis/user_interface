package br.com.benefrancis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import br.com.benefrancis.config.AccessToken;
import br.com.benefrancis.dto.ClientDTO;

@Controller
@EnableOAuth2Sso
public class UIController extends WebSecurityConfigurerAdapter {

	@Autowired
	RestTemplate restTemplate;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").permitAll().anyRequest().authenticated().and().logout()
				.logoutSuccessUrl("/").permitAll();
	}

	@GetMapping("/")
	public String loadIU() {
		return "home";
	}

	@GetMapping("/secure")
	public String loadSecuredIU() {
		return "secure";
	}

	@RequestMapping("/clients")
	public String loadClients(Model model) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", AccessToken.getAccessToken());
		HttpEntity<ClientDTO> clientHttpEntity = new HttpEntity<>(headers);

		try {
			ResponseEntity<ClientDTO[]> responseEntity = restTemplate.exchange("http://localhost:8081/api/client",
					HttpMethod.GET, clientHttpEntity, ClientDTO[].class);
			model.addAttribute("clients", responseEntity.getBody());
		} catch (HttpStatusCodeException e) {

			@SuppressWarnings("rawtypes")
			ResponseEntity responseEntity = ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
					.body(e.getResponseBodyAsString());
			model.addAttribute("error", responseEntity);
		}

		return "secure";
	}

}
