package com.cuggd.netty.chat;

import java.util.Scanner;

/**
 * @author lineng
 * @create 2019-05-26 15:48
 */
public class Demo {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        //程序会一直在这里（类似于死循环）等待用户的输入
        while (scanner.hasNextLine()){
            String msg=scanner.nextLine();
            System.out.println(msg);
        }
    }
}
