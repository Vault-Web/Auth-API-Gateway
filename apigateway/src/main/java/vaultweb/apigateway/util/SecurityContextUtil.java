package vaultweb.apigateway.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import vaultweb.apigateway.exceptions.DefaultException;
import vaultweb.apigateway.exceptions.dto.DefaultExceptionLevels;

import reactor.core.publisher.Mono;

@Component
@SuppressWarnings("unused")
public class SecurityContextUtil {

  public Mono<String> getAuthenticatedUsername() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getName)
        .switchIfEmpty(
            Mono.error(
                new DefaultException(
                    "User not authenticated", DefaultExceptionLevels.AUTHENTICATION_EXCEPTION)));
  }

  public Mono<Authentication> getAuthentication() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .switchIfEmpty(
            Mono.error(
                new DefaultException(
                    "User not authenticated", DefaultExceptionLevels.AUTHENTICATION_EXCEPTION)));
  }
}
