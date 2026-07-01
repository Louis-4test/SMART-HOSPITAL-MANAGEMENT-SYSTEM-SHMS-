package com.hms.dto;

public class CollectionComparisonDTO {

    private String comparisonGroup;
    private String feature;
    private String firstCollection;
    private String secondCollection;

    public CollectionComparisonDTO() {}

    public CollectionComparisonDTO(String comparisonGroup, String feature, String firstCollection, String secondCollection) {
        this.comparisonGroup = comparisonGroup;
        this.feature = feature;
        this.firstCollection = firstCollection;
        this.secondCollection = secondCollection;
    }

    public String getComparisonGroup() { return comparisonGroup; }
    public void setComparisonGroup(String comparisonGroup) { this.comparisonGroup = comparisonGroup; }
    public String getFeature() { return feature; }
    public void setFeature(String feature) { this.feature = feature; }
    public String getFirstCollection() { return firstCollection; }
    public void setFirstCollection(String firstCollection) { this.firstCollection = firstCollection; }
    public String getSecondCollection() { return secondCollection; }
    public void setSecondCollection(String secondCollection) { this.secondCollection = secondCollection; }
}
