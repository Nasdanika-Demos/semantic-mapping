import org.nasdanika.models.nature.Color

System.out.println("---");
System.out.println("Source: " + args);
System.out.println("Target: " + target);
System.out.println("Pass: " + pass);

target.setColor(Color.BROWN);

return pass > 2; // Just to test multiple invocations