package networktableclient;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import java.net.*;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.*; 

public class NetworkTableClient{

    private static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)){
                if (netint.getName().equals("eth0")) {
                    Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                    for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                        if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException se) {
            // TODO
        }
        return null;
    }


    public static void main(String[] args) {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        inst.startClient(args[0]);
        NetworkTable table = inst.getTable("raspberryPi");
        NetworkTableEntry entry = table.getEntry(getIpAddress());   
        while (true) {
            try {
                Date date = Calendar.getInstance().getTime();  
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
                String strDate = dateFormat.format(date);  
                entry.setValue(strDate);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                entry.setValue("interrupted");
                return;
            }
        }
    }
}