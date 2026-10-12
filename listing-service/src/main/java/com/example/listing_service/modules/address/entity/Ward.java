package com.example.listing_service.modules.address.entity;

import java.util.List;

import com.example.listing_service.modules.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "wards")
public class Ward {

    @Id
    private Long code;

    private String name;

    private String codename;

    @JsonProperty("division_type")
    private String divisionType;

    @JsonProperty("short_codename")
    private String shortCodename;

    @OneToMany(mappedBy = "ward")
    private List<Post> post;

    @ManyToOne
    @JoinColumn(name = "district_code")
    private District district;

}
