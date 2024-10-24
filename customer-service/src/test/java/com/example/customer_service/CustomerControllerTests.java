package com.example.customer_service;

import com.example.customer_service.dto.CustomerInformation;
import com.example.customer_service.dto.CustomerRequest;
import com.example.customer_service.dto.CustomerResponse;
import com.example.customer_service.dto.PaginatedCustomerInformationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CustomerControllerTests extends AbstractTest{


	public WebTestClient.ResponseSpec findById(Integer customerId, HttpStatus expectedStatus) {
		return client
				.get()
				.uri("/customers/{customerId}",customerId)
				.exchange()
				.expectStatus().isEqualTo(expectedStatus);
	}

	public WebTestClient.ResponseSpec addCustomer(CustomerRequest request, HttpStatus expectedStatus) {
		return client
				.post()
				.uri("/customers")
				.bodyValue(request)
				.exchange()
				.expectStatus().isEqualTo(expectedStatus);
	}

	public WebTestClient.ResponseSpec deleteCustomer(Integer customerId, HttpStatus expectedStatus) {
		return client
				.delete()
				.uri("/customers/{customerId}",customerId)
				.exchange()
				.expectStatus().isEqualTo(expectedStatus);
	}

	public WebTestClient.ResponseSpec paginated(Integer page, Integer size, String sort, HttpStatus expectedStatus) {
		return client
				.get()
				.uri("/customers")
				.exchange()
				.expectStatus().isEqualTo(expectedStatus);
	}


	@Test
	void testValidCustomerId(){
		findById(1,HttpStatus.OK)
				.returnResult(CustomerInformation.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(customerInformation -> {
					assertEquals("Alice",customerInformation.name());
					assertEquals(5000,customerInformation.balance());
					assertEquals("123 Main St",customerInformation.shippingAddress());
					assertTrue(customerInformation.orderSummaries().isEmpty());

				})
				.verifyComplete();
	}

	@Test
	void testInvalidCustomerId(){
		findById(25,HttpStatus.NOT_FOUND)
				.returnResult(ProblemDetail.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(problemDetail -> {
					assertEquals(HttpStatus.NOT_FOUND.value(),problemDetail.getStatus());
					assertEquals("Customer not found",problemDetail.getTitle());
					assertEquals("Customer with id 25 not found",problemDetail.getDetail());
					assertEquals(URI.create("http://example.com/problems/customer-not-found"),problemDetail.getType());
				})
				.verifyComplete();
	}

	@Test
	void testInvalidDelete(){
		deleteCustomer(25,HttpStatus.NOT_FOUND)
				.returnResult(ProblemDetail.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(problemDetail -> {
					assertEquals(HttpStatus.NOT_FOUND.value(),problemDetail.getStatus());
					assertEquals("Customer not found",problemDetail.getTitle());
					assertEquals("Customer with id 25 not found",problemDetail.getDetail());
					assertEquals(URI.create("http://example.com/problems/customer-not-found"),problemDetail.getType());
				})
				.verifyComplete();
	}

	@Test
	void testDefaultPaginated(){
		paginated(null, null, null, HttpStatus.OK)
				.returnResult(PaginatedCustomerInformationResponse.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(response -> {
					var customerInformationList = response.customerInformation();
					assertEquals(3L, response.count());
					assertEquals("Alice", customerInformationList.getFirst().name());
					assertEquals("Bob", customerInformationList.get(1).name());
					assertEquals("Charlie", customerInformationList.getLast().name());
				})
				.verifyComplete();
	}

	@Test
	void testNonDefaultPaginated(){
		paginated(1, 1, "name", HttpStatus.OK)
				.returnResult(PaginatedCustomerInformationResponse.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(response -> {
					assertEquals(3L, response.count());
					assertEquals("Alice", response.customerInformation().getFirst().name());
				})
				.verifyComplete();
	}

	@Test
	void testValidPostAndDeleteRequest(){
		var request = CustomerRequest.builder()
				.balance(5500)
				.name("Sara")
				.shippingAddress("Sidi Youssef Ben Ali")
				.build();

		addCustomer(request,HttpStatus.OK)
				.returnResult(CustomerResponse.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(response -> {
					assertEquals(4,response.customerId());
					assertEquals("Sara",response.name());
					assertEquals("Sidi Youssef Ben Ali",response.shippingAddress());
					assertEquals(5500,response.balance());
				})
				.verifyComplete();

		deleteCustomer(4,HttpStatus.OK)
				.returnResult(Void.class)
				.getResponseBody()
				.then()
				.as(StepVerifier::create)
				.verifyComplete();
	}

	@Test
	void addAlreadyExistsCustomer(){
		var request = CustomerRequest.builder()
				.balance(5500)
				.name("Alice")
				.shippingAddress("Sidi Youssef Ben Ali")
				.build();

		addCustomer(request,HttpStatus.BAD_REQUEST)
				.returnResult(ProblemDetail.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(problemDetail -> {
					assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
					assertEquals("Customer already exists",problemDetail.getTitle());
					assertEquals("Customer Alice already exists",problemDetail.getDetail());
					assertEquals(URI.create("http://example.com/problems/customer-already-exists"),problemDetail.getType());
				})
				.verifyComplete();
	}

	@Test
	void testNullCustomerNamePostRequest(){
		var request = CustomerRequest.builder()
				.balance(5500)
				.shippingAddress("Sidi Youssef Ben Ali")
				.build();

		addCustomer(request,HttpStatus.BAD_REQUEST)
				.returnResult(ProblemDetail.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(problemDetail -> {
					assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
					assertEquals("Customer request is invalid",problemDetail.getTitle());
					assertEquals("Name is required",problemDetail.getDetail());
					assertEquals(URI.create("http://example.com/problems/invalid-customer-request"),problemDetail.getType());
				})
				.verifyComplete();
	}

	@Test
	void testNullCustomerBalancePostRequest(){
		var request = CustomerRequest.builder()
				.name("Hamid")
				.shippingAddress("Sidi Youssef Ben Ali")
				.build();

		addCustomer(request,HttpStatus.BAD_REQUEST)
				.returnResult(ProblemDetail.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(problemDetail -> {
					assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
					assertEquals("Customer request is invalid",problemDetail.getTitle());
					assertEquals("Balance is required",problemDetail.getDetail());
					assertEquals(URI.create("http://example.com/problems/invalid-customer-request"),problemDetail.getType());
				})
				.verifyComplete();
	}

	@Test
	void testNullCustomerAddressPostRequest(){
		var request = CustomerRequest.builder()
				.balance(5500)
				.name("Hamid")
				.build();

		addCustomer(request,HttpStatus.BAD_REQUEST)
				.returnResult(ProblemDetail.class)
				.getResponseBody()
				.as(StepVerifier::create)
				.assertNext(problemDetail -> {
					assertEquals(HttpStatus.BAD_REQUEST.value(),problemDetail.getStatus());
					assertEquals("Customer request is invalid",problemDetail.getTitle());
					assertEquals("Shipping address is required",problemDetail.getDetail());
					assertEquals(URI.create("http://example.com/problems/invalid-customer-request"),problemDetail.getType());
				})
				.verifyComplete();
	}



}
