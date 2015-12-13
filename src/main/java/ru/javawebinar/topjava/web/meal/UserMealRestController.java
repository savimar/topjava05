package ru.javawebinar.topjava.web.meal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.LoggerWrapper;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;
import ru.javawebinar.topjava.service.UserMealService;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class UserMealRestController {
    protected final LoggerWrapper LOG = LoggerWrapper.get(getClass());

    @Autowired
    private UserMealService service;


    public List<UserMealWithExceed> get() {
        return getListMeal(LoggedUser.id());
    }

    public List<UserMealWithExceed> getByFilter(String fromDate, String toDate, String fromTime, String toTime) {
        return getFilter(fromDate, toDate, fromTime, toTime, LoggedUser.id());
    }

    public UserMeal get(int id) {
        if (service.get(id, LoggedUser.id()).getId() == LoggedUser.id()) {
            if (service.get(id, LoggedUser.id()) != null) {
                return service.get(id, LoggedUser.id());
            } else {
                LOG.error("Trying to get userMeal, not belonging to this user");
                throw new NotFoundException("Trying to get userMeal, not belonging to this user");
            }

        } else {
            LOG.error("Trying to get userMeal, not belonging to this user");
            throw new NotFoundException("Trying to get userMeal, not belonging to this user");
        }

    }

    public UserMeal create(UserMeal usermeal) {
        usermeal.setId(null);
        LOG.info("create " + usermeal);
        return service.save(usermeal, LoggedUser.id());
    }

    public void delete(int id, int userId) {
        LOG.info("delete " + id);
        service.delete(id, userId);

    }

    public void update(UserMeal userMeal, int id) {
        userMeal.setId(id);
        LOG.info("update " + userMeal);
        service.update(userMeal, LoggedUser.id());

    }

    public List<UserMeal> getByUser(int id) {
        LOG.info("get by user id = " + id);
        return service.getByUser(id);
    }

    public List<UserMealWithExceed> getListMeal(int userId) {
        return UserMealsUtil.getWithExceeded(service.getByUser(userId), UserMealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<UserMealWithExceed> getFilter(String fromDate, String toDate, String fromTime, String toTime, int userId) {
        LocalDate dateFrom = fromDate != "" ? LocalDate.parse(fromDate, TimeUtil.DATE_FORMATTER) : LocalDate.MIN;
        LocalDate dateTo = toDate != "" ? LocalDate.parse(toDate, TimeUtil.DATE_FORMATTER) : LocalDate.MAX;
        LocalTime timeFrom = fromTime != "" ? LocalTime.parse(fromTime, TimeUtil.TME_FORMATTER) : LocalTime.MIN;
        LocalTime timeTo = toTime != "" ? LocalTime.parse(toTime, TimeUtil.TME_FORMATTER) : LocalTime.MAX;

        List<UserMeal> listDate = UserMealsUtil.getFilteredWithExceededByDate(service.getByUser(userId), dateFrom, dateTo, UserMealsUtil.DEFAULT_CALORIES_PER_DAY);
        return UserMealsUtil.getFilteredWithExceeded(listDate, timeFrom, timeTo, UserMealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public UserMealWithExceed getUserMealWithExceed(UserMeal um) {
        List<UserMeal> listUm = new ArrayList<>();
        listUm.add(um);
        List<UserMealWithExceed> list = UserMealsUtil.getWithExceeded(listUm, UserMealsUtil.DEFAULT_CALORIES_PER_DAY);
        return list.size() > 0 ? list.get(0) : null;
    }

}
