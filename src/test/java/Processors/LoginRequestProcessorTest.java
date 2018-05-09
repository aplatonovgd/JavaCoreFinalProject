package Processors;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.*;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Objects.LoggedinUser;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.LoginRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.LoginRequestModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginRequestProcessorTest {


    static Properties props;
    static InputStream input = null;
    HttpServletRequest mockedReq = mock(HttpServletRequest.class);
    DBProcessor mockedDbProcessor = mock(DBProcessor.class);
    LoginRequestProcessor.LoggedInUserInfo mockedLoggedInUserInfo = new LoginRequestProcessor.LoggedInUserInfo();

    @BeforeAll
    public static void getProperties() throws IOException {
        props = new Properties();
        input = new FileInputStream("src/test/resources/authorizationRequestData.properties");
        props.load(input);
    }

    @Test
    @DisplayName("User session generated if user exist and has correct password and hashPassword is disabled")
    public void userSessionGeneratedHashPasswordDisabled() throws IOException, SQLException, NoSuchAlgorithmException, SessionNotFoundException, IncorrectNameOrPasswordException, InvalidEmailException, InvalidJsonException {
        setMockRequestBody(mockedReq, props.getProperty("AuthRequest_CorrectDataHash"));

        String mockedEmail = props.getProperty("AuthRequest_CorrectData_ExpectedEmail");
        String mockedHashedPassword = props.getProperty("AuthRequest_CorrectData_ExpectedPasswordHash");
        int mockedUserId = Integer.valueOf(props.getProperty("LoginOnly_AuthRequest_CorrectData_ExpectedUserId"));
        List<LoggedinUser> mockedList = mockLoginRequestModelList(mockedEmail,mockedHashedPassword, mockedUserId);

        when(mockedDbProcessor.getLoginRequestModelListByLogin(props.getProperty("AuthRequest_CorrectData_ExpectedEmail"))).thenReturn(mockedList);

        LoginRequestProcessor loginRequestProcessor = new LoginRequestProcessor(mockedReq, mockedDbProcessor,false, mockedLoggedInUserInfo);
        String actualSessionId = loginRequestProcessor.processRequest();
        UUID sessionID = UUID.fromString(actualSessionId);

        Assertions.assertTrue(sessionID.getClass().equals(UUID.class));
    }

    @Test
    @DisplayName("User session generated if user exist and has correct password and hashPassword is enabled")
    public void userSessionGeneratedHashPasswordEnabled() throws IOException, SQLException, NoSuchAlgorithmException, SessionNotFoundException, IncorrectNameOrPasswordException, InvalidEmailException,  InvalidJsonException {
        setMockRequestBody(mockedReq, props.getProperty("AuthRequest_CorrectDataNoHash"));

        String mockedEmail = props.getProperty("AuthRequest_CorrectData_ExpectedEmail");
        String mockedPassword = props.getProperty("AuthRequest_CorrectData_ExpectedPasswordNoHash");
        String mockedHashedPassword = RequestHelper.calculatePasswordHash(mockedPassword);
        int mockedUserId = Integer.valueOf(props.getProperty("LoginOnly_AuthRequest_CorrectData_ExpectedUserId"));
        List<LoggedinUser> mockedList = mockLoginRequestModelList(mockedEmail,mockedHashedPassword, mockedUserId);

        when(mockedDbProcessor.getLoginRequestModelListByLogin(props.getProperty("AuthRequest_CorrectData_ExpectedEmail"))).thenReturn(mockedList);

        LoginRequestProcessor loginRequestProcessor = new LoginRequestProcessor(mockedReq, mockedDbProcessor,true, mockedLoggedInUserInfo);
        String actualSessionId = loginRequestProcessor.processRequest();
        UUID sessionID = UUID.fromString(actualSessionId);

        Assertions.assertTrue(sessionID.getClass().equals(UUID.class));
    }

    @Test
    @DisplayName("User session added to user list if user exist and has correct password")
    public void userSessionAddedToUserList() throws IOException, SQLException, NoSuchAlgorithmException, SessionNotFoundException, IncorrectNameOrPasswordException, InvalidEmailException,  InvalidJsonException {
        setMockRequestBody(mockedReq, props.getProperty("AuthRequest_CorrectDataNoHash"));

        String mockedEmail = props.getProperty("AuthRequest_CorrectData_ExpectedEmail");
        String mockedPassword = props.getProperty("AuthRequest_CorrectData_ExpectedPasswordNoHash");
        String mockedHashedPassword = RequestHelper.calculatePasswordHash(mockedPassword);
        int mockedUserId = Integer.valueOf(props.getProperty("LoginOnly_AuthRequest_CorrectData_ExpectedUserId"));
        List<LoggedinUser> mockedList = mockLoginRequestModelList(mockedEmail,mockedHashedPassword, mockedUserId);

        when(mockedDbProcessor.getLoginRequestModelListByLogin(props.getProperty("AuthRequest_CorrectData_ExpectedEmail"))).thenReturn(mockedList);

        LoginRequestProcessor loginRequestProcessor = new LoginRequestProcessor(mockedReq, mockedDbProcessor,true, mockedLoggedInUserInfo);
        String actualSessionId = loginRequestProcessor.processRequest();
        LoggedinUser user = mockedLoggedInUserInfo.getUserBySessionId(actualSessionId);

        assertEquals(mockedEmail,user.getUserEmail());
    }

    @Test
    @DisplayName("Correct session created time added to user list if user exist and has correct password")
    public void sessionCreatedTimeAddedToUserList() throws IOException, SQLException, NoSuchAlgorithmException, SessionNotFoundException, IncorrectNameOrPasswordException, InvalidEmailException, InvalidJsonException {
        setMockRequestBody(mockedReq, props.getProperty("AuthRequest_CorrectDataNoHash"));

        String mockedEmail = props.getProperty("AuthRequest_CorrectData_ExpectedEmail");
        String mockedPassword = props.getProperty("AuthRequest_CorrectData_ExpectedPasswordNoHash");
        String mockedHashedPassword = RequestHelper.calculatePasswordHash(mockedPassword);
        int mockedUserId = Integer.valueOf(props.getProperty("LoginOnly_AuthRequest_CorrectData_ExpectedUserId"));
        List<LoggedinUser> mockedList = mockLoginRequestModelList(mockedEmail,mockedHashedPassword, mockedUserId);

        when(mockedDbProcessor.getLoginRequestModelListByLogin(props.getProperty("AuthRequest_CorrectData_ExpectedEmail"))).thenReturn(mockedList);

        long dateTimeBeforeMethodCall = RequestHelper.getCreationTimeMillis();
        LoginRequestProcessor loginRequestProcessor = new LoginRequestProcessor(mockedReq, mockedDbProcessor,true, mockedLoggedInUserInfo);
        String actualSessionId = loginRequestProcessor.processRequest();
        LoggedinUser user = mockedLoggedInUserInfo.getUserBySessionId(actualSessionId);
        long dateTimeAfterMethodCall = RequestHelper.getCreationTimeMillis();

        Assertions.assertTrue(user.getSessionCreatedTime() >= dateTimeBeforeMethodCall);
        Assertions.assertTrue(user.getSessionCreatedTime() <= dateTimeAfterMethodCall);
    }

    @Test
    @DisplayName("When user doesn't exist exception is thrown")
    public void whenUserDoesntExistExceptionThrows() throws IOException, SQLException {
        setMockRequestBody(mockedReq, props.getProperty("AuthRequest_CorrectDataNoHash"));

        when(mockedDbProcessor.getLoginRequestModelListByLogin(props.getProperty("AuthRequest_CorrectData_ExpectedEmail"))).thenReturn(new ArrayList<>());

        LoginRequestProcessor loginRequestProcessor = new LoginRequestProcessor(mockedReq, mockedDbProcessor,true, mockedLoggedInUserInfo);

        assertThrows(IncorrectNameOrPasswordException.class,loginRequestProcessor::processRequest);
    }

    @Test
    @DisplayName("When user password is incorrect exception is thrown")
    public void incorrectPasswordExcpetionThrown() throws IOException, SQLException, NoSuchAlgorithmException{
        setMockRequestBody(mockedReq, props.getProperty("AuthRequest_CorrectDataHash"));

        String mockedEmail = props.getProperty("AuthRequest_CorrectData_ExpectedEmail");
        String mockedPassword = props.getProperty("AuthRequest_CorrectData_ExpectedIncorrectPasswordNoHash");
        String mockedHashedPassword = RequestHelper.calculatePasswordHash(mockedPassword);
        int mockedUserId = Integer.valueOf(props.getProperty("LoginOnly_AuthRequest_CorrectData_ExpectedUserId"));
        List<LoggedinUser> mockedList = mockLoginRequestModelList(mockedEmail,mockedHashedPassword, mockedUserId);

        when(mockedDbProcessor.getLoginRequestModelListByLogin(props.getProperty("AuthRequest_CorrectData_ExpectedEmail"))).thenReturn(mockedList);

        LoginRequestProcessor loginRequestProcessor = new LoginRequestProcessor(mockedReq, mockedDbProcessor,false, mockedLoggedInUserInfo);

        assertThrows(IncorrectNameOrPasswordException.class,loginRequestProcessor::processRequest);
    }


    @Test
    @DisplayName("When user email without 'at'(@) exception is thrown")
    public void emailWithoutAtExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_EmailWithoutAt"));
        when(mockedDbProcessor.getLoginRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());

        LoginRequestProcessor loginRequestProcessor = new LoginRequestProcessor(mockedReq, mockedDbProcessor,true, mockedLoggedInUserInfo);

        assertThrows(InvalidEmailException.class, loginRequestProcessor::processRequest);
    }

    @Test
    @DisplayName("When user email without 'dot'(.) exception is thrown")
    public void emailWithoutDotExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_EmailWithoutDot"));
        when(mockedDbProcessor.getLoginRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());

        LoginRequestProcessor loginRequestProcessor = new LoginRequestProcessor(mockedReq, mockedDbProcessor,true, mockedLoggedInUserInfo);

        assertThrows(InvalidEmailException.class, loginRequestProcessor::processRequest);
    }

    @Test
    @DisplayName("When password is empty exception is thrown")
    public void emptyPasswordExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_EmptyPassword"));
        when(mockedDbProcessor.getLoginRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());

        LoginRequestProcessor loginRequestProcessor = new LoginRequestProcessor(mockedReq, mockedDbProcessor,true, mockedLoggedInUserInfo);

        assertThrows(IncorrectNameOrPasswordException.class, loginRequestProcessor::processRequest);
    }

    @Test
    @DisplayName("When request is incorrect InvalidJsonException is thrown")
    public void requestIncorrectInvalidJsonExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_InvalidJson"));
        when(mockedDbProcessor.getLoginRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());

        LoginRequestProcessor loginRequestProcessor = new LoginRequestProcessor(mockedReq, mockedDbProcessor,true, mockedLoggedInUserInfo);

        assertThrows(InvalidJsonException.class, loginRequestProcessor::processRequest);
    }

    @Test
    @DisplayName("When json has incorrect field exception is thrown")
    public void incorrectRequestFieldsJsonFieldExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_IncorrectFieldsJson"));
        when(mockedDbProcessor.getLoginRequestModelListByLogin(props.getProperty("AuthRequest_CorrectData_ExpectedEmail"))).thenReturn(new ArrayList<>());

        LoginRequestProcessor loginRequestProcessor = new LoginRequestProcessor(mockedReq, mockedDbProcessor,true, mockedLoggedInUserInfo);

        assertThrows(InvalidJsonException.class, loginRequestProcessor::processRequest);
    }




    public void setMockRequestBody(HttpServletRequest mockedReq, String mockedRequestBody) throws IOException {
        when(mockedReq.getReader()).thenReturn(new BufferedReader(new StringReader(mockedRequestBody)));
    }

    public List<LoggedinUser> mockLoginRequestModelList(String mockedEmail, String mockedPasswordHash, int userId){

        List<LoggedinUser> userList = new ArrayList<>();

        LoggedinUser loggedinUser = new LoggedinUser(userId, mockedEmail, mockedPasswordHash);

        userList.add(loggedinUser);

        return userList;
    }

}
