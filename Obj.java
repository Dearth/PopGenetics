package serialization_test;

import java.io.Serializable;
		
public class Obj implements Serializable{
	public String a = "thing";
	public String b = "other thing";
	public int c = 2;
	public double d = 14.7;
	Obj internal;

//	@Override
//	public String toString() {
//	    String value = "a : " + a + "\nb : " + b + "\nc : " + c
//	            + "\npassKeys : " + d;
//	    return value;
//	}
}
