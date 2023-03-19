package edu.utsa.cs3443.anw198.foodtracker.model;

public class FoodSearchResult {
    private String name;
    private String brand;
    private String id;
    private Double fat, carbs, protein, calories;


    public FoodSearchResult(String name, String brand, String id, Double fat, Double carbs, Double protein, Double calories) {
        this.name = name;
        this.brand = brand;
        this.id = id;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Double getFat() {
        return fat;
    }

    public Double getCarbs() {
        return carbs;
    }

    public Double getProtein() {
        return protein;
    }

    public Double getCalories() {
        return calories;
    }

    public String getBrand() {
        return brand;
    }
}