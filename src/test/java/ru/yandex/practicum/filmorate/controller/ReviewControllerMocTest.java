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
//    @MockBean
//    private ReviewService reviewService;
//
//    @Autowired
//    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenRequestResponseCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("reviews"))
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andExpect(MockMvcResultMatchers.content().json())
//                .andExpect()

    }

//    private final String jsonResponse200 = """
//        {
//            "id": "2222"
//
//        """

}
