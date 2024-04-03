package com.example.userapp.entity;

import com.example.common.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(min = 1, max = 100, message = "Name must be at least 1 character and no more than 100")
    @NotBlank(message = "User firstname cannot be empty")
    private String firstName;

    @Size(min = 1, max = 100, message = "Lastname must be at least 1 character and no more than 100")
    @NotBlank(message = "User Lastname cannot be empty")
    private String lastName;

    @Column(unique = true, nullable = false, length = 30)
    @Size(min = 5, max = 30, message = "Email must be at least 5 character and no more than 30")
    @NotBlank(message = "User email cannot be empty")
    @Email(message = "Invalid email")
    private String email;

    @Column(nullable = false)
    private Date registrationDate = new Date();

    @Column(nullable = false)
    private Date lastUpdateDate = new Date();

    @Column(unique = true)
    private Long phone;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "User password cannot be empty")
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isEnabled;

    private boolean isBlocked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
