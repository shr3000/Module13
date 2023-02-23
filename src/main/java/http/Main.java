package http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Main {
    private static final String TEST_USER = "https://jsonplaceholder.typicode.com/users/";
    private static final String TEST_POST = "https://jsonplaceholder.typicode.com/posts/";
    private static final String TEST_USER_BY_USERNAME = "https://jsonplaceholder.typicode.com/users?username=";

    private static final String TEST_POSTS_USERS = "/posts";
    private static final String TEST_TASK = "/todos";
    private static User user = new User();

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(TEST_USER);
        user.setName("NewUser");
        user.setUsername("NewUserfromPOST");
        user.setPhone("1112223344");

        //создание нового объекта в https://jsonplaceholder.typicode.com/users. Возможно, вы не увидите обновлений на сайте.
        //Метод создания работает правильно, если в ответ на JSON с объектом вернулся такой же JSON, но с полем id со значением на
        //1 больше, чем самый большой id на сайте.
        User sendetUser = HttpUtils.sendPost(uri, user);
        System.out.println(sendetUser);

        // получение информации о пользователе с определенным id https://jsonplaceholder.typicode.com/users/{id}
        URI uri1 = new URI(String.format("%s%d", TEST_USER, 10));
        System.out.println(HttpUtils.sendGet(uri1));

        //получение информации о пользователе с опредленным username - https://jsonplaceholder.typicode.com/users?username={username}

        System.out.println(HttpUtils.sendGetUserByIB(new URI(String.format("%s%s", TEST_USER_BY_USERNAME, "Bret"))));

        //удаление объекта из https://jsonplaceholder.typicode.com/users. Здесь будем считать корректным результат - статус из
        //группы 2хх в ответ на запрос.

        HttpUtils.sendDelete(new URI(String.format("%s%d", TEST_USER, 1)), HttpUtils.sendGet(uri1));

        //получение информации обо всех пользователях https://jsonplaceholder.typicode.com/users/
        List<User> list = HttpUtils.sendAllUsers(new URI(TEST_USER));
        for (User user1: list) {
            System.out.println(user1);
        }
        // Обновление пользователя по id
        System.out.println(HttpUtils.sendPut(new URI(TEST_USER + 1), user));

        //Дополните программу методом, который будет выводить все комментарии к последнему посту определенного пользователя и записывать их в файл.
        HttpUtils.findAllCommentsToPost(TEST_USER, TEST_POSTS_USERS, 1);

        //Дополните программу методом, который будет выводить все открытые задачи для пользователя Х.
        HttpUtils.findAllIncompleteTaskByID(TEST_USER, TEST_TASK, 1);


    }
}
