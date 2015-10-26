package com.company;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        ArrayList <Post> posts = new ArrayList<>();
        Spark.staticFileLocation("/public");
        Spark.init();
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    User user = new User();
                    user.name = request.queryParams("name");
                    users.add(user);
                    response.redirect("/posts");
                    return "";
                })
        );

        Spark.post(
                "/create-post",
                ((request, response) -> {
                    Post post = new Post();
                    post.text = request.queryParams("post");
                    posts.add(post);
                    response.redirect("/posts");
                    return "";
                })
        );

        Spark.get(
                "/posts",
                (request, response) -> {
                    HashMap m = new HashMap();
                    m.put("name", users.get(0).name);
                    m.put("posts", posts);
                    return new ModelAndView(m, "posts.html");
                },
                new MustacheTemplateEngine()
                );
    }
}