package vaultweb.apigateway.exceptions.dto;

/**
 * Simple enum to represent different levels of exceptions that can occur
 *
 * @author Calvin Shio
 */
public enum DefaultExceptionLevels {
    // On failure to get-access-tokens and verification
    AUTHENTICATION_EXCEPTION,
    // when the request to other services times out
    TIMEOUT_EXCEPTION,
    // when the request to other services fails with an HTTP error eg 404, 500, etc
    HTTP_ERROR_EXCEPTION,
    // when payload or response payload could not be parsed
    PARSE_EXCEPTION,
    // default exception for any other errors
    DEFAULT_EXCEPTION,
}
