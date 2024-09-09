package com.example.LibraryManagementSystem;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LibraryManagementSystem.class)
public class BaseTest {

    static final String AUTHORIZATION = "Authorization";
    static final String BEARER = "Bearer ";

    public String token = new String();

    @LocalServerPort
    protected int port;

    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11-alpine");

    static {
        postgreSQLContainer.start();
    }


    protected static RestClient restClient = RestClient.create();

    @BeforeEach
    public void setUp() {
        restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    /**
     * Performs an HTTP GET request to the specified endpoint and retrieves a single response of the specified type.
     *
     * @param uri          The path of the endpoint to send the GET request.
     * @param body         The object to be included in the request body. Can be null if the request does not require a body.
     * @param headers      Additional HTTP request headers (excluding the authentication token). Can be null if no additional headers are needed.
     * @param responseType The expected response type for the HTTP request. Use a concrete class (e.g., LoginResponse) but not supported for generic classes like Car<Model>.
     * @param <T>          The type of the expected response.
     * @return The response of the specified type received from the HTTP GET request.
     */
    public <T> T getOne(String uri, Object body, Map<String, String> headers, Class<T> responseType) {
        return findOne(uri, HttpMethod.GET, body, headers, responseType);
    }

    /**
     * Performs an HTTP GET request to the specified endpoint and retrieves a single response wrapped in a specified container type.
     *
     * @param uri          The URI of the endpoint to send the GET request to.
     * @param body         The request body, if applicable (use null if not needed).
     * @param headers      The headers to include in the HTTP request.
     * @param responseType The class representing the type of the expected response (e.g., LoginResponse).
     * @param wrapper      The class representing the container type that wraps the responseType (e.g., List).
     * @param <T>          The type of the result, determined by the wrapper type (e.g., List<LoginResponse> or RestPageImpl<LoginResponse> ...).
     * @param <E>          The element type of the response (e.g., LoginResponse).
     * @param <F>          The wrapper type of the responseType (e.g., List, String).
     * @return A response of the specified type, wrapped in the specified container type (e.g., List<LoginResponse>, RestPageImpl<LoginResponse> ...).
     */
    @SuppressWarnings("unchecked")
    public <T, E, F> T getList(String uri, Object body, Map<String, String> headers, Class<E> responseType, Class<F> wrapper) {
        return (T) findMany(uri, HttpMethod.GET, body, headers, responseType, wrapper);
    }

    public <T> T postAndExpectOne(String uri, Object body, Map<String, String> headers, Class<T> responseType) {
        return findOne(uri, HttpMethod.POST, body, headers, responseType);
    }

    public <T> ResponseEntity<Void> postAndExpectVoid(String uri, Object body, Map<String, String> headers) {
        return findNone(uri, HttpMethod.POST, body, headers);
    }

    @SuppressWarnings("unchecked")
    public <T, E, F> T postAndExpectList(String uri, Object body, Map<String, String> headers, Class<E> responseType, Class<F> wrapper) {
        return (T) findMany(uri, HttpMethod.POST, body, headers, responseType, wrapper);
    }


    public <T> T putAndExpectOne(String uri, Object body, Map<String, String> headers, Class<T> responseType) {
        return findOne(uri, HttpMethod.PUT, body, headers, responseType);
    }

    @SuppressWarnings("unchecked")
    public <T, E, F> T putAndExpectList(String uri, Object body, Map<String, String> headers, Class<E> responseType, Class<F> wrapper) {
        return (T) findMany(uri, HttpMethod.PUT, body, headers, responseType, wrapper);
    }

    public <T> ResponseEntity<Void> putAndExpectVoid(String uri, Object body, Map<String, String> headers) {
        return findNone(uri, HttpMethod.PUT, body, headers);
    }

    public <T> T patchAndExpectOne(String uri, Object body, Map<String, String> headers, Class<T> responseType) {
        return findOne(uri, HttpMethod.PATCH, body, headers, responseType);
    }

    @SuppressWarnings("unchecked")
    public <T, F> T patchAndExpectList(String uri, Object body, Map<String, String> headers, Class<T> responseType, Class<F> wrapper) {
        return (T) findMany(uri, HttpMethod.PATCH, body, headers, responseType, wrapper);
    }

    public <T> ResponseEntity<Void> patchAndExpectVoid(String uri, Object body, Map<String, String> headers) {
        return findNone(uri, HttpMethod.PATCH, body, headers);
    }

    public <T> T deleteAndExpectOne(String uri, Object body, Map<String, String> headers, Class<T> responseType) {
        return findOne(uri, HttpMethod.DELETE, body, headers, responseType);
    }

    @SuppressWarnings("unchecked")
    public <T, F> T deleteAndExpectList(String uri, Object body, Map<String, String> headers, Class<T> responseType, Class<F> wrapper) {
        return (T) findMany(uri, HttpMethod.DELETE, body, headers, responseType, wrapper);
    }

    public ResponseEntity<Void> deleteAndExpectVoid(String uri, Object body, Map<String, String> headers) {
        return findNone(uri, HttpMethod.DELETE, body, headers);
    }

    private RestClient.RequestBodySpec setBodyAndHeaders(RestClient.RequestBodySpec spec, Object body, Map<String, String> headers) {
        if (body != null) spec.body(body);
        if (headers != null) {
            spec.headers(httpHeaders -> httpHeaders.setAll(headers));
        }
        return spec;
    }


    public <T, E, F> T findMany(String uri, HttpMethod method, Object body, Map<String, String> headers, Class<E> type, Class<F> wrapper) {
        RestClient.RequestBodySpec request = restClient.method(method).uri(uri).contentType(MediaType.APPLICATION_JSON).header(AUTHORIZATION, BEARER + token);
        return setBodyAndHeaders(request, body, headers).retrieve().body(ParameterizedTypeReference.forType(ResolvableType.forClassWithGenerics(wrapper, type).getType()));
    }

    public <T> T findOne(String uri, HttpMethod method, Object body, Map<String, String> headers, Class<T> type) {
        RestClient.RequestBodySpec request = restClient.method(method).uri(uri).contentType(MediaType.APPLICATION_JSON).header(AUTHORIZATION, BEARER + token);
        return setBodyAndHeaders(request, body, headers).retrieve().body(type);
    }

    public <T> ResponseEntity<Void> findNone(String uri, HttpMethod method, Object body, Map<String, String> headers) {
        RestClient.RequestBodySpec request = restClient.method(method).uri(uri).contentType(MediaType.APPLICATION_JSON).header(AUTHORIZATION, BEARER + token);
        return setBodyAndHeaders(request, body, headers).retrieve().toBodilessEntity();
    }
}

