package com.paymybuddy.entity.embedkey;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class FriendKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "friend_id")
    Long friendId;

}
