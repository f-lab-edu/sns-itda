package me.liiot.snsserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.liiot.snsserver.exception.InvalidValueException;
import me.liiot.snsserver.exception.NotUniqueIdException;
import me.liiot.snsserver.model.*;
import me.liiot.snsserver.service.LoginService;
import me.liiot.snsserver.service.UserService;
import me.liiot.snsserver.util.PasswordEncryptor;
import me.liiot.snsserver.util.SessionKeys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.sql.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    MockHttpSession mockHttpSession;

    @MockBean
    UserService userService;

    @MockBean
    LoginService loginService;

    static ObjectMapper mapper;

    User testUser;

    User encryptedTestUser;

    @BeforeAll
    public static void setUpAll() {
        mapper = new ObjectMapper();
    }

    @BeforeEach
    public void setUpEach() {
        testUser = User.builder()
                .userId("test1")
                .password("1234")
                .name("Sally")
                .phoneNumber("01012341234")
                .email("test1@test.com")
                .birth(Date.valueOf("1990-01-10"))
                .profileMessage("안녕!")
                .profileImageName("testImage")
                .profileImagePath("C:\\Users\\cyj19\\Desktop\\Project\\sns-server\\images\\testImage")
                .build();

        encryptedTestUser = User.builder()
                .userId("test1")
                .password(PasswordEncryptor.encrypt("1234"))
                .name("Sally")
                .phoneNumber("01012341234")
                .email("test1@test.com")
                .birth(Date.valueOf("1990-01-10"))
                .profileMessage("안녕!")
                .profileImageName("testImage")
                .profileImagePath("C:\\Users\\cyj19\\Desktop\\Project\\sns-server\\images\\testImage")
                .build();

        mockHttpSession = new MockHttpSession();
    }

    @Test
    public void signUpUserTest() throws Exception {

        UserSignUpParam userSignUpParam = UserSignUpParam.builder()
                .userId("test1")
                .password("1234")
                .name("Sally")
                .phoneNumber("01012341234")
                .email("test1@test.com")
                .birth(Date.valueOf("1990-01-10"))
                .build();

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userSignUpParam)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(userService).signUpUser(any(UserSignUpParam.class));
    }

    @Test
    public void checkUserIdDupeTestWithoutDupe() throws Exception {

        mockMvc.perform(get("/users/test1/exists"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).checkUserIdDupe("test1");
    }

    @Test
    public void checkUserIdDupeTestWithDupe() throws Exception {

        doThrow(NotUniqueIdException.class).when(userService).checkUserIdDupe("test1");

        mockMvc.perform(get("/users/test1/exists"))
                .andDo(print())
                .andExpect(status().isConflict());

        verify(userService).checkUserIdDupe("test1");
    }

    @Test
    public void loginUserTestWithSuccess() throws Exception {

        UserIdAndPassword userIdAndPassword = new UserIdAndPassword("test1", "1234");

        when(userService.getLoginUser(any(UserIdAndPassword.class))).thenReturn(encryptedTestUser);

        mockMvc.perform(
                post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userIdAndPassword)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).getLoginUser(any(UserIdAndPassword.class));
        verify(loginService).loginUser(encryptedTestUser);
    }

    @Test
    public void loginUserTestWithFail() throws Exception {

        UserIdAndPassword userIdAndPassword = new UserIdAndPassword("test2", "5678");

        when(userService.getLoginUser(any(UserIdAndPassword.class))).thenReturn(null);

        mockMvc.perform(
                post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userIdAndPassword)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(userService).getLoginUser(any(UserIdAndPassword.class));
        verify(loginService, times(0)).loginUser(any(User.class));
    }

    @Test
    public void logoutUserTest() throws Exception {

        mockMvc.perform(get("/users/logout"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(loginService).logoutUser();
    }

    @Test
    public void updateUserTestWithSuccess() throws Exception {

        mockHttpSession.setAttribute(SessionKeys.USER_ID, encryptedTestUser);

        MockMultipartFile testFile = new MockMultipartFile(
                "profileImage",
                "profileImage",
                "image/png",
                "profileImage".getBytes());

        UserUpdateParam userUpdateParam = UserUpdateParam.builder()
                .name("Sarah")
                .phoneNumber("01012345678")
                .email("test1@abc.com")
                .birth(Date.valueOf("1990-02-20"))
                .profileMessage("안녕하세요")
                .build();

        // multipart로 request를 보내면 요청 방식이 "POST"로 하드코딩되어 있어 이를 임시로 "PUT"으로 변경
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/users/my-account");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        mockMvc.perform(builder
                        .file(testFile)
                        .session(mockHttpSession)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(mapper.writeValueAsString(userUpdateParam)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).updateUser(eq(encryptedTestUser), any(UserUpdateParam.class), eq(testFile));
    }

    @Test
    public void updateUserPasswordTestWithSuccess() throws Exception {

        mockHttpSession.setAttribute(SessionKeys.USER_ID, encryptedTestUser);

        UserPasswordUpdateParam passwordUpdateParam = new UserPasswordUpdateParam(
                "1234", "5678", "5678");

        mockMvc.perform(
                put("/users/my-account/password")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passwordUpdateParam)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).updateUserPassword(eq(encryptedTestUser), any(UserPasswordUpdateParam.class));
        verify(loginService).logoutUser();
    }

    @Test
    public void updateUserPasswordTestWithFailAndInvalidValue() throws Exception {

        mockHttpSession.setAttribute(SessionKeys.USER_ID, encryptedTestUser);

        UserPasswordUpdateParam passwordUpdateParam = new UserPasswordUpdateParam(
                "5678", "1234", "6789");

        doThrow(InvalidValueException.class)
                .when(userService)
                .updateUserPassword(eq(encryptedTestUser), any(UserPasswordUpdateParam.class));

        mockMvc.perform(
                put("/users/my-account/password")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passwordUpdateParam)))
                .andDo(print())
                .andExpect(status().isConflict());

        verify(userService).updateUserPassword(eq(encryptedTestUser), any(UserPasswordUpdateParam.class));
        verify(loginService, times(0)).logoutUser();
    }

    @Test
    public void deleteUserTestWithSuccess() throws Exception {

        mockHttpSession.setAttribute(SessionKeys.USER_ID, encryptedTestUser);

        mockMvc.perform(
                delete("/users/my-account")
                        .session(mockHttpSession)
                        .param("password", "1234"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).deleteUser(encryptedTestUser, "1234");
        verify(loginService).logoutUser();
    }

    @Test
    public void deleteUserTestWithFailAndInvalidPassword() throws Exception {

        mockHttpSession.setAttribute(SessionKeys.USER_ID, encryptedTestUser);

        doThrow(InvalidValueException.class).when(userService).deleteUser(eq(encryptedTestUser), anyString());

        mockMvc.perform(
                delete("/users/my-account")
                        .session(mockHttpSession)
                        .param("password","5678"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(userService).deleteUser(encryptedTestUser, "5678");
        verify(loginService, times(0)).logoutUser();
    }
}