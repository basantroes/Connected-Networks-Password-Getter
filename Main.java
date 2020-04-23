import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static Process p;

    public static void showPassword(String networkName) {
        String command = "cmd.exe /c  netsh wlan show profiles key=clear name=" + networkName;
        try {
            p = Runtime.getRuntime().exec(command);
            Scanner cs = new Scanner(p.getInputStream());
            String line = "";

            while (cs.hasNext()) {
                line += cs.nextLine();
                line += "\n";
            }

            String s = line.substring(line.indexOf("Key Content") + 25);
            System.out.print("Password: " + s.substring(0, s.indexOf("\n")));
            //  String[] arr=s.split()

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAllConnectedNetworks() {
        ArrayList<String> allNetworks = new ArrayList<>();
        try {
            p = Runtime.getRuntime().exec("cmd.exe /c netsh wlan show profiles");
            Scanner scs = new Scanner(p.getInputStream());
            String line = "";
            int lineNo = 0;

            while (scs.hasNext()) {
                line += scs.nextLine() + "\n";
                lineNo++;
            }

            for (int i = 0; i < lineNo + 1; i++) {
                if (line.contains("All User Profile")) {
                    String recS = line.substring(line.indexOf("All User Profile") + 22);
                    allNetworks.add(recS.substring(0, recS.indexOf("\n")));
                    System.out.println(allNetworks.get(i));
                    line = recS;
                } else break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        System.out.println("All connected networks are: ");
        showAllConnectedNetworks();

        while (true) {
            System.out.println("Enter the  network name from the previous list: ");
            Scanner scan = new Scanner(System.in);
            String name = scan.next();
            showPassword(name);
            System.out.print("\n");
        }
    }
}