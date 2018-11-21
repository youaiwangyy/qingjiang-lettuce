package com.qingjiang.lettuce.domain;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {

    private int id;
    private String name;
    private boolean gender;



}
