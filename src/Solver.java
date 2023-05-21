import java.io.*;
import java.math.*;
import java.util.*;

import parcs.*;

public class Solver implements AM
{

    public static void main(String[] args)
    {
        System.out.println("The Solver class start method main");

        task curtask = new task();
        curtask.addJarFile("Solver.jar");
        curtask.addJarFile("ShanksAlgorithm.jar");

        System.out.println("The Solver class method main adder jar files");

        (new Solver()).run(new AMInfo(curtask, (channel)null));

        System.out.println("The Solver class method main finish work");
        curtask.end();
    }

    public void run(AMInfo info)
    {
        long lp, la, lb, ln;

        try
        {
            BufferedReader in = new BufferedReader(new FileReader(info.curtask.findFile("input_1.txt")));

            lp = Long.parseLong(in.readLine());
            la = Long.parseLong(in.readLine());
            lb = Long.parseLong(in.readLine());
            ln = Long.parseLong(in.readLine());
        }
        catch (IOException e)
        {
            System.out.print("Reading input data error\n");
            e.printStackTrace();
            return;
        }

        System.out.println("The Solver class have read data from the parent server");
        System.out.println("alpha = " + la);
        System.out.println("beta = " + lb);
        System.out.println("n = " + ln);
        // Time counting
        long tStart = System.nanoTime();
        System.out.println(tStart);
        long res = solve(info, lp, la, lb, ln);

        long tEnd = System.nanoTime();
        // Printing results
        if(res == -1)
        {
            System.out.println("There is no solution for: " + la + " ^ x = " + lb + " (mod " + ln + ")");
        }
        else
        {
            System.out.println("" + la + " ^ " + res + " = " + lb + " (mod " + ln + ")");
            System.out.println("x = " + res);
        }
        System.out.println("Working time on " + lp + " processes: " + ((tEnd - tStart) / 1000000) + "ms");
    }

    static public long solve(AMInfo info, long lpThread, long la, long lb, long ln)
    {
        List<BigInteger> left = new ArrayList<>();
        List<BigInteger> right = new ArrayList<>();
        List<Long> solution = new ArrayList<>();

        BigInteger nThreads = BigInteger.valueOf(lpThread);
        BigInteger n = BigInteger.valueOf(ln);
        // Dividing the line of mod to intervals
        System.out.println("Before cycle");
        for(BigInteger i = BigInteger.valueOf(0); i.compareTo(nThreads) == -1; i = i.add(BigInteger.valueOf(1)))
        {
            BigInteger tl = n.multiply(i).divide(nThreads);
            BigInteger tr = n.multiply(i.add(BigInteger.valueOf(1))).divide(nThreads).subtract(BigInteger.valueOf(1));

            left.add(tl);
            right.add(tr);
        }

        List <point> points = new ArrayList<point>();
        List <channel> channels = new ArrayList<channel>();
        // Connection to points
        for(BigInteger i = BigInteger.valueOf(0); i.compareTo(nThreads) == -1; i = i.add(BigInteger.valueOf(1)))
        {
            BigInteger tl = n.multiply(i).divide(nThreads);
            BigInteger tr = n.multiply(i.add(BigInteger.valueOf(1))).divide(nThreads).subtract(BigInteger.valueOf(1));

            int intI = i.intValue();
            System.out.println(intI);
            System.out.println("Something unknown2");
            points.add(info.createPoint());
            System.out.println(points);
            channels.add(points.get(intI).createChannel());
            points.get(intI).execute("ShanksAlgorithm");
            channels.get(intI).write(la);
            channels.get(intI).write(lb);
            channels.get(intI).write(ln);

            channels.get(intI).write(tl.longValue());
            channels.get(intI).write(tr.longValue());
        }
        // Mapping results
        for(int i = 0; i < lpThread; i++)
        {
            System.out.println("perevirka");
            solution.add(channels.get(i).readLong());
        }
        // Finding the solution
        long result = -1;
        for (Long aLong : solution) {
            if (aLong != -1) {
                result = aLong;
            }
        }

        return result;
    }
}
