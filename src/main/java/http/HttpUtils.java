package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpUtils {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();
    public static User sendGet(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        User user = GSON.fromJson(response.body(), User.class);
        return user;
    }
    public static User sendGetUserByIB(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<User> list = GSON.fromJson(response.body(), new TypeToken<List<User>>(){}.getType());
        return list.get(0);
    }

    public static List<User> sendAllUsers(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<User> list = GSON.fromJson(response.body(), new TypeToken<List<User>>(){}.getType());
        return list;
    }

    public static User sendPost(URI uri, User user) throws IOException, InterruptedException {
        String str = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(str))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static void sendDelete(URI uri, User user) throws IOException, InterruptedException {
        String str = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(str))
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    public static String sendPut(URI uri, User user) throws IOException, InterruptedException {
        String str = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(str))
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body().toString();
    }

    private static String sendAllCommentsToPost(String url, String pathCommen, int id) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("%s%d%s", url, id, pathCommen)))
                .header("Content-type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        //String res = GSON.fromJson(response.body(), new TypeToken<List<Comment>>(){}.getType());
        return response.body().toString();
    }

    public static void findAllCommentsToPost(String url, String pathPost, int id) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("%s%d%s", url, id, pathPost)))
                .header("Content-type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Post> list = GSON.fromJson(response.body(), new TypeToken<List<Post>>(){}.getType());
        String res = sendAllCommentsToPost("https://jsonplaceholder.typicode.com/posts/", "/comments", list.get(list.size()-1).getId());
        //Файл должен называться "user-X-post-Y-comments.json", где Х - номер пользователя, Y - номер поста.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user-" + id + "-post-" + list.get(list.size()-1).getId() + ".gson"));){
            writer.write(res);
        }

    }

    public static void findAllIncompleteTaskByID(String url, String pathPost, int id) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("%s%d%s", url, id, pathPost)))
                .header("Content-type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Todo> list = GSON.fromJson(response.body(), new TypeToken<List<Todo>>(){}.getType());
        for (Todo todo: list) {
            if (!todo.isCompleted()) System.out.println(todo);
        }
    }


}
