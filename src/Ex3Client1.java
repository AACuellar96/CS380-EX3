import java.io.*;
import java.net.Socket;
public final class Ex3Client1 {
    public static void main(String[] args) throws Exception {
            System.out.println("Connected to server.");
            int size = 6;
            System.out.println("Reading "+size+" bytes.");
            byte[] holder = new byte[size];
            int place=0;
            String hexV = "F08F7807236C";
            for(int i=0;i<size;i++){
                holder[i]=(byte) Integer.parseInt(hexV.substring(place,place+2),16);
                place+=2;
            }
            int cSum =checksum(holder);
            String hex = Integer.toHexString(cSum);
            holder[0] = (byte) Integer.parseInt(hex.substring(0,2).toUpperCase(),16);
            holder[1] = (byte) Integer.parseInt(hex.substring(2).toUpperCase(),16);
            System.out.println("Checksum calculated: 0x"+hex.toUpperCase() );
            System.out.println("Disconnected from server.");
    }
    public static short checksum(byte[] b){
        int cSum = 0;
        for(int i=0;i<b.length;i+=2){
            short one = (short) (b[i] & 0xFF);
            short two = (short) (b[i+1] & 0xFF);
            cSum += ((256*one)+ two);
            if(cSum>=65535){
                cSum-=(65535);
            }
        }
        System.out.println(cSum);
      //  0x73FC
        return (short) (~(cSum & 0xFFFF));
    }
}
