package com.sample.base.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.base.client.user.dto.LoginRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("로그인 테스트 성공 케이스")
    void 로그인_성공() throws Exception {
        LoginRequestDto dto = LoginRequestDto.builder()
                .email("rlawodbs1024@gmail.com")
                .password("123456")
                .build();

        mockMvc.perform(
                        post("http://localhost:8080/users/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 케이스 1 - 이메일 EMPTY(AuthenticationServiceException)")
    void 로그인_실패_유형1() throws Exception {
        LoginRequestDto dto = LoginRequestDto.builder()
                .email("")
                .password("123456")
                .build();

        mockMvc.perform(
                        post("http://localhost:8080/users/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    @DisplayName("로그인 실패 케이스 2 - 존재하지 않는 이메일(BadCredentialsException)")
    void 로그인_실패_유형2() throws Exception {
        LoginRequestDto dto = LoginRequestDto.builder()
                .email("fdsfds")
                .password("123456")
                .build();

        mockMvc.perform(
                        post("http://localhost:8080/users/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    @DisplayName("로그인 실패 케이스 3 - 비밀번호 6자 미만")
    void 로그인_실패_유형3() throws Exception {
        LoginRequestDto dto = LoginRequestDto.builder()
                .email("rlawodbs102@gmail.com")
                .password("12345")
                .build();

        mockMvc.perform(
                        post("http://localhost:8080/users/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    @DisplayName("로그인 실패 케이스 4 - 비밀번호 20자 초과")
    void 로그인_실패_유형4() throws Exception {
        LoginRequestDto dto = LoginRequestDto.builder()
                .email("rlawodbs1024@gmail.com")
                .password("31223131242343454353453531232")
                .build();

        mockMvc.perform(
                        post("http://localhost:8080/users/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    @DisplayName("로그인 실패 케이스 5 - 비밀번호 틀림")
    void 로그인_실패_유형5() throws Exception {
        LoginRequestDto dto = LoginRequestDto.builder()
                .email("rlawodbs1024@gmail.com")
                .password("1234567")
                .build();

        mockMvc.perform(
                        post("http://localhost:8080/users/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().is(401));
    }
}
