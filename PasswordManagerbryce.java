import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PasswordManagerbryce {
    private static SecretKey secretKey;
    private static Map<String, String> passwordMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        // Generate a new AES key
        secretKey = generateKey();

        Scanner scanner = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("Options: [add, get, list, exit]");
            choice = scanner.nextLine();

            switch (choice) {
                case "add":
                    addPassword(scanner);
                    break;
                case "get":
                    getPassword(scanner);
                    break;
                case "list":
                    listAll();
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static SecretKey generateKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        return generator.generateKey();
    }

    private static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static String decrypt(String encryptedText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    private static void addPassword(Scanner scanner) throws Exception {
        System.out.println("Enter platform name: ");
        String platform = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        String encryptedPassword = encrypt(password);
        passwordMap.put(platform, encryptedPassword);

        System.out.println("Password added successfully!");
    }

    private static void getPassword(Scanner scanner) throws Exception {
        System.out.println("Enter platform name to retrieve the password: ");
        String platform = scanner.nextLine();

        if (passwordMap.containsKey(platform)) {
            System.out.println("Decrypted password: " + decrypt(passwordMap.get(platform)));
        } else {
            System.out.println("No password found for the given platform.");
        }
    }

    private static void listAll() {
        System.out.println("All platforms:");
        for (String platform : passwordMap.keySet()) {
            System.out.println(platform);
        }
    }
}
