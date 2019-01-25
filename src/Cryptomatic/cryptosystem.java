package Cryptomatic;

import java.io.*;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Scanner;

/**
 * File locations
 * D:\Jacob\School\Spring 2019\CS430\assignments\1\encrypted.txt
 * D:\Jacob\School\Spring 2019\CS430\assignments\1\plaintext.txt
 * D:\Jacob\School\Spring 2019\CS430\assignments\1\keys.txt
 * D:\Jacob\Java-Projects\CyberSecurity\Encrypted2.txt
 */

public class cryptosystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        String key;
        File file;

        while (loop) {
            //Menu
            System.out.println("Please select an option \n" +
                    "1: Encrypt\n" +
                    "2: Decrypt\n" +
                    "3: Brute Force\n" +
                    "4: Exit\n");
            String option = sc.nextLine();
            switch (option) {
                case "1":
                    System.out.println("Enter File location: ");
                    file = new File(sc.nextLine());
                    System.out.println("Enter a key consisting of two letters: ");
                    key = sc.nextLine();
                    encrypt(file, key);
                    break;
                case "2":
                    System.out.println("Enter File location: ");
                    file = new File(sc.nextLine());
                    System.out.println("Enter the key: ");
                    key = sc.nextLine();
                    decrypt(file, key);
                    break;
                case "3":
                    System.out.println("Enter File location: ");
                    file = new File(sc.nextLine());
                    bruteForce(file);
                    break;
                case "4":
                    loop = false;
                    break;
                //End Menu
            }
        }
    }

    private static void bruteForce(File file) {
        HashSet<String> dictionary = new HashSet<>();
        String finalKey = "";
        int countMax = 0;
        /**
         * Creates HashSet of dictionary
         * o(1) for .contains
         */
        try{
            FileReader fileReader = new FileReader("D:\\Jacob\\Java-Projects\\CyberSecurity\\google-english.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                dictionary.add(line.trim().toLowerCase());
            }
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            /**
             * All possible keys on keyboard
             * converted to byte array
             */
            String possibleKeys = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
            byte[] pKeys = possibleKeys.getBytes();
            byte[] tempKey = new byte[2];

            /**
             * Reads the file and converts to byte array
             * outPut is the byte array after the XOR function
             */
            long start = System.currentTimeMillis();
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            byte[] outPut = new byte[fileBytes.length];

            /**
             * Loop for all possibleKeys to be tried.
             * Within is the XOR function
             */
            for( int i = 0; i < pKeys.length; i++){
                for(int k = 0; k < pKeys.length; k++){
                    tempKey[0] = pKeys[i];
                    tempKey[1] = pKeys[k];
                    /**
                     * XOR Between tempKey and file
                     * outPut Holds the XOR string
                     */
                    for (int l = 0; l < fileBytes.length; l++) {
                        if (l % 2 == 0)
                            outPut[l] = (byte) (tempKey[0] ^ fileBytes[l]);
                        else
                            outPut[l] = (byte) (tempKey[1] ^ fileBytes[l]);
                    }
                    String tempOut = new String(outPut);

                    /**
                     * Checks Each word from file with dictionary
                     *
                     */
                    Scanner sc = new Scanner(tempOut.trim().toLowerCase());
                    int count = 0;
                    while(sc.hasNext()){
                        if(dictionary.contains(sc.next())){
                            count++;
                        } else{
                            count--;
                        }
                        if(count<-1)break;

                    }
                    /**
                     * String with highest word frequency is kept in countMax.
                     * finalKey stores the probable key and then decrypts it.
                     */
                    if (count > countMax){
                        countMax = count;
                        finalKey = "" + (char)tempKey[0] + (char)tempKey[1];
                        System.out.println(finalKey);
                    }
                }
            }

            //Decrypt using final key.
            decrypt(file,finalKey);
            long end = System.currentTimeMillis();
            long total = end - start;
            System.out.println("Time for Decode: " + total);
            System.out.println("The key should be: " + finalKey);

        } catch (IOException e){

        }


    }

    private static void decrypt(File file, String key)  {
        try {
            /**
             * Reads the file and converts to byte array
             * outPut is the byte array after the XOR function
             */
            byte[] fileBytes = Files.readAllBytes(file.toPath());

            /**
             * If the file has an uneven number of bytes we pad it with a space at the end.
             * ASCII space = 32
             */
            if (fileBytes.length % 2 != 0) {
                byte[] fileTemp = new byte[fileBytes.length + 1];
                for (int i = 0; i < fileBytes.length; i++) {
                    fileTemp[i] = fileBytes[i];
                }
                fileTemp[fileTemp.length - 1] = 32;
                fileBytes = fileTemp;
            }
            //System.out.println(fileBytes.length);
            /**
             * Grabs The keys from the user input
             */
            byte[] kTemp = key.getBytes();
            byte key1 = kTemp[0];
            byte key2 = kTemp[1];
            /**
             * The XOR function between the keys and file
             */
            byte[] outPut = new byte[fileBytes.length];
            for (int i = 0; i < fileBytes.length; i++) {
                if (i % 2 == 0)
                    outPut[i] = (byte) (key1 ^ fileBytes[i]);

                else
                    outPut[i] = (byte) (key2 ^ fileBytes[i]);
            }
            /**
             * Prints file to terminal
             * Creation of file as well
             */
            System.out.println(new String(outPut));
            try (FileOutputStream fileOutputStream = new FileOutputStream("Decrypted2.txt")) {
                fileOutputStream.write(outPut);
                fileOutputStream.close();
            }


        } catch (IOException e) {

        }
    }

    public static void encrypt(File file, String key) {
        try {
            /**
             * Reads the file and converts to byte array
             * outPut is the byte array after the XOR function
             */
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            /**
             * If the file has an uneven number of bytes we pad it with a space at the end.
             * ASCII space = 32
             */
            if (fileBytes.length % 2 != 0) {
                byte[] fileTemp = new byte[fileBytes.length + 1];
                for (int i = 0; i < fileBytes.length; i++) {
                    fileTemp[i] = fileBytes[i];
                }
                fileTemp[fileTemp.length - 1] = 32;
                fileBytes = fileTemp;
            }
            //System.out.println(fileBytes.length);
            /**
             * Grabs The keys from the user input
             */
            byte[] kTemp = key.getBytes();
            byte key1 = kTemp[0];
            byte key2 = kTemp[1];
            /**
             * The XOR function between the keys and file
             */
            byte[] outPut = new byte[fileBytes.length];
            for (int i = 0; i < fileBytes.length; i++) {

                if (i % 2 == 0)
                    outPut[i] = (byte) (key1 ^ fileBytes[i]);

                else
                    outPut[i] = (byte) (key2 ^ fileBytes[i]);
            }
            /**
             * Prints file to terminal
             * Creation of file as well
             */
            System.out.println("The file is encrypted");
            try (FileOutputStream fileOutputStream = new FileOutputStream("Encrypted2.txt")) {
                fileOutputStream.write(outPut);
                fileOutputStream.close();
            }

        } catch (IOException e) {

        }
    }
}

