/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.npm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.IOException;

/**
 *
 * @author PRASHANT
 */
public class InwardMap
{

    String datafile;
    int itemcount;

    List<List<Object[]>> list = new ArrayList<>();

    public void addEntry(int itemid, Date d, int purchase_price, int sale_price)
    {
        Object[] entry = new Object[]
        {
            d.getTime(), purchase_price, sale_price
        };

        if (list.size() <= itemid || list.get(itemid) == null)
        {
            while (list.size() < itemid + 1)
            {
                list.add(new ArrayList<>());
            }

            list.set(itemid, new ArrayList<>());
        }
        List<Object[]> entryList = list.get(itemid);
        entryList.add(entry);
    }

    public void getEntry(int itemid, Date d, int[] outputs)
    {
        List<Object[]> entryList = list.get(itemid);
        long time = d.getTime();

        long maxtime = 0;
        Object[] entry = null;

        for (Object[] e : entryList)
        {
            long t = (long) e[0];
            if (t <= time && t > maxtime)
            {
                entry = e;
                maxtime = t;
            }
        }

        if (entry == null)
        {
            outputs[0] = outputs[1] = 0;
        } else
        {
            outputs[0] = (int) entry[1];
            outputs[1] = (int) entry[2];
        }
    }

    public static void main(String[] args)
    {
        InwardMap im = new InwardMap();
        try(PrintWriter pw = new PrintWriter(new File(datafile)))
        {
            StringBuilder sb = new StringBuilder(); 
            BufferedReader reader;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try
            {
                reader = new BufferedReader(new FileReader("D:\\BE\\NPM\\inward.csv"));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    String[] tokens = line.split(",");
                    int id = Integer.parseInt(tokens[0]);
                    Date d = sdf.parse(tokens[1]);
                    int price1 = Integer.parseInt(tokens[2]);
                    int price2 = Integer.parseInt(tokens[3]);

                    im.addEntry(id, d, price1, price2);
                }
                reader.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                int []prices=new int[2];
                reader = new BufferedReader(new FileReader("D:\\BE\\NPM\\dataset.csv"));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    String[] tokens = line.split(",");
                    Date d = sdf.parse(tokens[0]);
                    sb.append(sdf.format(c.getTime())).append(",");
                    for (int i = 1; i < tokens.length; i++)
                    {
                        int id = Integer.parseInt(tokens[0]);
                        int value=0;
                        if(id==1)
                        {
                            im.getEntry(i-1, d, prices);
                            value = prices[1]-prices[0];
                        }
                         sb.append(value).append(",");
                         
                        
                         pw.println(sb);
                         sb.length(0);
                    }
                    
                }
                pw.close();
                reader.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }            
        }
    }

}
