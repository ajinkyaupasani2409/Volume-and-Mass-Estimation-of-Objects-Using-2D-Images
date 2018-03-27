/*
 * StrokedShapeSliderUI.java
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/** I also made a <code>StrokeShapeSliderUI</code> which is similar but only
 * uses one shape. It uses filled and not filled shapes to denote the value,
 * much like the Netflix website.  However to build a replacement for their
 * control would require creating a new model class and adding some more
 * methods to a subclass of <code>JSlider</code>. This because the Netflix
 * widget also displays the type of rating (User, System Average, and Friends
 * Rating) by the color used to fill the shape. It also has an optional button
 * to indicate no interest.
 *
 * @author Adam Walker
 */
public class StrokedShapeSliderUI extends ShapeSliderUI{
    public StrokedShapeSliderUI(){
    }
    public static ComponentUI createUI(JComponent c){
        return new StrokedShapeSliderUI();
    }
    public void paintShape(Graphics2D g, double percent, boolean enabled){
        Shape s = getPrimaryShape();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        if(percent>0.0){
            g.setColor(enabled?getPrimaryColor():getSecondaryColor());
            g.fill(s);
            g.setColor(getSecondaryColor());
            g.draw(s);
        }else{
            if(!enabled)
                return;
            g.setColor(getSecondaryColor());
            g.draw(s);
        }
        
    }
    public void installColors(){
        setPrimaryColor(Color.YELLOW);
        setSecondaryColor(Color.BLACK);
        super.installColors();
    }
}
