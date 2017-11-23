package serialization_test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class test {

	public static void main(String args[]) throws IOException, ClassNotFoundException
	{
		Obj o = new Obj();
		o.a = "test1";
		o.b = "test2";
		o.c = 4;
		o.d = 7.4;
		o.internal = new Obj();
		
		o.internal.a = "fuck";
		o.internal.b = "this shit";
		o.internal.c = 3;
		o.internal.d = 2.9;
		
		String serialized = ObjectUtil.serializeObjectToString(o);
		
		Object returned = ObjectUtil.deserializeObjectFromString(serialized);
		Obj b = (Obj) returned;
		System.out.println("a: " + b.a);
		System.out.println("b: " + b.b);
		System.out.println("c: " + b.c);
		System.out.println("d: " + b.d);
		System.out.println("a: " + b.internal.a);
		System.out.println("b: " + b.internal.b);
		System.out.println("c: " + b.internal.c);
		System.out.println("d: " + b.internal.d);

	}
	
	
	
}


