/**
 * Copyright 2014 Unicon (R)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ltistarter.controllers;

import ltistarter.BaseApplicationTest;
import ltistarter.lti.LTIRequest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "SpringJavaAutowiringInspection"})
public class AppControllersTest extends BaseApplicationTest {

    @Autowired
    private FilterChainProxy springSecurityFilter;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        assertNotNull(context);
        assertNotNull(springSecurityFilter);
        // Process mock annotations
        MockitoAnnotations.initMocks(this);
        // Setup Spring test in webapp-mode (same config as spring-boot)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(springSecurityFilter)
                .build();
        context.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
    }

    @Test
    public void testLoadRoot() throws Exception {
        // Test basic home controller request (no session, no user)
        MvcResult result = this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
        assertTrue(content.contains("Hello Spring Boot"));
        assertTrue(content.contains("Form Login endpoint"));
    }

    @Test
    public void testLoadRootWithAuth() throws Exception {
        // Test basic home controller request with a session and logged in user
        MockHttpSession session = makeAuthSession("azeckoski", "ROLE_USER");
        MvcResult result = this.mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
        assertTrue(content.contains("Hello Spring Boot"));
        assertTrue(content.contains("only shown to users (ROLE_USER)"));
    }

    @Test
    public void testLoadFormWithAuth() throws Exception {
        // Test form controller request with a session and logged in user
        MockHttpSession session = makeAuthSession("azeckoski", "ROLE_USER");
        MvcResult result = this.mockMvc.perform(get("/form").session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
        assertTrue(content.contains("Hello Spring Boot"));
        assertTrue(content.contains("only shown to users (ROLE_USER)"));
        assertTrue(content.contains("Logout")); // logout button
    }

    @Test
    @Ignore
    public void testLoadLTI() throws Exception {
        // test minimal LTI launch
        MockHttpSession session = makeAuthSession("azeckoski", "ROLE_LTI", "ROLE_OAUTH", "ROLE_USER");
        MvcResult result = this.mockMvc.perform(
                post("/lti1p").session(session)
                        .param(LTIRequest.LTI_VERSION, LTIRequest.LTI_VERSION_1P0)
                        .param(LTIRequest.LTI_MESSAGE_TYPE, LTIRequest.LTI_MESSAGE_TYPE_BASIC)
                        .param(LTIRequest.LTI_CONSUMER_KEY, "key")
                        .param(LTIRequest.LTI_LINK_ID, "Mylink")
                        .param(LTIRequest.LTI_CONTEXT_ID, "courseAZ")
                        .param(LTIRequest.LTI_USER_ID, "azeckoski")
        ).andExpect(status().isOk()).andReturn();
        assertNotNull(result);
        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
        assertTrue(content.contains("Hello Spring Boot"));
    }
}