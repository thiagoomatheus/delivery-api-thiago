package com.deliverytech.delivery_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import com.deliverytech.delivery_api.security.JwtUtil;

@WebMvcTest(
  controllers = HealthController.class,
  excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
class HealthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private JwtUtil jwtUtil;

  @MockBean
  private UserDetailsService userDetailsService;

  @Test
  @DisplayName("Deve retornar status da saúde do serviço")
  void deveRetornarStatusSaude() throws Exception {
    mockMvc.perform(get("/health"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("UP"));
  }

  @Test
  @DisplayName("Deve retornar informações do serviço")
  void deveRetornarInformacoesServico() throws Exception {
    mockMvc.perform(get("/info"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.application").value("Delivery Tech API"))
      .andExpect(jsonPath("$.version").value("1.0.0"))
      .andExpect(jsonPath("$.developer").value("Thiago Oliveira Matheus"))
      .andExpect(jsonPath("$.javaVersion").value("JDK 21"))
      .andExpect(jsonPath("$.framework").value("Spring Boot 3.2.x"));
  }
    
}
