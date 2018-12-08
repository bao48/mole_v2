package com.example.v3;

import java.util.ArrayList;

public class tests {

    public static void main(String args[]) {
        ArrayList<Object> o = new ArrayList<Object>();

        o.add(new Integer(1));
        o.add(new String("HELLO"));
        o.add(new Integer (9));

        for (Object ob : o) {
            System.out.println(ob.toString());
        }
    }

}

