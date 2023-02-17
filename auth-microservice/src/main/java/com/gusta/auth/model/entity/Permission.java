package com.gusta.auth.model.entity;

import lombok.*;
import org.springframework.security.core.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "permission")
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @Override
    public String getAuthority() {
        return this.description;
    }
}