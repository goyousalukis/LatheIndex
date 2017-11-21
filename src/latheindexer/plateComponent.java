/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package latheindexer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import java.util.prefs.*;
//mport static javafx.scene.paint.Color.color;
//import static javafx.scene.paint.Color.color;
import javax.imageio.ImageIO;


/**
 *
 * @author winsock
 */
public class plateComponent extends JComponent{
    private Preferences prefs;
    BufferedImage img = null;
  
    private int aRadius;
    private int aDivisions;
    private int aPhasing;
    private int aIndex;
    private int aCurPhase;
    private String aInfo;
    
    public plateComponent() {
        //this.setPreferredSize(new Dimension(600,1000));
        try
            {
                img = ImageIO.read(new File("disc.png"));
            } catch (IOException e){
                System.out.println("EXCEPTION");
            }
        this.setToolTipText("This istooTip text!");
        this.setBorder(BorderFactory.createTitledBorder("Indexing Plate"));
        prefs = Preferences.userNodeForPackage(this.getClass());
        aRadius = prefs.getInt("RADIUS", 100);
        aDivisions = prefs.getInt("DIVISIONS", 24);
        aPhasing = prefs.getInt("PHASING",1);
        aIndex = prefs.getInt("INDEX",0);
        aCurPhase = prefs.getInt("CURPHASE", 0);
        aInfo = prefs.get("INFO", "Empty");
        
        System.out.println(this.getClass().getName());
       // System.out.println(aInfo);
    }
    
    public int getRadius()
    {
        return aRadius;
    }
    
    public void setRadius(int myRadius)
    {
        aRadius = myRadius;
        prefs.putInt("RADIUS", myRadius);
       this.repaint();
    }

    public int getDivisions()
    {
        return aDivisions;
    }
    
    public void setDivisions(int myDivisions)
    {
        aDivisions = myDivisions;
        prefs.putInt("DIVISIONS", myDivisions);
        prefs.putInt("INDEX", 0);
       this.repaint();
    }  
    
        public int getPhasing()
    {
        return aPhasing;
    }
    
    public void setPhasing(int myPhasing)
    {
        aPhasing = myPhasing;
        prefs.putInt("PHASING", myPhasing);
       this.repaint();
    } 

        public int getIndex()
    {
        return aIndex;
    }
    
    public void setIndex(int myIndex)
    {
        aIndex = myIndex;
        prefs.putInt("INDEX", myIndex);
       this.repaint();
    }     
    
        public int getCurPhase()
    {
        return aCurPhase;
    }
    
    public void setCurPhase(int myCurPhase)
    {
        aCurPhase = myCurPhase;
        prefs.putInt("CURPHASE", myCurPhase);
       this.repaint();
    } 
    
    public String getInfo()
    {
        return aInfo;
    }
    
    public void setInfo(String myInfo)
    {
        System.out.println("setInfo Called");
        if (myInfo == "XXX")
        {
            aInfo = "";
            prefs.put("INFO", aInfo);
        }
        else
        {
            aInfo = aInfo + myInfo;
            prefs.put("INFO", aInfo);
        }
       this.repaint();
    }    
    
    
    
    public void drawCenteredCircle(Graphics2D g, double x, double y, int r) {
        x = x-(r/2);
        y = y-(r/2);
        g.draw(new Ellipse2D.Double(x,y,r,r));
       }
    public void fillCenteredCircle(Graphics2D g, double x, double y, int r) {
        x = x-(r/2);
        y = y-(r/2);
        g.fill(new Ellipse2D.Double(x,y,r,r));
       }
    
    public BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
        BufferedImage dbi = null;
        if (sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }
    
        @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = getSize();
        int centerX = d.width / 2;
        int centerY = d.width / 2;
        //double aRatio = ((aRadius+60)*2)/(1.0 * img.getWidth());
        //BufferedImage img2 = scale(img,img.getType(),(aRadius+60)*2,(aRadius+60)*2,aRatio,aRatio);
        //g2.drawImage(img2,centerX-(img2.getWidth()/2),centerY-(img2.getHeight()/2),this);
        int phasedDivisions = aDivisions * aPhasing;
        //System.out.println(aPhasing);
        double newDiv = phasedDivisions;
        double divAngle = 360  / newDiv;
        //System.out.println("Div angle is:" + divAngle);
        g2.setColor(Color.white);
        g2.setPaint(new GradientPaint(0,0,Color.DARK_GRAY, 1000, 0 ,Color.WHITE));
        fillCenteredCircle(g2, centerX, centerY, 2 * (aRadius +15));
        g2.setColor(Color.BLACK);
        
        for (int div = 0; div <phasedDivisions; div++)
        {
            //System.out.println("Div*DivAngle" + (div*divAngle));
            double angle = Math.toRadians((div*divAngle)-90);
            double x = aRadius * Math.cos(angle) + centerX;
            double y = aRadius * Math.sin(angle) + centerY;
           if ((div%aPhasing)==0) fillCenteredCircle(g2, x, y, 10);
           else drawCenteredCircle(g2, x, y, 10);
        }
        g2.setStroke(new BasicStroke(5));
   
        newDiv = aDivisions;
        divAngle = 360 / newDiv;
        //System.out.println("New Div Angle is :" + divAngle);
        double phaseOffset = (divAngle / (aPhasing));
       // System.out.println("Phase Offset Angle" + phaseOffset);
        double angle;
        if (aCurPhase == 0) 
        {angle = Math.toRadians((aIndex * divAngle)-90);}
        else
        {angle = Math.toRadians((aIndex * divAngle) + (phaseOffset*(aCurPhase))  -90);}
       //System.out.println("Angle:" + (aIndex * divAngle));
        double x = aRadius * Math.cos(angle) + centerX;
        double y = aRadius * Math .sin(angle) + centerY;
        int x1 = (int) Math.round(x);
        int y1 = (int) Math.round(y);
        g2.drawLine(centerX, centerY, x1, y1 );
        g2.setStroke(new BasicStroke(1));

       
    }
    
    
}
