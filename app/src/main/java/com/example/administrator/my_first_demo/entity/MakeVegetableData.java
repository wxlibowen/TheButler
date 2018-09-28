package com.example.administrator.my_first_demo.entity;

/*
 *项目名：com.example.administrator.my_first_demo.entity
 *创建者： LJW
 *创建时间：2018/6/6 0006
 */public class MakeVegetableData {
    String title;//标题
    //     String tags;//
    String imtro;//简介
    String ingredients;//食材
    String burden;//调料
    String stepdetail;//步骤详情

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImtro() {
        return imtro;
    }

    public void setImtro(String imtro) {
        this.imtro = imtro;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getBurden() {
        return burden;
    }

    public void setBurden(String burden) {
        this.burden = burden;
    }

    public String getStepdetail() {
        return stepdetail;
    }

    public void setStepdetail(String stepdetail) {
        this.stepdetail = stepdetail;
    }

    @Override
    public String toString() {
        return "MakeVegetableData{" +
                "title='" + title + '\'' +
                ", imtro='" + imtro + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", burden='" + burden + '\'' +
                ", stepdetail='" + stepdetail + '\'' +
                '}';
    }
}
