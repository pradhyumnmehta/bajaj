package com.bajaj.bajaDemo;

import com.bajaj.bajaDemo.dto.BfhlRequest;
import com.bajaj.bajaDemo.dto.BfhlResponse;
import com.bajaj.bajaDemo.service.BfhlService;
import com.bajaj.bajaDemo.service.BfhlServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BajaDemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BfhlService bfhlService;

    @BeforeEach
    void setUp() {
        bfhlService = new BfhlServiceImpl();
    }

    // -----------------------------------------------------------------------
    // Unit tests on the service layer
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Example A: mixed input - numbers, alphabets, special chars")
    void testExampleA_service() {
        BfhlRequest req = new BfhlRequest(List.of("a", "1", "334", "4", "R", "$"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).containsExactly("1");
        assertThat(res.getEvenNumbers()).containsExactly("334", "4");
        assertThat(res.getAlphabets()).containsExactly("A", "R");
        assertThat(res.getSpecialCharacters()).containsExactly("$");
        assertThat(res.getSum()).isEqualTo("339");
        // alphabetical chars in order: a, R  → reversed: Ra → alternating caps: R(upper,0) a(lower,1) = "Ra"
        assertThat(res.getConcatString()).isEqualTo("Ra");
    }

    @Test
    @DisplayName("Example B: mixed input with multiple special chars")
    void testExampleB_service() {
        BfhlRequest req = new BfhlRequest(List.of("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).containsExactly("5");
        assertThat(res.getEvenNumbers()).containsExactly("2", "4", "92");
        assertThat(res.getAlphabets()).containsExactly("A", "Y", "B");
        assertThat(res.getSpecialCharacters()).containsExactly("&", "-", "*");
        assertThat(res.getSum()).isEqualTo("103");
        // alphabetical chars in order: a, y, b → reversed: bya → B(0,upper) y(1,lower) A(2,upper) = "ByA"
        assertThat(res.getConcatString()).isEqualTo("ByA");
    }

    @Test
    @DisplayName("Example C: only multi-char alphabetical tokens")
    void testExampleC_service() {
        BfhlRequest req = new BfhlRequest(List.of("A", "ABCD", "DOE"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getAlphabets()).containsExactly("A", "ABCD", "DOE");
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        // chars in order: A,A,B,C,D,D,O,E → reversed: E,O,D,D,C,B,A,A
        // alternating caps: E(U) o(l) D(U) d(l) C(U) b(l) A(U) a(l) = "EoDdCbAa"
        assertThat(res.getConcatString()).isEqualTo("EoDdCbAa");
    }

    @Test
    @DisplayName("Empty data array returns success with all empty lists and sum 0")
    void testEmptyData_service() {
        BfhlRequest req = new BfhlRequest(List.of());
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getAlphabets()).isEmpty();
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        assertThat(res.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Only numbers — no alphabets or special chars")
    void testOnlyNumbers_service() {
        BfhlRequest req = new BfhlRequest(List.of("3", "6", "15"));
        BfhlResponse res = bfhlService.process(req);

        assertThat(res.getOddNumbers()).containsExactly("3", "15");
        assertThat(res.getEvenNumbers()).containsExactly("6");
        assertThat(res.getAlphabets()).isEmpty();
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("24");
        assertThat(res.getConcatString()).isEmpty();
    }

    // -----------------------------------------------------------------------
    // Integration tests via MockMvc
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("POST /bfhl returns 200 with correct JSON structure")
    void testPostBfhl_http200() throws Exception {
        BfhlRequest req = new BfhlRequest(List.of("a", "1", "334", "4", "R", "$"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.roll_number").isNotEmpty())
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.even_numbers[0]").value("334"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"));
    }

    @Test
    @DisplayName("POST /bfhl with null data returns 400")
    void testPostBfhl_nullData_returns400() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"data\": null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /bfhl with missing body returns 400")
    void testPostBfhl_emptyBody_returns400() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
