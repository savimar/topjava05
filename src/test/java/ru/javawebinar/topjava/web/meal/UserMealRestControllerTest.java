package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


/**
 * Created by B on 19.01.2016.
 */
public class UserMealRestControllerTest extends AbstractControllerTest {

    static final String REST_URL = "/rest/meals/";


    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(MEAL1));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), mealService.getAll(USER_ID));
    }


    @Test
    public void testGetAll() throws Exception {
        TestUtil.print(mockMvc.perform(get(REST_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentListMatcher(USER_MEALS)));
    }


    @Test
    public void testUpdate() throws Exception {
        UserMeal updated = getUpdated();
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk());
        MATCHER.assertEquals(updated, mealService.get(MEAL1_ID, USER_ID));

    }

    @Test
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void testCreate() throws Exception {
        UserMeal created = getCreated();
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created)))
                .andExpect(status().isCreated());
        UserMeal returned = MATCHER.fromJsonAction(actions);
        created.setId(returned.getId());
        MATCHER.assertEquals(created, returned);
        MATCHER.assertCollectionEquals(Arrays.asList(created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), mealService.getAll(USER_ID));
    }



    @Test
    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void testGetBetween() throws Exception {
        //  get(REST_URL + "getBetween/?startDate="+startDate+"&endDate="+endDate).contentType(
        TestUtil.print(mockMvc.perform(get(REST_URL + "filter?startDate=2015-05-30T07:00:00&endDate=2015-05-30T22:00:00")
                //  +LocalDateTime.of(2015, Month.MAY, 30, 7,0,0).format(DateTimeFormatter.ISO_DATE)
                // "2015-06-30T07:00"+
                //  +
                // + LocalDateTime.of(2015, Month.MAY, 30, 22,0,0).format(DateTimeFormatter.ISO_DATE))
                //  "2015-06-30T22:00")
                //        + )
              /*  mockMvc.perform(get(REST_URL + "getBetween/?startDate=" + LocalDateTime.of(2015, Month.MAY, 30, 7,0, 0).format(DateTimeFormatter.ISO_DATE)
               // "2015-06-30T07:00:00"

                +"&endDate= " + LocalDateTime.of(2015, Month.MAY, 30, 22,0, 0).format(DateTimeFormatter.ISO_DATE))
               // "2015-06-30T22:00:00")

             //    "2015-06-30T22:00:00")*/
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)))
                .andExpect(MATCHER.contentListMatcher(Arrays.asList(MEAL3, MEAL2, MEAL1)));
        //TODO
    }
}