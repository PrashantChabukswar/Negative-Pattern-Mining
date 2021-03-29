/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.npm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author PRASHANT
 */
public class SynthDatasetGeneratorInward
{

    private static class ItemEntry
    {

        int id;
        int frequency;
        int baseprice;

        public ItemEntry(int id, int frequency, int baseprice)
        {
            this.id = id;
            this.frequency = frequency;
            this.baseprice = baseprice;
        }

    }

    private String paramfile;
    private String datafile;

    int nitems;
    List<ItemEntry> entries = new ArrayList<>();

    public void generateSynthData(String paramfile, String datafile) throws Exception
    {
        this.paramfile = paramfile;
        this.datafile = datafile;
        loadParams();
        generateDataset();
    }

    private void loadParams() throws Exception
    {
        try (BufferedReader br = new BufferedReader(new FileReader(paramfile)))
        {
            String line = null;
            while ((line = br.readLine()) != null)
            {
                String[] tokens = line.split("\\s+");
                System.out.println(Arrays.toString(tokens));

                if ("items".equals(tokens[0]))
                {
                    nitems = Integer.parseInt(tokens[1]);
                } else if ("item".equals(tokens[0]))
                {
                    if (tokens.length != 4)
                    {
                        continue;
                    }
                    ItemEntry ie = new ItemEntry(
                            Integer.parseInt(tokens[1]),
                            Integer.parseInt(tokens[2]),
                            Integer.parseInt(tokens[3])
                    );
                    entries.add(ie);
                }
            }
        }
        System.out.println(entries.size() + " entries found");
    }

    private void generateDataset() throws FileNotFoundException
    {
        int y = 2019;
        Random random = new Random();
        try (PrintWriter pw = new PrintWriter(new File(datafile)))
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            StringBuilder sb = new StringBuilder();

            for (ItemEntry ie : entries)
            {
                Set<Integer> dayset = new HashSet<>();
                while (dayset.size() != ie.frequency)
                {
                    dayset.add(random.nextInt(365));
                }
                List<Integer> days = new ArrayList<>(dayset);
                Collections.sort(days);

                for (int day : days)
                {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, y);

                    c.set(Calendar.DAY_OF_YEAR, day);

                    float factor = (float) (random.nextFloat() * 0.2 + 0.9);
                    float factor1 = (float) (random.nextFloat() * 0.2 + 1);
                    int price = (int) (ie.baseprice * factor);
                    int price1 = (int) (ie.baseprice * factor * factor1);
                    System.out.println("Item :" + ie.id + ":" + c.getTime() + ":" + price + ":" + price1);
                    
                   
                   
                    sb.append(ie.id).append(",");
                    sb.append(sdf.format(c.getTime())).append(",");
                    sb.append(price).append(",");
                    sb.append(price1).append("");
                    pw.println(sb);
                    
                    sb.setLength(0);
                }
            }
            pw.close();
        }

    }

    public static void main(String[] args) throws Exception
    {
        SynthDatasetGeneratorInward dsg = new SynthDatasetGeneratorInward();
        dsg.generateSynthData("purchasesynth.params", "inward.csv");
    }
}
