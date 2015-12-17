import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class disGyn {
	File inFile;
	FileInputStream bytes;
	int n;

	
	public disGyn(String filename)
	{
		inFile=new File(filename);
		 bytes=null;
		 n=0;
	}
	
	String getHex(int pairs)
	{
		String s="";
		try {
		for (int i=0;i<pairs;++i){
			int x;
			x = bytes.read();
			++n;
			s=Integer.toHexString(x)+s;
			if (x<16) s="0"+s;
		}
		s="0x"+s;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;		
	}
	
	String getJump()
	{
		String s="";
		try {
			int x = bytes.read();
			x+=256*bytes.read();
			n=n+2;
			x+=n;
			x%=65536;
			s=Integer.toHexString(x);
			s="0x"+s;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;		
	}
	
	String getReg()
	{

		String s="";
		try {
			Integer x = bytes.read();
			++n;
			s=x.toString()+s;
			s="R"+s;
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return s;
	}
	
	String parseLine()
	{
		try {
			String tmp;
			int x=bytes.read(); //opcode
			++n;
			switch (x){
			case -1:
				return ("END");
		
				
			case 0: //move
				return("VMOV\t"+getReg()+", "+getReg()+"\n");
			case 1: //set
				return("VSET\t"+getReg()+", "+getHex(4)+"\n");
			case 2: //load
				return("VLD\t"+getReg()+", "+getReg()+"\n");	
			case 3: //store
				return("VST\t"+getReg()+", "+getReg()+"\n");
			case 4: //load byte
				return("VLDB\t"+getReg()+", "+getReg()+"\n");	
			case 5: //store byte
				return("VSTB\t"+getReg()+", "+getReg()+"\n");
		
			
			case 16: //add
				return("VADD\t"+getReg()+", "+getReg()+"\n");
			case 17: //subtract
				return("VSUB\t"+getReg()+", "+getReg()+"\n");	
			case 18: //multiply
				return("VMUL\t"+getReg()+", "+getReg()+"\n");
			case 19: //divide
				return("VDIV\t"+getReg()+", "+getReg()+"\n");
			case 20: //modulo
				return("VMOD\t"+getReg()+", "+getReg()+"\n");	
				
			case 21: //or
				return("VOR\t"+getReg()+", "+getReg()+"\n");
			case 22: //and
				return("VAND\t"+getReg()+", "+getReg()+"\n");	
			case 23: //xor
				return("VXOR\t"+getReg()+", "+getReg()+"\n");
			case 24: //not
				return("VNOT\t"+getReg()+"\n");
				
			case 25: //shift left
				return("VSHL\t"+getReg()+", "+getReg()+"\n");	
			case 26: //shift right
				return("VSHR\t"+getReg()+", "+getReg()+"\n");	
					
				
			case 32: //compare
				return("VCMP\t"+getReg()+", "+getReg()+"\n");
				
			case 33: //jump if 0
				return("VJE\t"+getJump()+"\n");	
			case 34: //jump if !0
				return("VJNE\t"+getJump()+"\n");	
			case 35: //jump if carry
				return("VJB\t"+getJump()+"\n");	
			case 36: //jump if not carry
				return("VJAE\t"+getJump()+"\n");	
			case 37: //jump if below or eq
				return("VJBE\t"+getJump()+"\n");	
			case 38: //jump if above
				return("VJA\t"+getJump()+"\n");	
				
				
			case 48: //push
				return("VPUSH\t"+getReg()+"\n");
			case 49: //pop
				return("VPOP\t"+getReg()+"\n");
				

			case 64: //jump
				return("VJMP\t"+getJump()+"\n");
			case 65: //jump to reg
				return("VJMPR\t"+getReg()+"\n");	
			case 66: //call
				return("VCALL\t"+getJump()+"\n");
			case 67: //call from reg
				return("VCALLR\t"+getReg()+"\n");
			case 68: //return
				return("VRET\t\n");
				
				
			case 240: //control register load
				return("VCRL\t"+getHex(2)+", "+getReg()+"\n");
			case 241: //control register store
				return("VCRS\t"+getHex(2)+", "+getReg()+"\n");
				
			case 242: //output byte
				tmp=getReg();
				return("VOUTB\t"+getHex(1)+", "+tmp+"\n");
			case 243: //input byte
				tmp=getReg();
				return("VINB\t"+getHex(1)+", "+tmp+"\n");
				
			case 244: //interrupt return
				return("VIRET\t\n");
			
			
			
			case 255: //power off
				return("VOFF\t\n");
				
				
			default:
				String s="?";
				if(31<x && 127>x) s="\""+(char)(x)+"\"";
				return(s+"\t"+Integer.toHexString(x)+"\n");
			}
				
		} catch (IOException e) {
			e.printStackTrace();
			return ("END");
		}
	}
	
	void parseFile(int off)
	{

		try {
			bytes = new FileInputStream(inFile);
			bytes.skip(off);
			String line="";
			while (line!="END"){
				int m=n;
				line=parseLine();
				System.out.print(Integer.toHexString(m)+"\t"+line);
			}
			bytes.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void parseFile(){
		parseFile(0);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 1 && args.length != 2){
			System.out.println("Usage: dis [offset] <filename>");
			return;
		}
		String path=args[0];
		int offset=0;
		if (args.length == 2){
			path=args[1];
			offset=Integer.parseInt(args[0]);
		}
		disGyn foo=new disGyn(path);
		foo.parseFile(offset);
	}

}
