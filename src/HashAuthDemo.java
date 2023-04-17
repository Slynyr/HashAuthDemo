import java.security.MessageDigest;
import java.util.Base64;

public class HashAuthDemo {

    private static String[] seeds = {"seed1", "seed2", "seed3"};
    private static String testTokenSeed = "seed3";

    public static void main(String[] args) throws Exception{
        String testToken = generateToken(testTokenSeed);
        System.out.println(validateToken(testToken, seeds));
    }

    //Generates Token
    public static String generateToken(String seed) throws Exception {
        long processId = ProcessHandle.current().pid();
        int cpuTime = (int)Math.floor(System.currentTimeMillis()/100);

        String rawToken = processId + "|" + seed + "|" + cpuTime;
        String token = null;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(rawToken.getBytes());
        token = Base64.getEncoder().encodeToString(hash);
        return token;
    }

    
    //Checks if a token matches any token that is generated by the trusted seeds
    public static boolean validateToken(String token, String[] seeds) throws Exception{ 
        for (String i : seeds){
            if (isValidToken(token, i)) {
                return true;
            }
        }
        return false;
    }

    //Checks if an input token is the same to that of a generated token with the given seed
    public static boolean isValidToken(String token, String seed) throws Exception {
        String generatedToken = generateToken(seed);
        System.out.println(token + " | " + generatedToken);
        return generatedToken.equals(token);
    } 
}
