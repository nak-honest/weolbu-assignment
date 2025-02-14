package weolbu.assignment.member.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import weolbu.assignment.global.infrastructure.JwtTokenProvider;
import weolbu.assignment.member.domain.EncryptedPassword;
import weolbu.assignment.member.domain.Member;
import weolbu.assignment.member.domain.MemberRepository;
import weolbu.assignment.member.domain.MemberRole;
import weolbu.assignment.member.domain.RawPassword;
import weolbu.assignment.member.dto.AccessTokenResponse;
import weolbu.assignment.member.dto.LoginRequest;
import weolbu.assignment.member.dto.SignUpRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ObjectMapper objectMapper;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("회원가입을 한다.")
    void signUpTest() throws Exception {
        // given
        String accessToken = "accesstoken";
        when(jwtTokenProvider.createAccessToken(any())).thenReturn(accessToken);
        SignUpRequest signUpRequest =
                new SignUpRequest("naknak", "abc@abc.com", "010-1234-5678", "pwd123", MemberRole.STUDENT);
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(accessToken);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signUpRequest)
                .when().post("/api/v1/members")
                .then().log().all()
                .statusCode(200)
                .body(is(objectMapper.writeValueAsString(accessTokenResponse)));
    }

    @Test
    @DisplayName("로그인을 한다.")
    void loginTest() throws Exception {
        // given
        Member member = saveMember();
        String accessToken = "accesstoken";
        when(jwtTokenProvider.createAccessToken(any())).thenReturn(accessToken);
        LoginRequest loginRequest = new LoginRequest("weolbu@abc.com", "abc123");
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(accessToken);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/api/v1/members/login")
                .then().log().all()
                .statusCode(200)
                .body(is(objectMapper.writeValueAsString(accessTokenResponse)));
    }

    private Member saveMember() {
        EncryptedPassword encryptedPassword = new RawPassword("abc123").encrypt(passwordEncoder);
        Member member = new Member(
                "weolbu", "weolbu@abc.com", "010-1111-1111", encryptedPassword, MemberRole.STUDENT);
        return memberRepository.save(member);
    }
}
