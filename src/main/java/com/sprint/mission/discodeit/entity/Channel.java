package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity{
    private String name; // 이름

    public Channel(String name) {
        super();
        this.name = name;
    }

    public void changeName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return String.format("Channel ( \n" +
                        " id=%s, createdAt=%s, updateAt=%s \n" +
                        " name=%s \n" + ")",
                super.getId(), super.getCreatedAt(),super.getUpdatedAt(),
                this.name
        );
    }
}
