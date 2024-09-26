package io.hhplus.tdd.point

import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch

@WebMvcTest(PointController::class)
class PointControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userPointService: UserPointService

    @Test
    fun `포인트 조회시 유효한 아이디가 아니면 에러 응답`() {
        // given
        val userId = "a"

        // when // then
        mockMvc.get("/point/$userId")
            .andDo { print() }
            .andExpect {
                status { is4xxClientError() }
            }
    }

    @Test
    fun `포인트 조회시 유효한 아이디면 UserPointResponse 반환`() {
        // given
        given(userPointService.find(anyLong()))
            .willReturn(UserPointResponse(id = 1L, point = 100L))
        val userId = 1

        // when // then
        mockMvc.get("/point/$userId")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("id") { value(1) }
                    jsonPath("point") { value(100) }
                }
            }
    }

    @Test
    fun `포인트 이력 조회시 유효하지 않은 아이디면 에러 응답`() {
        // given
        val userId = "a"

        // when // then
        mockMvc.get("/point/$userId/histories")
            .andDo { print() }
            .andExpect {
                status { is4xxClientError() }
            }
    }

    @Test
    fun `포인트 이력 조회시 유효한 아이디면 이력 리스트 반환`() {
        // given
        val histories = listOf(
            PointHistoryResponse(id = 1L, userId = 1L, type = TransactionType.USE, amount = 0),
            PointHistoryResponse(id = 2L, userId = 1L, type = TransactionType.USE, amount = 0)
        )
        given(userPointService.findHistories(anyLong()))
            .willReturn(histories)
        val userId = "1"

        // when // then
        mockMvc.get("/point/$userId/histories")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("[*]") { isArray() }
                    jsonPath("[0].id") { value(1) }
                    jsonPath("[0].userId") { value(1) }
                    jsonPath("[0].type") { value("USE") }
                    jsonPath("[1].id") { value(2) }
                    jsonPath("[1].userId") { value(1) }
                    jsonPath("[1].type") { value("USE") }
                }
            }
    }


    @Test
    fun `포인트를 충전시 충전금액이 숫자가 아니면 클라이언트 오류`() {
        // given
        val userId = 1L

        // when // then
        mockMvc.patch("/point/$userId/charge") {
            contentType = MediaType.APPLICATION_JSON
            content = "a"
        }
            .andDo { print() }
            .andExpect {
                status { is4xxClientError() }
            }
    }

    @Test
    fun `포인트를 충전하면 충전결과 UserPoint를 반환한다`() {
        // given
        given(userPointService.charge(anyLong(), anyLong()))
            .willReturn(UserPointResponse(id = 1L, point = 1L))
        val userId = 1L

        // when // then
        mockMvc.patch("/point/$userId/charge") {
            contentType = MediaType.APPLICATION_JSON
            content = 1
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("id") { value(1) }
                    jsonPath("point") { value(1) }
                }
            }
    }

    @Test
    fun `포인트를 사용시 사용금액이 숫자가 아니면 클라이언트 오류`() {
        // given
        val userId = 1L

        // when // then
        mockMvc.patch("/point/$userId/use") {
            contentType = MediaType.APPLICATION_JSON
            content = "a"
        }
            .andDo { print() }
            .andExpect {
                status { is4xxClientError() }
            }
    }

    @Test
    fun `포인트를 사용하면 사용결과 UserPoint를 반환한다`() {
        // given
        given(userPointService.use(anyLong(), anyLong()))
            .willReturn(UserPointResponse(id = 1L, point = 0L))
        val userId = 1L

        // when // then
        mockMvc.patch("/point/$userId/use") {
            contentType = MediaType.APPLICATION_JSON
            content = 1
        }
        .andDo { print() }
        .andExpect {
            status { isOk() }
            content {
                jsonPath("$.id") { value(1) }
                jsonPath("$.point") { value(0) }
            }
        }
    }
}
