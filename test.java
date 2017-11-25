package PopGenetics;

public class test {

	public static void main(String args[])
	{
		Fitness f = new Fitness();
		
		String serialized = ObjectUtil.serializeObjectToString(f.pop);
		
		Object returned = ObjectUtil.deserializeObjectFromString(serialized);
		Population b = (Population) returned;
		System.out.println(b.population[1].get_x_coord());
	}
}


