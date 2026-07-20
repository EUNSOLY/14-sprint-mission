package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String name; // 이름
    private String phone;

    public User(String name, String phone) {
        super();
        this.name = name;
        this.phone = phone;
    }

    public void changeName(String name){
        this.name = name;
    }
    public void changePhone(String phone){this.phone = phone;}

    public String getName(){
        return this.name;
    }

    public String getPhone(){return this.phone;}

    @Override
    public String toString(){
        return String.format("Channel ( \n" +
                        " id=%s, createdAt=%s, updateAt=%s \n" +
                        " name=%s \n" +
                        ")",
                super.getId(), super.getCreatedAt(),super.getUpdatedAt(),
                this.name
        );
    }
}