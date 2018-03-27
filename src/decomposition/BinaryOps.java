package decomposition;

/**
 *

Calculate power and log functions using fast
binary operations.

 */
public class BinaryOps {

    /**
     *
    Calculate 2<sup>n</sup> where n >= 0

    @param n
    a value between 0..31

    @return power2 returns 2<sup>n</sup>

     */
    public static int power2(byte n) {
        int rslt = 0x1 << (n & 0x1f);
        return rslt;
    } // power2

    /**
     *
    <p>
    Calculate floor( log<sub>2</sub>( val ) ), where val > 0
    <p>
    The log function is undefined for 0.

    @param val
    a positive value

    @return <tt>floor( log<sub>2</sub>( val ) )</tt>

     */
    public static byte log2(int val) {
        byte log;

        for (log = 0; val > 0; log++, val = val >> 1){}
            log--;
        

        return log;
    } // log2

    /**
    nearestPower2
    <p>
    Given a value "val", where val > 0, nearestPower2 returns
    the power of 2 that is less than or equal to val.

     */
    public static int nearestPower2(int val) {
        byte l = log2(val);
        int power = power2(l);

        return power;
    } // nearestPower2
} // class binary

