import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JFrame;


public class Gynfont extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; //removes a warning :)
	private static File file;
	private static FileInputStream bytes;

	public void init() {
	      setBackground(Color.white);
	      setForeground(Color.black);
	      setSize(800,100);
	   }
	
	
	public static int readInt() throws IOException{
		return (int)bytes.read();
	}
	
	public static float readFloat() throws IOException{
		byte[] fbytes = new byte[4];
		bytes.read(fbytes);
		for (int i=0;i<4;++i){
			if (fbytes[i]<0) fbytes[i]+=256;
		}
		int asInt = (fbytes[3] & 0xFF) 
	            | ((fbytes[2] & 0xFF) << 8) 
	            | ((fbytes[1] & 0xFF) << 16) 
	            | ((fbytes[0] & 0xFF) << 24);
		return Float.intBitsToFloat(asInt);
	}
	
	 public void paint(Graphics g1){
			file=new File("/home/eri/Dokumenty/gynbook/font.decompressed");
			try {
				bytes=new FileInputStream(file);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 Graphics2D g=(Graphics2D) g1;
		  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	      RenderingHints.VALUE_ANTIALIAS_ON);
	      g.setPaint(Color.black);
	      int n=0;
			while(true){
				int npaths=-1;
				try {
					npaths = readInt();
					if (npaths<1) break;
					System.out.println(n);
					for(int i=0;i<npaths;++i){
						System.out.println(i+" / "+npaths);
						int nnodes=readInt();
						float ox=0;
						float oy=0;
						System.out.println("nodes: "+nnodes);
						if (nnodes<1) {
							npaths=0;
							break;
						}
						for(int j=0;j<nnodes;++j){
							float x=readFloat();
							float y=readFloat();
							System.out.println(j+"\t"+x+"\t"+y);
							x=(float) ((x+n)*20.0+40);
							y=(float) (y*20+40);
							if(j>0){
								g.draw(new Line2D.Double(ox,oy,x,y));
							}
							ox=x;
							oy=y;
						}
					}
				}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						npaths=0;
						break;
						}

				if (npaths<1) break;
				n++;
			}
	 }
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
	

	      JFrame f = new JFrame("Line");
	      f.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent e) {
	            System.exit(0);
	         }
	      });
	      
	      JApplet applet = new Gynfont();
	      f.getContentPane().add("Center", applet);
	      applet.init();
	      f.pack();
	      f.setSize(new Dimension(800, 100));
	      f.setVisible(true);
		
	}

}
