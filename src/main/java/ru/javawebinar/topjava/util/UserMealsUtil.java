package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.dao.UserDao;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0)),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0)),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0)),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0)),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0)),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0))
        );
        List<UserMealWithExceed> filteredMealsWithExceeded = getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        filteredMealsWithExceeded.forEach(System.out::println);

        System.out.println(getFilteredMealsWithExceededByCycle(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExceed> getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = mealList.stream().collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)));

        return mealList.stream()
                .filter(um -> TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(),
                        caloriesSumByDate.get(um.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredMealsWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesSumPerDate = new HashMap<>();
        for (UserMeal meal : mealList) {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            caloriesSumPerDate.put(mealDate, caloriesSumPerDate.merge(mealDate, 0, (oldValue, newValue) -> oldValue + newValue));
        }

        List<UserMealWithExceed> mealExceeded = new ArrayList<>();
        for (UserMeal meal : mealList) {
            LocalDateTime dateTime = meal.getDateTime();
            if (TimeUtil.isBetween(dateTime.toLocalTime(), startTime, endTime)) {
                mealExceeded.add(new UserMealWithExceed(dateTime, meal.getDescription(), meal.getCalories(),
                        caloriesSumPerDate.get(dateTime.toLocalDate()) > caloriesPerDay));
            }
        }
        return mealExceeded;
    }

    public static List<UserMealWithExceed> getExceed(List<UserMeal> mealList, int caloriesPerDay) {
        Map<LocalDate, Integer> mapCalories =
                mealList
                        .stream()
                        .collect(Collectors.groupingBy(
                                um -> um.getDateTime().toLocalDate(),
                                Collectors.summingInt(UserMeal::getCalories)));
        List<UserMealWithExceed> userMealWithExceeds = mealList
                .stream()
                .map((s) -> new UserMealWithExceed(s.getId(), s.getCalories(), s.getDateTime(), s.getDescription(),
                        ((mapCalories.get(s.getDateTime().toLocalDate()) > caloriesPerDay))))
                .sorted((o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime()))
                .collect(Collectors.toList());


        return userMealWithExceeds;
    }

    public static List<UserMealWithExceed> getUserMealWithExceeds() {
        UserDao umd = UserDao.instance;
        List<UserMeal> userMeals = new ArrayList<>();
        userMeals.addAll(umd.mapUserMeal.values());
        return UserMealsUtil.getExceed(userMeals, 2000);
    }

}
