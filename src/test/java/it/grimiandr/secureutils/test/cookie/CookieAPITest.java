package it.grimiandr.secureutils.test.cookie;

import it.grimiandr.security.csrf.DomainValidation;
import it.grimiandr.security.csrf.DoubleSubmit;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

/**
 *
 */
@Test
public class CookieAPITest {

    private static final String SALT = "wzoHsv7)zrRyY8aWNEqR#";
    private static final String ALG = "AES";
    private static final String CIPHER = "AES/CBC/PKCS5Padding";

    public void shouldCreateSecureCookie() throws Exception {
        Cookie cookieToSend = new Cookie("csrf-cookie", "some-payload");
        Cookie secureCookieToSend = new DoubleSubmit(cookieToSend, SALT, ALG, CIPHER).createSecureCookie();
        Assert.assertNotNull(secureCookieToSend);
    }

    public static void main(String[] args) throws Exception {
        HttpServletRequest request = null;
        HttpServletResponse response = null;

        // cookie to send to the client (step 1)
        Cookie cookieToSend = new Cookie("csrf-cookie", "some-payload");
        Cookie secureCookieToSend = new DoubleSubmit(cookieToSend, SALT, ALG, CIPHER).createSecureCookie();

        // check on each request (step 2) if false, block the request
        boolean cookieMatchHeader = new DoubleSubmit(request, SALT, ALG, CIPHER).setRequestCookie("csrf-cookie")
                .cookieMatchHeader("csrf-header");

        new DomainValidation(new URL(""), request).checkOriginAndDomain();
    }

}
