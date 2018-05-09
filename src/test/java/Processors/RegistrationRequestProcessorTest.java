package Processors;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidEmailException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidJsonException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.InvalidPasswordException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.UserExistException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther.RegistrationRequestProcessor;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.RegisterRequestModel;

import org.junit.jupiter.api.BeforeAll;
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
        input = new FileInputStream("src/test/resources/registrationRequestData.properties");
        props.load(input);
    }


    @Test
    public void whenUserIsAbsentCorrectEmailToDatabase() throws IOException, InvalidEmailException, InvalidPasswordException, NoSuchAlgorithmException, SQLException, InvalidJsonException, UserExistException {

        setMockRequestBody(mockedReq,props.getProperty("RegisterRequest_CorrectData"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(registerReqArgCapture.capture());

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);
        registrationRequestProcessor.processRequest();

        assertEquals(props.getProperty("RegisterRequest_CorrectData_ExpectedEmail"), registerReqArgCapture.getValue().getEmail());
    }

    @Test
    public void whenUserIsAbsentCorrectPasswordHashToDatabase() throws IOException, InvalidEmailException, InvalidPasswordException, NoSuchAlgorithmException, SQLException, InvalidJsonException, UserExistException {

        setMockRequestBody(mockedReq,props.getProperty("RegisterRequest_CorrectData"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(registerReqArgCapture.capture());

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);
        registrationRequestProcessor.processRequest();

        String expectedPassHash = RequestHelper.calculatePasswordHash(props.getProperty("RegisterRequest_CorrectData_ExpectedPasswordNoHash"));
        assertEquals(expectedPassHash, registerReqArgCapture.getValue().getPassword());
    }

    @Test
    public void whenUserIsAbsentCorrectPasswordToDatabase() throws IOException, InvalidEmailException, InvalidPasswordException, NoSuchAlgorithmException, SQLException, InvalidJsonException, UserExistException {

        setMockRequestBody(mockedReq,props.getProperty("RegisterRequest_CorrectData"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(registerReqArgCapture.capture());

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, false);
        registrationRequestProcessor.processRequest();

        assertEquals(props.getProperty("RegisterRequest_CorrectData_ExpectedPasswordNoHash"), registerReqArgCapture.getValue().getPassword());
    }

    @Test
    public void whenUserEmailWithoutAtExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("RegisterRequest_EmailWithoutAt"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(isA(RegisterRequestModel.class));

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);

        assertThrows(InvalidEmailException.class, registrationRequestProcessor::processRequest);
    }

    @Test
    public void whenUserEmailWithoutDotExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("RegisterRequest_EmailWithoutDot"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(isA(RegisterRequestModel.class));

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);

        assertThrows(InvalidEmailException.class, registrationRequestProcessor::processRequest);
    }

    @Test
    public void whenUserHasEmptyPasswordExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("RegisterRequest_EmptyPassword"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(isA(RegisterRequestModel.class));

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);

        assertThrows(InvalidPasswordException.class, registrationRequestProcessor::processRequest);
    }

    @Test
    public void whenUserInvalidJsonExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("RegisterRequest_InvalidJson"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(isA(RegisterRequestModel.class));

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);

        assertThrows(InvalidJsonException.class, registrationRequestProcessor::processRequest);
    }

    @Test
    public void whenUserIncorrectJsonFieldExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("RegisterRequest_IncorrectFieldsJson"));
        when(mockedDbProcessor.getRegisterRequestModelListByLogin(isA(String.class))).thenReturn(new ArrayList<>());
        doNothing().when(mockedDbProcessor).addUserToDB(isA(RegisterRequestModel.class));

        RegistrationRequestProcessor registrationRequestProcessor =
                new RegistrationRequestProcessor(mockedReq, mockedDbProcessor, true);

        assertThrows(InvalidJsonException.class, registrationRequestProcessor::processRequest);
    }

    @Test
    public void whenUserExistExceptionThrown() throws IOException, SQLException {

        setMockRequestBody(mockedReq,props.getProperty("RegisterRequest_CorrectData"));

        //We expect List with user. So we need to mock our list
        String mockedEmail = props.getProperty("RegisterRequest_CorrectData_ExpectedEmail");
        String mockedPassword = props.getProperty("RegisterRequest_CorrectData_ExpectedPasswordNoHash");
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
