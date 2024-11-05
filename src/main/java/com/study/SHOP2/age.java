package com.study.SHOP2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class age {


    private Integer age;
    private String name;

    public void 한살더하기() {
        this.age = this.age + 1;

    }
    public void 나이설정(Integer a) {
        if (a > 0 && a < 100) {
        this.age = a;
        }
    }


}
