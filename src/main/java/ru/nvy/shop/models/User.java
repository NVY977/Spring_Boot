package ru.nvy.shop.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private boolean active;

    //ElementCollection - позволяет избавиться от создания таблицы для Enum
    // Fetch - параметр, который определяет как будут подгружаться данные
    // Жадный - сразу подгружаем (ускоряет работу при малых данных), ленивый - как только потребуется (медленный)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    //Создаем таблицу для ролей и присоединяем к текущей таблице через user_id
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


    //region Get/Set
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    //endregion
}
