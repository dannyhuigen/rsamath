import java.util.ArrayList;
import java.util.Random;

class RsaHelper {

    static int getPFromN(int n) {
        int p = 2;

        while (n % p > 0 && p <= n){ p++; }

        return p;
    }

    static int getQFromPAndN(int n, int p) {
        return n / p;
    }

    static int calcPhi(int p, int q) { return (p-1)*(q-1);   }

    /**
     * Check if given num is prime (simple check)
     * @param num the number
     * @return prime or not
     */
    static boolean isPrime(int num) {
        for (int i = 2; i <= num / 2; ++i) {
            // condition for nonprime number
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    static int getRandomE(int p, int q) {
        // Calculate phi
        int phi = calcPhi(p, q);

        // Get all possible values of e
        ArrayList<Integer> allPossibleE = getAllPossibleE(phi);

        // Select a random E index
        int randomEIndex = new Random().nextInt(allPossibleE.size());

        // Return random E value
        return allPossibleE.get(randomEIndex);
    }

    static ArrayList<Integer> getAllPossibleE(int phi) {
        ArrayList<Integer> possibleE = new ArrayList<>();

        int temp = 2;

        while (temp < phi) {
            temp++;
            if (gcd(temp, phi) == 1) possibleE.add(temp);
        }

        return possibleE;
    }

    private static int gcd(int p, int q) {
        if (p == 0) return q;
        return gcd(q % p, p);
    }

    static int modInverse(int a, int m) {
        for (int x = 1; x < m; x++) {
            if (((a%m) * (x%m)) % m == 1) return x;
        }

        return 0;
    }
}
