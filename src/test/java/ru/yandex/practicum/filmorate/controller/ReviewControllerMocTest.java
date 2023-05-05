package ru.yandex.practicum.filmorate.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.service.ReviewService;


@WebMvcTest(ReviewController.class)
class ReviewControllerMocTest {
    @MockBean
    private ReviewService reviewService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetRequestResponseCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reviews"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenPostRequestResponseCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reviews"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenPutRequestResponseCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/reviews/1/like/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
