package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerMocTest {
    @MockBean
    private ReviewService reviewService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnStatusOkAndId1() throws Exception {
        Review correctReview = new Review("This film is beautiful.", true,
                1, 1);
        Review correctReviewWithId = new Review("This film is beautiful.", true,
                1, 1);
        correctReviewWithId.setReviewId(1);
        when(reviewService.addNewReview(correctReview)).thenReturn(correctReviewWithId);

        mockMvc.perform(post("/reviews")
                        .content(objectMapper.writeValueAsString(correctReview))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.reviewId").value("1"));
    }

    @Test
    public void whenGetRequestResponseCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reviews"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void whenPutRequestResponseCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/reviews/1/like/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void whenGetRequestResponseIncorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/y"))
                .andExpect(status().is5xxServerError())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
