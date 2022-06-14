package com.paymybuddy.entity;

import com.paymybuddy.entity.embedkey.FriendKey;

import javax.persistence.*;

@Entity
@Table(name = "friend")
public class FriendEntity {

    @EmbeddedId
    private FriendKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("friendId")
    @JoinColumn(name = "friend_id")
    private UserEntity friend;
}
