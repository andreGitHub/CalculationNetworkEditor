/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ToolBox;

/**
 *
 * @author andre
 */
public class ToolBox {
    public static void printStackTrace() {
        System.out.println("--- stacktrace ----------------------------------");
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        for(int i = 0; i < ste.length; i++) {
            System.out.println(ste[i]);
        }
        System.out.println("-------------------------------------------------");
    }
}
