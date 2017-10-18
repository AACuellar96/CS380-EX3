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
            System.out.print("Data received: ");
            byte[] holder = new byte[size];
            for(int i=0;i<size;i++){
                if(i%10==0)
                    System.out.println("");
                int val = is.read();
                System.out.print(Integer.toHexString(val).toUpperCase());
                holder[i]=(byte) val;
            }
            System.out.println("");
            int cSum =checksum(holder);
            String hex = Integer.toHexString(cSum);
            if(hex.length()>4)
                hex=hex.substring(hex.length()-4);
            else if(hex.length()==3)
                hex="0"+hex;
            else if(hex.length()==2)
                hex="00"+hex;
            holder[0] = (byte) Integer.parseInt(hex.substring(0,2).toUpperCase(),16);
            //Doesnt work for 1 byte?
            try {
                holder[1] = (byte) Integer.parseInt(hex.substring(2).toUpperCase(), 16);
            }
            catch (ArrayIndexOutOfBoundsException e){

            }
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
            try {
                short two = (short) (b[i + 1] & 0xFF);
                cSum += ((256 * one) + two);
                if (cSum >= 65535) {
                    cSum -= (65535);
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                cSum+=(256*one);
                if (cSum >= 65535) {
                    cSum -= (65535);
                }
            }
        }
        return (short) ((~(cSum))& 0xFFFF);
    }
}
