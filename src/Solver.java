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
            BufferedReader in = new BufferedReader(new FileReader(info.curtask.findFile("input_0_1.txt")));

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

        int nThreads = (int) lpThread;
        BigInteger n = BigInteger.valueOf(ln);
        // Dividing the line of mod to intervals
        System.out.println("Before cycle");
        for(int i = 0; i < nThreads; i++)
        {
            BigInteger tl = n.multiply(BigInteger.valueOf(i)).divide(BigInteger.valueOf(nThreads));
            BigInteger tr = n.multiply(BigInteger.valueOf(i).add(BigInteger.valueOf(1))).divide(BigInteger.valueOf(nThreads)).subtract(BigInteger.valueOf(1));

            left.add(tl);
            right.add(tr);
        }

        List <point> points = new ArrayList<point>();
        List <channel> channels = new ArrayList<channel>();
        // Connection to points
        for(int i = 0; i < nThreads; i++)
        {
            BigInteger tl = n.multiply(BigInteger.valueOf(i)).divide(BigInteger.valueOf(nThreads));
            BigInteger tr = n.multiply(BigInteger.valueOf(i).add(BigInteger.valueOf(1))).divide(BigInteger.valueOf(nThreads)).subtract(BigInteger.valueOf(1));

            points.add(info.createPoint());
            System.out.println(points);
            channels.add(points.get(i).createChannel());
            points.get(i).execute("ShanksAlgorithm");
            channels.get(i).write(la);
            channels.get(i).write(lb);
            channels.get(i).write(ln);

            channels.get(i).write(tl.longValue());
            channels.get(i).write(tr.longValue());
        }

        /*
        * for(int i = 0; i.compareTo(nThreads) == -1; i++)
        {
            BigInteger tl = n.multiply(i).divide(nThreads);
            BigInteger tr = n.multiply(i.add(BigInteger.valueOf(1))).divide(nThreads).subtract(BigInteger.valueOf(1));

            int intI = i.intValue();
            System.out.println(intI);
            System.out.println("Something unknown2");
            points.add(info.createPoint());
            points.add(info.createPoint());
            System.out.println(points);
            channels.add(points.get(0).createChannel());
            points.get(0).execute("ShanksAlgorithm");
            channels.get(0).write(la);
            channels.get(0).write(lb);
            channels.get(0).write(ln);

            channels.get(0).write(tl.longValue());
            channels.get(0).write(tr.longValue());

            channels.add(points.get(1).createChannel());
            points.get(1).execute("ShanksAlgorithm");
            channels.get(1).write(la);
            channels.get(1).write(lb);
            channels.get(1).write(ln);

            channels.get(1).write(tl.longValue());
            channels.get(1).write(tr.longValue());
        }
        *
        * */

        // Mapping results
        for(int i = 0; i < lpThread; i++)
        {
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
