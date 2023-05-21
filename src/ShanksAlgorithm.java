import java.math.*;
import java.util.*;

import parcs.*;

public class ShanksAlgorithm implements AM{
    public void run(AMInfo info)
    {
        long la, lb, ln, ll, lr;
        // Reading data
        la = info.parent.readLong();
        lb = info.parent.readLong();
        ln = info.parent.readLong();
        ll = info.parent.readLong();
        lr = info.parent.readLong();

        System.out.println("The ShanksAlgorithm class have read data from the parent server");
        System.out.println("alpha = " + la);
        System.out.println("beta = " + lb);
        System.out.println("n = " + ln);
        System.out.println("left bound = " + ll);
        System.out.println("right bound = " + lr);
        // Converting to BigInteger
        BigInteger a = BigInteger.valueOf(la);
        BigInteger b = BigInteger.valueOf(lb);
        BigInteger n = BigInteger.valueOf(ln);
        BigInteger l = BigInteger.valueOf(ll);
        BigInteger r = BigInteger.valueOf(lr);

        HashMap<BigInteger, BigInteger> mp = new HashMap<>();

        BigInteger maxRoot = BigInteger.valueOf(10000000);
        BigInteger m = n.sqrt();
        m = m.min(maxRoot);

        // Making map - the first part
        BigInteger x;
        BigInteger gamma = BigInteger.valueOf(1);
        for(BigInteger i = BigInteger.valueOf(1); i.compareTo(m) == -1; i = i.add(BigInteger.valueOf(1)))
        {
            mp.put(gamma, i);
            gamma = gamma.multiply(a).mod(n);
        }

        // Main algorithm - the second part
        BigInteger solutionX = null;
        BigInteger step = a.modInverse(n).modPow(m, n);

        BigInteger j  = BigInteger.valueOf(0);

        for(x = l, gamma = a.modInverse(n).modPow(l, n); x.compareTo(r) <= 0;
            x = x.add(m), gamma = gamma.multiply(step).mod(n))
        {
            if(mp.containsKey(gamma.multiply(b).mod(n)))
            {
                solutionX = x.add(mp.get(gamma.multiply(b).mod(n)));
            }
            j.add(BigInteger.valueOf(1));
        }

        // Returning results
        if(solutionX != null)
        {
            solutionX = solutionX.subtract(j);
            System.out.println("The ShanksAlgorithm class return the result: x = " + solutionX);
            info.parent.write(solutionX.longValue());
        }
        else
        {
            System.out.println("The ShanksAlgorithm class doesn't find any solution");
            info.parent.write(-1L);
        }
    }
}
