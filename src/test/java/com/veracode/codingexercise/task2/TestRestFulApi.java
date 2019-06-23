package com.veracode.codingexercise.task2;

import com.veracode.codingexercise.task1.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

public class TestRestFulApi {

    private static Logger logger = LoggerFactory.getLogger(TestRestFulApi.class);
    private static final String BASE_URI_STR = "http://localhost:8080";
    private static final List<String> userNameList = new ArrayList<>();

    private Map<String, User> userMap = new HashMap<>();

    static {
        userNameList.add("ironman");
        userNameList.add("captainamerica");
    }

    @Before
    public void setUp() {
        /*
         * Add User name ironman
         */
        final User userIronman = new User();
        userIronman.setUserName(userNameList.get(0));
        userIronman.setFirstName("Anthony");
        userIronman.setMiddleName("Edward");
        userIronman.setLastName("Stark");
        userMap.put(userNameList.get(0), userIronman);

        /*
         * Add User name captainamerica
         */
        final User userCaptainamerica = new User();
        userCaptainamerica.setUserName(userNameList.get(1));
        userCaptainamerica.setFirstName("Steven");
        userCaptainamerica.setMiddleName("");
        userCaptainamerica.setLastName("Rogers");
        userMap.put(userNameList.get(1), userCaptainamerica);

    }

    @Test
    public void testRestFulApi() {

        failIfTestDataExists();

        createUsers();
        readAllUsers();
        updateUser();
        deleteUsers();
    }

    private void failIfTestDataExists() {
        /*
         * Check if the test data currently exists
         */
        RestTemplate restTemplate = new RestTemplate();
        try {
            for(User user : userMap.values()) {
                try {
                    URI uri = new URI(BASE_URI_STR + "/users/" + user.getUserName());
                    User existingUser = restTemplate.getForObject(uri, User.class);
                    if(existingUser != null) {
                        fail("Test data with key " + user.getUserName() + " already exists");
                        System.exit(1);
                    }
                } catch (RestClientResponseException e) {
                    //continue
                }
            }
        } catch (URISyntaxException e) {
            fail(e.toString());
            System.exit(1);
        }
    }

    private void createUsers() {

        try {
            final URI uri = new URI(BASE_URI_STR + "/users");
            RestTemplate restTemplate = new RestTemplate();

            for(User user : userMap.values()) {
                restTemplate.postForLocation(uri, user);
            }
        } catch (URISyntaxException | RestClientResponseException e) {
            fail(e.toString());
        }
    }

    private void readAllUsers() {
        try {
            final URI uri = new URI(BASE_URI_STR + "/users");

            /*
             * Get the User with Traverson which traverses HAL JSON and follows the link to get each Object
             */
            Traverson traverson = new Traverson(uri, MediaTypes.HAL_JSON);
            PagedResources<Resource<User>> pagedResources = traverson
                    .follow("self")
                    .toObject(new TypeReferences.PagedResourcesType<Resource<User>>(){});
            assertThat(pagedResources.getContent(), is(not(nullValue())));
            assertThat(pagedResources.getContent().size(), is(greaterThanOrEqualTo(userMap.size())));
            /*
             * Go through all the users and verify all the known users are found
             */
            int usersFound = 0;
            for(Resource<User> resource : pagedResources) {
                User user = resource.getContent();
                if(userMap.containsKey(user.getUserName())) {
                    User verifyUser = userMap.get(user.getUserName());
                    if(user.equals(verifyUser)) {
                        usersFound++;
                    } else {
                        logger.debug("These users have same username but is a different person: " + user.toString() + " | " + verifyUser.toString());
                    }
                } else {
                    logger.debug("Following user does not have recognized username: " + user.toString());
                }
            }
            assertThat(usersFound, is(equalTo(userMap.size())));
        } catch (URISyntaxException e) {
            fail(e.toString());
        }
    }

    private void updateUser() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            User updatedUser = userMap.get(userNameList.get(1));
            updatedUser.setFirstName("James");
            updatedUser.setMiddleName("Buchanan");
            updatedUser.setLastName("Barnes");

            final URI uri = new URI(BASE_URI_STR + "/users/" + updatedUser.getUserName());

            //Update user
            restTemplate.put(uri, updatedUser);

            /*
             * Verify update
             */
            User verifyUser = restTemplate.getForObject(uri, User.class);
            assertThat(verifyUser.equals(updatedUser), is(true));


        } catch (URISyntaxException | RestClientResponseException e) {
            fail(e.toString());
        }
    }

    private void deleteUsers() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            for(String username : userNameList) {
                URI uri = new URI(BASE_URI_STR + "/users/" + username);
                restTemplate.delete(uri);
            }
        } catch (URISyntaxException | RestClientResponseException e) {
            fail(e.toString());
        }
    }

    @AfterClass
    public static void cleanUp() {
        // remove the users that have been added for this test
        RestTemplate restTemplate = new RestTemplate();

        for(String username : userNameList) {
            try {
                URI uri = new URI(BASE_URI_STR + "/users/" + username);
                restTemplate.delete(uri);
            } catch (URISyntaxException | RestClientResponseException e) {
                logger.debug(e.toString());
            }
        }
    }

}
