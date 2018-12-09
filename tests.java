/**
 * Does nothing useful. Just experimental code before implementing in programs that actually matter
 *
 * @author  YuKai Bao
 * @version 1.0
 * @since   2018.12.05
 */



package com.example.v3;

import java.util.ArrayList;

public class tests {



    public static void main(String args[]) {


        // Checking if ArrayList<Object> can store different kinds of objects
        // Conclusion: It can.
        ArrayList<Object> o = new ArrayList<Object>();

        o.add(new Integer(1));
        o.add(new String("HELLO"));
        o.add(new Integer (9));

        for (Object ob : o) {
            System.out.println(ob.toString());
        }
        // End ArrayList<Object> test

    }

}

