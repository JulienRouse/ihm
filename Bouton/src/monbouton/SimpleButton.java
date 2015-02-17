package monbouton;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.transitions.LeaveOnTag;
import fr.lri.swingstates.canvas.transitions.PressOnTag;
import fr.lri.swingstates.canvas.transitions.ReleaseOnTag;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.canvas.transitions.EnterOnTag;
import fr.lri.swingstates.debug.StateMachineVisualization;

import javax.swing.JFrame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 * 
 */
public class SimpleButton {

	private CText label;
	private CRectangle rect;
	private CExtensionalTag label_associate;
	private static CExtensionalTag react_click;

	SimpleButton(Canvas canvas, String text) {
		//init pour les groupes de tags
		label_associate = new CExtensionalTag(canvas) {};
		react_click = new CExtensionalTag() {};
		
		//on rajoute des formes sur le canva
		label = canvas.newText(0, 0, text, new Font("verdana", Font.PLAIN, 12));
		rect = canvas.newRectangle(-5, -5, 50, 25);
		
		//specification de l'ordre d'affichage
		label.above(rect).setPickable(false);
		//on rajoute des tags sur ces formes
		label.addTag(label_associate);
		rect.addTag(label_associate);
		
		rect.addTag(react_click);
		
		
		
	}

	public void action() {
		System.out.println("ACTION!");
	}

	public CShape getShape() {
		return label;
	}
	
	public CExtensionalTag getTag(){
		return label_associate;
	}
	
	public static  CExtensionalTag getClickTag(){
		return react_click;
	}

	static public void main(String[] args) {
		JFrame frame = new JFrame();
		Canvas canvas = new Canvas(400, 400);
		frame.getContentPane().add(canvas);
		frame.pack();
		frame.setVisible(true);
		
		SimpleButton simple = new SimpleButton(canvas, "simple");
		simple.getTag().translateBy(100, 100);
		
		//creation de la machine a etat
		CStateMachine sm = new CStateMachine() {
		    Paint initColor;
		    Stroke initStroke;
		    
		    public State out = new State() {
		        Transition enterBox = new EnterOnTag(react_click,">> in") {
		            public void action() {
		                initStroke = getClickTag().firstShape().getStroke();
		                getClickTag().firstShape().setStroke(new BasicStroke((float) 5.0));
		            }
		        };
		    };
		    public State clickin = new State() {
		    	Transition click_deds = new PressOnTag(react_click,">> releasein"){
		    		public void action() {
		    			System.out.println("yoyo");
		    			initColor = getShape().getFillPaint();
		                getClickTag().setFillPaint(Color.YELLOW);
		    		}
		    	};
		    };
		    
		    public State releasein = new State() {
		    	Transition relache_deds = new ReleaseOnTag(react_click,">> clickin"){
		    		public void action() {
		    			getClickTag().setFillPaint(initColor);
		    		}
		    	};
		    };
		    
		   
		    public State in = new State() {
		        Transition leaveBox = new LeaveOnTag(react_click,">> out") {
		            public void action() {
		            	getClickTag().firstShape().setStroke(initStroke);
		            }
		        };
		    };
		    
		    
		};
		sm.attachTo(canvas);
		
		JFrame viz = new JFrame() ;
		viz.getContentPane().add(new StateMachineVisualization(sm)) ;
		viz.pack() ;
		viz.setVisible(true) ;
		
	}

}