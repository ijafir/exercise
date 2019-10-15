package com.exercise.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.exercise.domain.Person;
import com.exercise.service.PersonService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebMvcTest(PersonController.class)
public class PersonControllerTest
{

    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Inject
    private MockMvc mockMvc;
    @Inject
    private WebApplicationContext context;
    @MockBean
    private PersonService service;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .defaultRequest(get("/").with(user("user").password("user").roles("USER"))).build();
    }

    @Test
    public void findObjects_StorageIsNotEmpty_OneObjectIsReturned() throws Exception {
        given(service.findAllPersons()).willReturn(Arrays.asList(new Person()));
        mockMvc
                .perform(get("/api/v1/persons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    public void savePerson_validPerson_PersonIsReturned() throws Exception
    {
        Person user = new Person("100", "testLastName", "testFirstName",
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test@test.com");
        mockMvc
                .perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(MAPPER.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.firstName", Matchers.equalTo("testFirstName")));

    }



    @Test
    public void savePerson_NotValidPerson_BadRequest_NoDob() throws Exception
    {
        Person personNoDob = new Person("100", "testLastName", "testFirstName",
                null, "test@test.com");

        mockMvc
                .perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(MAPPER.writeValueAsString(personNoDob)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void savePerson_NotValidPerson_BadRequest_NoId() throws Exception
    {
        Person personNoId = new Person(null, "testLastName", "testFirstName",
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test@test.com");

        mockMvc
                .perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(MAPPER.writeValueAsString(personNoId)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void savePerson_NotValidPerson_BadRequest_NoLastName() throws Exception
    {
        Person personNoLastName = new Person("100", null, "testFirstName",
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test@test.com");

        mockMvc
                .perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(MAPPER.writeValueAsString(personNoLastName)))
                .andExpect(status().isBadRequest());

    }
    @Test
    public void savePerson_NotValidPerson_BadRequest_NoFirstName() throws Exception
    {
        Person personNoFirstName = new Person("100", "test", null,
                new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test@test.com");

        mockMvc
                .perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(MAPPER.writeValueAsString(personNoFirstName)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePerson_validPerson_PersonIsReturned() throws Exception {
        Person p = new Person("2", "testLast", "testFirst", new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test@aol.com");
        mockMvc
                .perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(MAPPER.writeValueAsString(p)));

        Person p2 = new Person("2", "testLast2", "testFirst2", new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test2@aol.com");

        given(service.findPersonById("2")).willReturn(p);
        mockMvc
                .perform(put("/api/v1/persons/2").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(MAPPER.writeValueAsString(p2))).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.lastName", Matchers.equalTo("testLast2")));

    }

    @Test
    public void findPersonByID_ValidId_OneObjectIsReturned() throws Exception {
        Person p3 = new Person("3", "testLast3", "testFirst3", new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test3@aol.com");

        given(service.findPersonById("3")).willReturn(p3);
        given(service.isPersonExist(p3)).willReturn(true);
        mockMvc
                .perform(get("/api/v1/persons/3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo("3")));
    }

    @Test
    public void findPersonByID_NotValidId_NotFound() throws Exception {
        given(service.findPersonById("4")).willReturn(null);
        mockMvc
                .perform(get("/api/v1/persons/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteObjectById_NotValidId_NotFound() throws Exception {
        Person p5 = new Person("5", "testLast5", "testFirst5", new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-1"), "test5@aol.com");

        given(service.isPersonExist(p5)).willReturn(false);
        mockMvc
                .perform(delete("/api/v1/persons/5"))
                .andExpect(status().isNotFound());
    }
}
