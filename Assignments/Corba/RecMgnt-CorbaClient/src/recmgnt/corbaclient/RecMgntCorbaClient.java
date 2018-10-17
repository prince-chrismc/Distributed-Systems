/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recmgnt.corbaclient;

import Interface.AdditionApp.Addition;
import Interface.AdditionApp.AdditionHelper;
import java.util.Scanner;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

/**
 *
 * @author cmcarthur
 */
public class RecMgntCorbaClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
              try {
	    ORB orb = ORB.init(args, null);
	    org.omg.CORBA.Object objRef =   orb.resolve_initial_references("NameService");
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	    Addition addobj = (Addition) AdditionHelper.narrow(ncRef.resolve_str("ABC"));
 
            Scanner c=new Scanner(System.in);
            System.out.println("Welcome to the addition system:");          		    
		    for(;;){
		      System.out.println("Enter a:");
		      String aa = c.nextLine();
		      System.out.println("Enter b:");
		      String bb = c.nextLine();
		      int a=Integer.parseInt(aa);
		      int b=Integer.parseInt(bb);
		      int r=addobj.add(a,b);
		      System.out.println("The result for addition is : "+r);
		      System.out.println("-----------------------------------");
            }
       }
       catch (Exception e) {
          System.out.println("Hello Client exception: " + e);
	  e.printStackTrace();
       }
              
              
              
    }
    
}
