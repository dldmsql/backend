package grooteogi;

import static grooteogi.ApiDocumentUtils.getDocumentRequest;
import static grooteogi.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import grooteogi.controller.UserController;
import grooteogi.domain.User;
import grooteogi.dto.user.PwDto;
import grooteogi.enums.LoginType;
import grooteogi.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(UserController.class)
public class UserDocumentationTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(
        documentationConfiguration(restDocumentation).operationPreprocessors()
            .withRequestDefaults(prettyPrint()).withResponseDefaults(prettyPrint())).build();
  }

  private User getTestUser() {
    User user = new User();
    user.setId(1);
    user.setType(LoginType.GENERAL);
    user.setEmail("groot@example.com");
    user.setPassword(passwordEncoder.encode("groot1234*"));
    user.setNickname("groot-1");
    return user;
  }

  @DisplayName("모든 회원정보 조회")
  @Test
  void getAllUser() throws Exception {

    User testUser = getTestUser();
    List<User> userList = new ArrayList<>();
    userList.add(testUser);
    given(userService.getAllUser()).willReturn(userList);

    //then
    ResultActions result = mockMvc.perform(
        RestDocumentationRequestBuilders.get("/user").characterEncoding("utf-8")
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk()).andDo(print()).andDo(
        document("get-all-user", getDocumentRequest(), getDocumentResponse(),
            responseFields(fieldWithPath("status").description("결과 코드"),
                fieldWithPath("count").description("리스트 카운트"),
                fieldWithPath("data.[].id").description("아이디"),
                fieldWithPath("data.[].type").description("타입"),
                fieldWithPath("data.[].nickname").description("닉네임"),
                fieldWithPath("data.[].password").type(JsonFieldType.STRING).description("패스워드"),
                fieldWithPath("data.[].email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("data.[].modified").type(JsonFieldType.STRING).description("수정날짜")
                    .optional(),
                fieldWithPath("data.[].registered").type(JsonFieldType.STRING).description("가입날짜")
                    .optional(),
                fieldWithPath("data.[].userHashtags").type(JsonFieldType.ARRAY).description("해시태그"),
                fieldWithPath("data.[].posts").type(JsonFieldType.ARRAY).description("포스트"),
                fieldWithPath("data.[].hostReserves").type(JsonFieldType.ARRAY).description("주최자")
                    .optional(),
                fieldWithPath("data.[].participateReserves").type(JsonFieldType.ARRAY)
                    .description("참가자").optional(),
                fieldWithPath("data.[].hearts").type(JsonFieldType.ARRAY).description("찜")
                    .optional())));
  }

  @DisplayName("회원정보 조회")
  @Test
  void getUser() throws Exception {
    int userId = anyInt();
    User testUser = getTestUser();

    given(userService.getUser(userId)).willReturn(testUser);

    ResultActions result = mockMvc.perform(
        RestDocumentationRequestBuilders.get("/user/{userId}", userId).characterEncoding("utf-8")
            .accept(MediaType.APPLICATION_JSON));
    result.andExpect(status().isOk()).andDo(print()).andDo(
        document("get-user", getDocumentRequest(), getDocumentResponse(),
            responseFields(fieldWithPath("status").description("결과 코드"),
                fieldWithPath("data.id").description("아이디"),
                fieldWithPath("data.type").description("타입"),
                fieldWithPath("data.nickname").description("닉네임"),
                fieldWithPath("data.password").type(JsonFieldType.STRING).description("패스워드"),
                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("data.modified").type(JsonFieldType.STRING).description("수정날짜")
                    .optional(),
                fieldWithPath("data.registered").type(JsonFieldType.STRING).description("가입날짜")
                    .optional(),
                fieldWithPath("data.userHashtags").type(JsonFieldType.ARRAY).description("해시태그"),
                fieldWithPath("data.posts").type(JsonFieldType.ARRAY).description("포스트"),
                fieldWithPath("data.hostReserves").type(JsonFieldType.ARRAY).description("주최자")
                    .optional(), fieldWithPath("data.participateReserves").type(JsonFieldType.ARRAY)
                    .description("참가자").optional(),
                fieldWithPath("data.hearts").type(JsonFieldType.ARRAY).description("찜")
                    .optional())));
  }

  @DisplayName("프로필 조회")
  @Test
  void getProfile() throws Exception {
    int userId = anyInt();
    User testUser = getTestUser();

    given(userService.getUser(userId)).willReturn(testUser);

    ResultActions result = mockMvc.perform(
        RestDocumentationRequestBuilders.get("/user/{userId}", userId).characterEncoding("utf-8")
            .accept(MediaType.APPLICATION_JSON));
    result.andExpect(status().isOk()).andDo(print()).andDo(
        document("get-user", getDocumentRequest(), getDocumentResponse(),
            responseFields(fieldWithPath("status").description("결과 코드"),
                fieldWithPath("data.id").description("아이디"),
                fieldWithPath("data.type").description("타입"),
                fieldWithPath("data.nickname").description("닉네임"),
                fieldWithPath("data.password").type(JsonFieldType.STRING).description("패스워드"),
                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("data.modified").type(JsonFieldType.STRING).description("수정날짜")
                    .optional(),
                fieldWithPath("data.registered").type(JsonFieldType.STRING).description("가입날짜")
                    .optional(),
                fieldWithPath("data.userInfo").type(JsonFieldType.STRING).description("추가정보")
                    .optional(),
                fieldWithPath("data.userHashtags").type(JsonFieldType.ARRAY).description("해시태그"),
                fieldWithPath("data.posts").type(JsonFieldType.ARRAY).description("포스트"),
                fieldWithPath("data.hostReserves").type(JsonFieldType.ARRAY).description("주최자")
                    .optional(), fieldWithPath("data.participateReserves").type(JsonFieldType.ARRAY)
                    .description("참가자").optional(),
                fieldWithPath("data.hearts").type(JsonFieldType.ARRAY).description("찜")
                    .optional())));
  }

  @DisplayName("비밀번호 변경")
  @Test
  void UpdatePassword() throws Exception {
    PwDto pwDto = new PwDto();
    pwDto.setPassword("groot1234*");
    int userId = anyInt();

    User testUser = getTestUser();
    given(userService.getUser(userId)).willReturn(testUser);

    ResultActions result = mockMvc.perform(
        RestDocumentationRequestBuilders.patch("/user/{userId}/password", userId)
            .characterEncoding("utf-8").content(objectMapper.writeValueAsString(pwDto))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk()).andDo(print()).andDo(
        document("update-password", getDocumentRequest(), getDocumentResponse(),
            responseFields(fieldWithPath("status").description("결과 코드"),
                fieldWithPath("message").description("메시지"))));
  }
}