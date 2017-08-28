package tech.rsqn.springopenidclient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Principal;

public class OAuth2UserInfo implements Principal {
    private String id;
    private String name;
    private String picture;
    private String link;

    public OAuth2UserInfo() {
    }

    @JsonCreator
    public OAuth2UserInfo(@JsonProperty("id") String id,
                          @JsonProperty("name") String name,
                          @JsonProperty("picture") String picture,
                          @JsonProperty("link") String link) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "OAuth2UserInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}