package io.swagger.client.api;

import org.junit.Before;
import org.junit.Test;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.model.CreditCard;
import io.swagger.client.model.PhoneNumber;
import io.swagger.client.model.User;

/**
 * API tests for CreateuserApi
 */
public class CreateuserApiTest {  
    private CreateuserApi api;

    @Before
	public void setUp() throws Exception {
    	ApiClient apiClient = new ApiClient();
    	apiClient.setBasePath("http://localhost:8080/server/SWArchi2018_3/designcraft/1.0.0");
    	api = new CreateuserApi(apiClient);
	}
    
    /**
     * Create user
     *
     * This can only be done by the logged in user.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void testCreateUserTest() throws ApiException {
        User body = new User();
        body.setEmail("sragent@gmail.com");
        body.setAddress("Seoul, Korea");
        CreditCard cc = new CreditCard();
        cc.setNumber("0000-0000-0000-0000");
        cc.setExpirationdate("1111-11-11");
        cc.setValidationcode("337");
        body.setCreditcard(cc);
        body.setPassword("aaaa");
        PhoneNumber response = api.createUser(body);
        
        System.out.println("response: " + response.toString());

        // TODO: test validations
    }
    
}
