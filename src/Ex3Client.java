import java.io.*;
import java.net.Socket;
public final class Ex3Client {
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("18.221.102.182",38103)) {
            System.out.println("Connected to server.");
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            BufferedReader brIS = new BufferedReader(new InputStreamReader(System.in));
            PrintStream out = new PrintStream((socket.getOutputStream()),true,"UTF-8");
            int size = is.read();
            System.out.println("Reading "+size+" bytes.");
            byte[] holder = new byte[size];
            for(int i=0;i<size;i++){
                holder[i]=(byte) is.read();
            }
            int cSum =checksum(holder);
            String hex = Integer.toHexString(cSum);
            holder[0] = (byte) Integer.parseInt(hex.substring(0,2).toUpperCase(),16);
            holder[1] = (byte) Integer.parseInt(hex.substring(2).toUpperCase(),16);
            System.out.println("Checksum calculated: 0x"+hex.toUpperCase() );
            out.write(holder);
            if(is.read()==1)
                System.out.println("Response good.");
            else
                System.out.println("Response false.");
            is.close();
            isr.close();
            br.close();
            brIS.close();
            socket.close();
            System.out.println("Disconnected from server.");
        }
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
        return (short) ((~(cSum))& 0xFFFF);
    }
}
