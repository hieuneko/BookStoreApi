package com.phamhieu.bookapi.api.auth;

import com.phamhieu.bookapi.api.AbstractControllerTest;
import com.phamhieu.bookapi.domain.auth.JwtTokenService;
import com.phamhieu.bookapi.domain.auth.JwtUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static com.phamhieu.bookapi.fakes.AuthenticationFakes.buildAuthentication;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/auths";

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldLogin_Ok() throws Exception {
        final var auth = buildAuthentication();
        final var token = randomAlphabetic(3,10);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(jwtTokenService.generateToken((JwtUserDetails) auth.getPrincipal())).thenReturn(token);

        post(BASE_URL, auth)
                .andExpect(jsonPath("$.token").value(token));
    }
}