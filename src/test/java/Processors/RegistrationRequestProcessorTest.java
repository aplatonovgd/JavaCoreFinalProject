package Processors;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.IncorrectNameOrPasswordException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidEmailException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.UserExistException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.RegistrationRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.RegisterRequestModel;

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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class RegistrationRequestProcessorTest {


    static Properties props;
    static InputStream input = null;
    HttpServletRequest mockedReq = mock(HttpServletRequest.class);
    DBProcessor mockedDbProcessor = mock(DBProcessor.class);
    ArgumentCaptor<RegisterRequestModel> registerReqArgCapture = ArgumentCaptor.forClass(RegisterRequestModel.class);

    @BeforeAll
    public static void getProperties() throws IOException {
        props = new Properties();
        input = new FileInputStream("src/test/resources/authorizationRequestData.properties");
        props.load(input);
    }


    @Test
    @DisplayName("When user is absent correct email sent to database")
    public void CorrectEmailSentToDatabase() throws IOException, InvalidEmailException, NoSuchAlgorithmException, SQLException, InvalidJsonException, UserExistException, IncorrectNameOrPasswordException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_CorrectDataNoHash"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(registerReqArgCapture.capture());

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);
        registrationRequestProcessor.processRequest();

        assertEquals(props.getProperty("AuthRequest_CorrectData_ExpectedEmail"), registerReqArgCapture.getValue().getEmail());
    }

    @Test
    @DisplayName("When user is absent correct password hash sent to database")
    public void correctPasswordHashSentToDatabase() throws IOException, InvalidEmailException, NoSuchAlgorithmException, SQLException, InvalidJsonException, UserExistException, IncorrectNameOrPasswordException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_CorrectDataNoHash"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(registerReqArgCapture.capture());

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);
        registrationRequestProcessor.processRequest();

        String expectedPassHash = RequestHelper.calculatePasswordHash(props.getProperty("AuthRequest_CorrectData_ExpectedPasswordNoHash"));
        assertEquals(expectedPassHash, registerReqArgCapture.getValue().getPassword());
    }

    @Test
    @DisplayName("When user is absent correct password sent to database")
    public void correctPasswordSentToDatabase() throws IOException, InvalidEmailException, NoSuchAlgorithmException, SQLException, InvalidJsonException, UserExistException, IncorrectNameOrPasswordException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_CorrectDataNoHash"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(registerReqArgCapture.capture());

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, false);
        registrationRequestProcessor.processRequest();

        assertEquals(props.getProperty("AuthRequest_CorrectData_ExpectedPasswordNoHash"), registerReqArgCapture.getValue().getPassword());
    }

    @Test
    @DisplayName("When user email without 'at'(@) exception is thrown")
    public void emailWithoutAtExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_EmailWithoutAt"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(isA(RegisterRequestModel.class));

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);

        assertThrows(InvalidEmailException.class, registrationRequestProcessor::processRequest);
    }

    @Test
    @DisplayName("When user email without 'dot'(.) exception is thrown")
    public void emailWithoutDotExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_EmailWithoutDot"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(isA(RegisterRequestModel.class));

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);

        assertThrows(InvalidEmailException.class, registrationRequestProcessor::processRequest);
    }

    @Test
    @DisplayName("When password is empty exception is thrown")
    public void emptyPasswordExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_EmptyPassword"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(isA(RegisterRequestModel.class));

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);

        assertThrows(IncorrectNameOrPasswordException.class, registrationRequestProcessor::processRequest);
    }

    @Test
    @DisplayName("When request is incorrect InvalidJsonException is thrown")
    public void requestIncorrectInvalidJsonExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_InvalidJson"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(isA(RegisterRequestModel.class));

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);

        assertThrows(InvalidJsonException.class, registrationRequestProcessor::processRequest);
    }

    @Test
    @DisplayName("When json has incorrect field exception is thrown")
    public void incorrectRequestFieldsJsonFieldExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_IncorrectFieldsJson"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(isA(RegisterRequestModel.class));

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);

        assertThrows(InvalidJsonException.class, registrationRequestProcessor::processRequest);
    }

    @Test
    @DisplayName("When user is already exist exception is thrown")
    public void userExistExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("AuthRequest_CorrectDataNoHash"));

        //We expect List with user. So we need to mock our list
        String mockedEmail = props.getProperty("AuthRequest_CorrectData_ExpectedEmail");
        String mockedPassword = props.getProperty("AuthRequest_CorrectData_ExpectedPasswordNoHash");
        List<RegisterRequestModel> mockedList = mockRegisterRequestModelList(mockedEmail,mockedPassword);

        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(mockedList);

        doNothing().when(mockedDbProcessor).addUserToDB(isA(RegisterRequestModel.class));

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);

        assertThrows(UserExistException.class, registrationRequestProcessor::processRequest);
    }


    public void setMockRequestBody(HttpServletRequest mockedReq, String mockedRequestBody) throws IOException {
        when(mockedReq.getReader()).thenReturn(new BufferedReader(new StringReader(mockedRequestBody)));
    }

    public List<RegisterRequestModel> mockRegisterRequestModelList(String mockedEmail, String mockedPassword){

        List<RegisterRequestModel> userList = new ArrayList<>();

        RegisterRequestModel registerRequestModel = new RegisterRequestModel(mockedEmail,mockedPassword);

        userList.add(registerRequestModel);

        return userList;
    }

}
