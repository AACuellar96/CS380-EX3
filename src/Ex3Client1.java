import java.io.*;
import java.net.Socket;
public final class Ex3Client1 {
    public static void main(String[] args) throws Exception {
            System.out.println("Connected to server.");
            int size = 210;
            System.out.println("Reading "+size+" bytes.");
            byte[] holder = new byte[size];
            int place=0;
            String hexV = "34492FFDD26689239C9A5C3F8EDDBFADC0DA166C5730702B13A1C283650746D9EBFDE4DCC95283FBFCF05A966468DB03E1CB354116EB49F2A511D548F89DEB1763527ABAA337B9257CBA1EBAC7BCD457EF9E9E3FA3AE1996D5CC6D4125C13CA01DD862E1ACA5F674EAA549C7CC1269C44B09E23D875EA863230D4919CC0FDE787C84476E478DB1959842CA84C0D2776A7DFE5DEE9CAB2625D3F13E4542A88C3549C9B1AA68ADE663DA0CFEDBA0E7BCB4AED0B12992EAD1206BD32946CF221B492F3DD12C1B52A55D898A58C99FE192382CE1";
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
            if(cSum>=Short.MAX_VALUE){
                cSum-=(Short.MAX_VALUE);
            }
        }
        short res = (short) cSum;
        return (short) (cSum & 0xFFFF);
    }
}
