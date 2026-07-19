package com.sprint.mission;


import com.sprint.mission.discodeit.entity.User;

public class Application {
    public static void main(String[] args) {
        User aaron = new User("Aaron");
        System.out.println(aaron);
        System.out.println(aaron.getCreatedAt());
    }
}