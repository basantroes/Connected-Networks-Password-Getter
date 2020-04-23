import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    //There is no need for global variable, can be local. (Saving memory)

    public static void showPassword(String networkName) {
        String[] command = {"cmd.exe", "/c",
                "netsh wlan show profile key=clear name=\"" + networkName + '"'}; //eg: "SEMICOLON 6408"
        try {
            Process p = new ProcessBuilder(command).start(); //Make p local & using modern ProcessBuilder instead of Runtime
            Scanner cs = new Scanner(p.getInputStream());
            String line = null;

            while (cs.hasNext())
                if ((line = cs.nextLine()).matches("[ ]{4}Key Content[ ]{12}: .*")) //.* = any char
                    break;

            if (line != null) System.out.println("Password:" + line.substring(line.indexOf(':') + 1));

        } catch (IOException e) {
            e.printStackTrace(); //System.err.println(e); //Print error to console
        }
    }

    public static void showAllConnectedNetworks() {
        ArrayList<String> allNetworks = new ArrayList<>();
        try {
            Process p = new ProcessBuilder("cmd.exe", "/c", "netsh wlan show profiles").start();
            Scanner scs = new Scanner(p.getInputStream());
            String line;

            while (scs.hasNext())
                if ((line = scs.nextLine()).matches("[ ]{4}All User Profile[ ]{5}: .*"))
                    allNetworks.add("-" + line.substring(line.indexOf(':') + 1));

            allNetworks.forEach(System.out::println); //Print every network in list by method reference

        } catch (IOException e) {
            e.printStackTrace(); //System.err.println(e); //Print error to console
        }
    }

    public static void main(String[] args) {
        System.out.println("All connected networks are: ");
        showAllConnectedNetworks();

        final Scanner scan = new Scanner(System.in); //Saving memory by declaring once

        while (true) {
            System.out.print("Enter the network name: ");
            String name = scan.nextLine(); //Read the entire network name (eg: SEMICOLON 6408)
            if (name.matches("[ \\n]*")) break; //Prevent infinite loop
            showPassword(name);
            System.out.print("\n");
        }
        scan.close();
    }
}