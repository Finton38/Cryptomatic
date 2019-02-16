package PassCrack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class passCrack {
    //Variables
    private static HashSet<String> dictionary = new HashSet<>();
    private static HashSet<String> mangleDict = new HashSet<>();
    private static ArrayList<user> users = new ArrayList<>();

    private static String Keys = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    private static char[] keys = Keys.toCharArray();

    public static void main(String[] args) {
        loadDictionary();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter file Name: ");
        String fileName = sc.nextLine();

        addUsers(fileName);
        long start = System.currentTimeMillis();
        dictAttack();
        long end = System.currentTimeMillis();
        int count = 0;
        for(user u: users){
            if(!u.isCracked()){
                count++;
            }
        }
        System.out.println("Users accounts Cracked: " + (users.size()-count) + " out of " + users.size());
        System.out.println("Total time taken: " + (end-start));
    }


    //U
    private static void dictAttack() {
        //Mangles Dictionary Word
        for (String y: dictionary) {
            mangler(y, mangleDict);
        }
        //Mangles user account and names
        for(user u: users){
            mangler(u.getAccount(), mangleDict);
            mangler(u.getfName(), mangleDict);
            mangler(u.getlName(), mangleDict);
        }

        //Grabs User Objects and tests with mangleDict
        for(user u: users){
            //Grabs users encrypted password and salt
            String pass = u.getEncryptedPassword();
            String salt = u.getSalt();

            boolean solved = false;
            while(!solved){
                for(String s: mangleDict){
                    String encryptedTemp = jcrypt.crypt(salt,s);
                    if (encryptedTemp.equals(salt+pass)) {
                        System.out.println("Orginal: " + (salt+ pass) + " New " + encryptedTemp);
                        System.out.println("User: " + u.getAccount() + " Password: " + s);
                        u.setCracked(true);
                        break;
                    }
                }
                solved = true;
            }
        }
    }

    private static void mangler(String word, HashSet<String> mangleDict) {
        //NonMangled Word
        mangleDict.add(word);
        //Append/Prepend Char to word.
        for(char c: keys){
            String prepend = c + word;
            String append = word +  c;
            mangleDict.add(prepend);
            mangleDict.add(append);

            capitalize(prepend, mangleDict);
            capitalize(append, mangleDict);
        }
        // Add/Remove first and last char
        mangleDict.add(word.substring(1));
        mangleDict.add(word.substring(0, word.length()-1));
        //Reverse Mangle
        StringBuilder reverse = new StringBuilder();
        reverse.append(word);
        reverse.reverse();
        mangleDict.add(reverse.toString());
        //Duplicate string
        String duplicate = word + word;
        mangleDict.add(duplicate);
        //reflect string
        String reflect1 = word + reverse.toString();
        String reflect2 = reverse.toString() + word;
        mangleDict.add(reflect1);
        mangleDict.add(reflect2);
        //capitalize String
        capitalize(word, mangleDict);
        capitalize(reverse.toString(), mangleDict);
        capitalize(reflect1, mangleDict);
        capitalize(reflect2, mangleDict);
        //1337 Speak Mangling
        mangleDict.add(word.replace('a', '@'));
        mangleDict.add(word.replace('A', '@'));
        mangleDict.add(word.replace('b', '8'));
        mangleDict.add(word.replace('B', '8'));
        mangleDict.add(word.replace('e', '3'));
        mangleDict.add(word.replace('E', '3'));
        mangleDict.add(word.replace('g', '6'));
        mangleDict.add(word.replace('G', '6'));
        mangleDict.add(word.replace('h', '4'));
        mangleDict.add(word.replace('H', '4'));
        mangleDict.add(word.replace('i', '1'));
        mangleDict.add(word.replace('I', '1'));
        mangleDict.add(word.replace('l', '1'));
        mangleDict.add(word.replace('L', '1'));
        mangleDict.add(word.replace('o', '0'));
        mangleDict.add(word.replace('O', '0'));
        mangleDict.add(word.replace('q', '9'));
        mangleDict.add(word.replace('Q', '9'));
        mangleDict.add(word.replace('s', '5'));
        mangleDict.add(word.replace('S', '5'));
        mangleDict.add(word.replace('s', '$'));
        mangleDict.add(word.replace('S', '$'));
        mangleDict.add(word.replace('t', '7'));
        mangleDict.add(word.replace('T', '7'));
        mangleDict.add(word.replace('x', '8'));
        mangleDict.add(word.replace('X', '8'));
        mangleDict.add(word.replace('z', '2'));
        mangleDict.add(word.replace('Z', '2'));
    }

    //Does a mix of capitalization mangling
    private static void capitalize(String word, HashSet<String> mangleDict) {
        //Uppercase and lowercase
        mangleDict.add(word.toLowerCase());
        mangleDict.add(word.toUpperCase());
        //First char CAPS
        mangleDict.add(word.substring(0,1).toUpperCase() + word.substring(1));
        //First char CAPS missing last Char
        mangleDict.add(word.substring(0,1).toUpperCase() + word.substring(word.length()-1));
        //Last char CAPS
        mangleDict.add(word.substring(0, word.length() - 1).toLowerCase()
                + word.substring(word.length() - 1).toUpperCase());
        //First and Last CAPS
        mangleDict.add(word.substring(0,1).toUpperCase() + word.substring(1, word.length()-1).toLowerCase()
        + word.substring(word.length()-1).toUpperCase());
        //All but first CAPS
        mangleDict.add(word.substring(0,1).toLowerCase() + word.substring(1).toUpperCase());
        //All but last CAPS
        mangleDict.add(word.substring(0,word.length()-1).toUpperCase() + word.substring(word.length()-1).toLowerCase());
        //All CAPS except fist and last
        mangleDict.add(word.substring(0,1).toLowerCase() + word.substring(1,word.length()-1).toUpperCase()
        + word.substring(word.length()-1).toLowerCase());
        //Alternating CAPS
        StringBuilder startUP = new StringBuilder();//First letter is uppercase
        StringBuilder startLO = new StringBuilder(); //First letter is lowercase
        for(int i=0; i < word.length(); i++){
            if(i%2 == 0){
                startUP.append(word.substring(i, i + 1).toUpperCase());
                startLO.append(word.substring(i, i + 1).toLowerCase());
            }else{
                startUP.append(word.substring(i, i + 1).toLowerCase());
                startLO.append(word.substring(i, i + 1).toUpperCase());
            }
        }
        mangleDict.add(startUP.toString());
        mangleDict.add(startLO.toString());
    }

    /**
     * Creates user arraylist
     */
    private static void addUsers(String fileName) {

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] arrStr = line.split(":|\\s+"); //Regex splits string by : and white space(For Name)
                user uTemp = new user();
                uTemp.setAccount(arrStr[0]); //accountName
                uTemp.setSalt(arrStr[1].substring(0,2)); //salt
                uTemp.setEncryptedPassword(arrStr[1].substring(2)); //encrypted pass
                uTemp.setfName(arrStr[4]); //first Name
                uTemp.setlName(arrStr[5]); //last Name
                users.add(uTemp);
            }
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    /**
     * Loads Dictionary
     */
    private static void loadDictionary() {
        try{
            FileReader fileReader = new FileReader("D:\\Jacob\\Java-Projects\\CyberSecurity\\wordlist.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                dictionary.add(line);
            }
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
