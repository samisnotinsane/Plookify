package account;

/**
 * Created by tahnik on 15/02/2016
 */
public class RegistrarTest {
    public static void registrarDatabaseTest(){
        String email;
        for(int i = 0 ; i < 100 ; i++) {
            Registrar registrar = new Registrar();
            email = "tahnik" + i + "@live.co.uk";
            registrar.setEmail(email);
            registrar.setPassword("12");
            registrar.setFirstName("Tahnik");
            registrar.setLastName("Mustasin");
            registrar.setAddressLine("12 Francis Avenue");
            registrar.setCity("Ilford");
            registrar.setPostCode("IG1 1TS");
            System.out.println(registrar.registrate());
        }
    }
}
