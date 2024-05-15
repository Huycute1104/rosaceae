package com.example.rosaceae.model;

import com.example.rosaceae.enums.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "[User]")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UsersID")
    private int usersID;

    @Column(name = "AccountName", length = 20)
    private String accountName;

    @Column(name = "Email", length = 50,unique = true)
    private String email;

    @Column(name = "Password", length = 100)
    private String password;

    @Column(name = "Phone", length = 50)
    private String phone;

    @Column(name = "Address" , length = 100)
    private String address;

    private  float rate;

    @Column(name = "UserWallet")
    private double userWallet;

    @Column(name = "UserStatus")
    private boolean userStatus;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    private List<Token> tokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Cart> cart;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    private List<UserReport> userReports;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    private List<Follower> customers;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    private List<Follower> shops;

    @ManyToOne
    @JoinColumn(name = "RankMemberID")
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @JsonBackReference
    private RankMember rankMember;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }
}
