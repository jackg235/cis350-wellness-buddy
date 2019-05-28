package com.example.personalwellness;

public class Resource {
    private String name;
    private String image;
    private String summary;
    private String category;
    private String address;
    private String website;
    private double lat;
    private double lon;
    private String phonenumber;
    private boolean isSpotify;

    public Resource(String name, String image, String summary, String category,
                    String address, String website, double lat, double lon, String phonenumber,
                    boolean isSpotify) {
        this.name = name;
        this.image = image;
        this.summary = summary;
        this.category = category;
        this.address = address;
        this.website = website;
        this.lat = lat;
        this.lon = lon;
        this.phonenumber = phonenumber;
        this.isSpotify = isSpotify;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getSummary() {
        return summary;
    }

    public String getAddress() { return address; }

    public double getLat() { return lat; }

    public double getLng() { return lon; }

    public boolean getIsSpotify() { return isSpotify; }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Resource)) {
            return false;
        }
        Resource r = (Resource)o;
        return this.name == r.name;
    }


}
