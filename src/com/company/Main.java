package com.company;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<Post> posts = new ArrayList<>();
        Spark.staticFileLocation("/public");
        Spark.init();

        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("name");
                    if (username == null) {
                        return new ModelAndView(new HashMap<>(), "index.html");//Why does modelandview require a HashMap?
                    }
                    HashMap m = new HashMap();
                    m.put("name",username);
                    m.put("posts", posts);
                    return new ModelAndView(m, "posts.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String username = request.queryParams("name");
                    Session session= request.session();
                    session.attribute("name",username);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/create-post",
                ((request, response) -> {
                    Post post = new Post();
                    post.id = posts.size() + 1;
                    post.text = request.queryParams("post");
                    posts.add(post);
                    response.redirect("/");
                    return "";
                })
        );




        Spark.post( //Why aren't we creating a session here? When's this necessary?
                "/delete-post",
                ((request, response) -> {
                    String id = request.queryParams("postid");
                    try {
                        int idNum = Integer.valueOf(id);
                        posts.remove(idNum - 1);
                        for (int i = 0; i < posts.size(); i++) {
                            posts.get(i).id = i + 1;
                        }
                    } catch (Exception e) {
                    }
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/edit-post",
                ((request, response) -> {
                    String id = request.queryParams("postid");
                    try {
                        int idNum = Integer.valueOf(id);
                        Post post = posts.get(idNum-1);
                        post.text = request.queryParams("editpost");
                    }catch(Exception e){

                    }
                    response.redirect("/");
                    return "";
                })
        );
    }
}

