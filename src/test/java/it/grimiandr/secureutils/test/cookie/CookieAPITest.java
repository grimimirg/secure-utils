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
 * @author andre
 */
@Test
public class CookieAPITest {

    private static final String SECRET = "4CF2F8C0B4F74DA54E55D22AC1BEA541C91D43643F14B41A7B9553126C6C9B1F";
    private static final String KEY = "VkYp3s6v9y$B&E)H@McQfTjWmZq4t7w!";
    private static final String SALT = "wzoHsv7)zrRyY8aWNEqR#";
    private static final String ALG = "AES";
    private static final String CIPHER = "AES/CBC/PKCS5Padding";

    private static final int EXPIRATION_DAYS_TOKEN = 10;
    private static final int EXPIRATION_DAYS_REFRESH_TOKEN = 0;

    private static final String IDENTIFIER = "grimiandr@protonmail.ch";
    private static final String USERNAME = "grimiandr@protonmail.ch";
    private static final String PASSWORD = "123456";

    private static final String TOKEN = "UjIgC3Tq6DFQ9pIC59kvqdaCPCX9L4pPDsHUnwN/Yq2xLTKLHF4Gwpqto8nKkq7ut/vY5s0+ViPs0sqfkCAX/KTX7GpAuV/iIB7etUx4PUYeK2I9CkznfZR3TdTafQTZYLz9gm8ptu/GKCoBzer4zOqsQdxd1pjQO0x6V2LwV8qKs6ROvhKXYf3yshR00UR7tlGRXn01LfMAPjSbXOIU0tC0ulnfhYcdP8Pe0iGgluai1Rl+U8kuyY6Kutm2XcrBHvLlyseNrUx0dWZujaEyZTnA+lxp9BiYmP0O+Hd/FOQ4rIW6HuADnTVsO3SOVy42";
    private static final String TOKEN_INVALID = "UjIgC3Tq6DFQ9pIC59kvqdaCPCX9L4pPDsHUnwN/Yq2xLTKLHF4Gwdgrtereterkq7ut/vY5s0+ViPs0sqfkCAX/KTX7GpAuV/iIB7etUx4PUYeK2I9CkznfZR3TdTafQTZYLz9gm8ptu/GKCoBzer4zOqsQdxd1pjQO0x6V2LwV8qKs6ROvhKXYf3yshR00UR7tlGRXn01LfMAPjSbXOIU0tC0ulnfhYcdP8Pe0iGgluai1Rl+U8kuyY6Kutm2XcrBHvLlyseNrUx0dWZujaEyZTnA+lxp9BiYmP0O+Hd/FOQ4rIW6HuADnTVsO3SOVy42";
    private static final String TOKEN_EXPIRED = "UjIgC3Tq6DFQ9pIC59kvqdaCPCX9L4pPDsHUnwN/Yq2xLTKLHF4Gwpqto8nKkq7ut/vY5s0+ViPs0sqfkCAX/KTX7GpAuV/iIB7etUx4PUYFGxLMnzwe+7o8QXzsTw2WlX75tdqu6NYtXNy26bXs+c5/d3wgv1SnVsFX7G2ocoe7KNXxOeqDLKWsl+zHJHS7GKrpRPpuVYnFDW3J2qf6g8jEv0twN8m/6z/LwUXtC9D+gR4EBZ/6o3csu43a4BjG/wvtfdobx08SWwjlcuntlrjoq6bGwaA042+wiAKoWbeclBUB0aHErHJOZ3OFQdo5";
    private static final String TOKEN_WITH_REFRESH = "UjIgC3Tq6DFQ9pIC59kvqdaCPCX9L4pPDsHUnwN/Yq2xLTKLHF4Gwpqto8nKkq7ut/vY5s0+ViPs0sqfkCAX/KTX7GpAuV/iIB7etUx4PUakLIO9GpKx/J6laGiLSgpGEtvYZLPLb2+53pCNwcD+Pe0Kpiumw6a4viODYZxr703hyGv/Yl7w99ACdEJgvTPi9+HOVIdr/SGDs6C2R8CW+Y2XLHBJCnPFgR4g1QbBwdIjMuwTJPDagBL6+3G8njhedHcG4rUC9bAzMFXajfzO/s8JSmUjXSCcfzCBQSwK6JborDNEEKFUEs8ffAQ+9B74";

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
