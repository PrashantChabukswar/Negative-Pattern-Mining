package com.mycompany.npm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SynthDatasetGeneratorOutward
{

    Map<Integer, List<int[]>> month = new HashMap<Integer, List<int[]>>();
    Map<Integer, List<int[]>> year = new HashMap<Integer, List<int[]>>();
    String paramfile;
    private String datafile;
    private int itemcount;
    private int transactioncount;
    private int y;
    private int support;

    public void generateSynthData(String paramfile, String datafile) throws Exception
    {
	this.paramfile = paramfile;
	this.datafile = datafile;
	loadParams();
	generateDataset();
    }

    private void loadParams() throws Exception
    {
	BufferedReader br = new BufferedReader(new FileReader(paramfile));
	String line = null;
	while ((line = br.readLine()) != null)
	{
	    String[] parts = line.split("[\t]+");
	    String type = parts[0];
	    if (type.equalsIgnoreCase("items"))
	    {
		this.itemcount = Integer.parseInt(parts[1]);
	    }
	    else if (type.equalsIgnoreCase("transactions"))
	    {
		this.transactioncount = Integer.parseInt(parts[1]);

	    }
	    else if (type.equalsIgnoreCase("support"))
	    {
		this.support = Integer.parseInt(parts[1]);
	    }
	    else if (type.equalsIgnoreCase("month"))
	    {
		int m = Integer.parseInt(parts[1]);
		y = Integer.parseInt(parts[2]);
		String[] items = parts[3].split(",");
		int[] item = new int[items.length];
		for (int i = 0; i < item.length; i++)
		{
		    item[i] = Integer.parseInt(items[i]);
		}
		System.out.println("Month:" + m + ":" + Arrays.toString(item));
		List<int[]> is = month.get(m);
		if (is == null)
		{
		    is = new ArrayList<int[]>();
		    month.put(m, is);
		}
		is.add(item);
	    }
	    else if (type.equalsIgnoreCase("year"))
	    {
		int n = Integer.parseInt(parts[1]);
		String[] items = parts[2].split(",");
		int[] item = new int[items.length];
		for (int i = 0; i < item.length; i++)
		{
		    item[i] = Integer.parseInt(items[i]);
		}
		List<int[]> is = year.get(n);
		if (is == null)
		{
		    is = new ArrayList<int[]>();
		    year.put(n, is);
		}
		is.add(item);
	    }
	}
	br.close();
    }

    private void generateDataset() throws Exception
    {
	Random random = new Random();
	PrintWriter pw = new PrintWriter(new File(datafile));
	Calendar c = Calendar.getInstance();
	c.set(Calendar.YEAR, y);
	c.set(Calendar.MONTH, 0);
	c.set(Calendar.DAY_OF_MONTH, 1);

	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	StringBuilder sb = new StringBuilder();
	while (c.get(Calendar.YEAR) == y)
	{
	    int i = 0;
	    List<int[]> list = month.get(c.get(Calendar.MONTH));
	    List<int[]> list2 = year.get(y);
	    if (list != null)
	    {
		for (; i <= transactioncount * support / 100; i++)
		{
		    sb.setLength(0);
		    sb.append(sdf.format(c.getTime())).append(",");
		    int[] t = new int[itemcount];
		    for (int j : list.get(i % list.size()))
		    {
			t[j - 1] = 1;
		    }
		    for (int j : t)
		    {
			sb.append(j).append(",");
		    }
		    sb.deleteCharAt(sb.length() - 1);
		    pw.println(sb);
		}
	    }
	    if (list2 != null)
	    {
		for (int k=0; k <= transactioncount * support / 100 / 12; i++,k++)
		{
		    sb.setLength(0);
		    sb.append(sdf.format(c.getTime())).append(",");
		    int[] t = new int[itemcount];
		    for (int j : list2.get(i % list2.size()))
		    {
			t[j - 1] = 1;
		    }
		    System.out.println(sb+":"+Arrays.toString(t));
		    for (int j : t)
		    {
			sb.append(j).append(",");
		    }
		    sb.deleteCharAt(sb.length() - 1);
		    pw.println(sb);
		}
	    }
	    for (; i < transactioncount; i++)
	    {
		sb.setLength(0);
		sb.append(sdf.format(c.getTime())).append(",");
		for (int j = 0; j < itemcount; j++)
		{
		    sb.append(random.nextInt(100) < 10 ? 1 : 0).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		pw.println(sb);
	    }
	    c.add(Calendar.DAY_OF_MONTH, 1);
	}
	pw.close();
    }

    public static void main(String[] args) throws Exception
    {
	new SynthDatasetGeneratorOutward().generateSynthData("synth.params", "dataset.csv");
    }
}