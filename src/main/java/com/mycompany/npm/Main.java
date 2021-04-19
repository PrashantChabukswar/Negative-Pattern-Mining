/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.npm;

import fpgrowth.FPGrowth;
import java.io.File;
import java.io.FileNotFoundException;

public class Main
{

    static int threshold = 5;
    static String file = "dataset-weights.csv";

    public static void main(String[] args) throws FileNotFoundException
    {
	long start = System.currentTimeMillis();
	new FPGrowth(new File(file), threshold);
	System.out.println((System.currentTimeMillis() - start));
    }
}
