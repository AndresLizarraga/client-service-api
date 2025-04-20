package com.pinapp.clientservice.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pinapp.clientservice.testdummy.DummyTestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DummyTestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControllerUtilsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldBuildUriWithId() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().string("ok"));
    }
}
