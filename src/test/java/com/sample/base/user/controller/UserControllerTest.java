package com.sample.base.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.base.client.user.dto.UserInfoJoinRequestDto;
import com.sample.base.common.dto.CustomResponseDto;
import com.sample.base.common.security.provider.JwtTokenProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
    @Order(1)
    @DisplayName("회원 가입 테스트 - 정상 시나리오")
    void 회원가입_컨트롤러_테스트_정상시나리오() throws Exception {
        String email = "rlawodbs10245@gmail.com";
        String password = "123456";
        String username = "테스트위한회원가입";
        UserInfoJoinRequestDto dto = UserInfoJoinRequestDto.builder()
                        .email(email)
                        .password(password)
                        .userName(username)
                        .build();


        mockMvc.perform(
                        post("http://localhost:8080/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(dto))
                )
                .andDo(print())
                .andExpect(status().isOk());
        ;
    }

    @Test
    @Order(2)
    @DisplayName("회원 가입 테스트 - 예외 케이스 - 빈 값")
    void 회원가입_컨트롤러_테스트_유효성검증1() throws Exception {
        String email = "";
        String password = "123456";
        String username = "빈값";
        UserInfoJoinRequestDto dto = UserInfoJoinRequestDto.builder()
                .email(email)
                .password(password)
                .userName(username)
                .build();


        mockMvc.perform(
                        post("http://localhost:8080/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(dto))
                )
                .andDo(print())
                .andExpect(status().is(401));
        ;
    }

    @Test
    @Order(3)
    @DisplayName("회원 가입 테스트 - 예외 케이스 - 이메일 중복")
    void 회원가입_컨트롤러_테스트_유효성검증2() throws Exception {
        String email = "rlawodbs1024@gmail.com";
        String password = "123456";
        String username = "이메일 중복";
        UserInfoJoinRequestDto dto = UserInfoJoinRequestDto.builder()
                .email(email)
                .password(password)
                .userName(username)
                .build();


        mockMvc.perform(
                        post("http://localhost:8080/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(dto))
                )
                .andDo(print())
                .andExpect(status().is(401));
        ;
    }

    @Test
    @Order(4)
    @DisplayName("회원 가입 테스트 - 예외 케이스 - 비밀번호 6자 미만")
    void 회원가입_컨트롤러_테스트_유효성검증3() throws Exception {
        String email = "rlawodbs1024_case3@gmail.com";
        String password = "123";
        String username = "비밀번호 6자 미만";
        UserInfoJoinRequestDto dto = UserInfoJoinRequestDto.builder()
                .email(email)
                .password(password)
                .userName(username)
                .build();


        mockMvc.perform(
                        post("http://localhost:8080/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(dto))
                )
                .andDo(print())
                .andExpect(status().is(401));
        ;
    }

    @Test
    @Order(5)
    @DisplayName("회원 가입 테스트 - 예외 케이스 - 비밀번호 20자 초과")
    void 회원가입_컨트롤러_테스트_유효성검증4() throws Exception {
        String email = "rlawodbs1024_case4@gmail.com";
        String password = "";
        while(password.length() > 20){
            password += password + "1";
        }
        String username = "비밀번호 20자 초과";
        UserInfoJoinRequestDto dto = UserInfoJoinRequestDto.builder()
                .email(email)
                .password(password)
                .userName(username)
                .build();


        mockMvc.perform(
                        post("http://localhost:8080/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(dto))
                )
                .andDo(print())
                .andExpect(status().is(401));
        ;
    }

    @Test
    @Order(6)
    @DisplayName("로그아웃 테스트 ")
    void 로그아웃_테스트() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJybGF3b2RiczEwMjRAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOiJST0xFX1VTRVIiLCJwcmluY2lwYWwiOiJybGF3b2RiczEwMjRAZ21haWwuY29tIiwiaXNFbWFpbENlcnQiOnRydWUsImlzTG9jayI6ZmFsc2UsImlhdCI6MTczMjQwOTQxNiwiZXhwIjoxNzMyNDEzMDE2fQ.jjMA_Z3yX9wRMcGZRftSk2DByXqQlk1yqiu79UpWzdI";
        boolean isEnableToken = jwtTokenProvider.validateToken(token);
        assertTrue(isEnableToken);

        mockMvc.perform(
                        delete("http://localhost:8080/users/signout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                )
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(result -> {
                    String res = result.getResponse().getContentAsString();
                    CustomResponseDto dto = objectMapper.readValue(res, CustomResponseDto.class);
                    LinkedHashMap<String,Boolean> data = (LinkedHashMap<String,Boolean>) dto.getData();
                    assertTrue(data.get("success"));

                    assertFalse(jwtTokenProvider.validateToken(token));
                })
                ;
        ;

    }
}