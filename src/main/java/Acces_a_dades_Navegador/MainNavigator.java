package Acces_a_dades_Navegador;


public class MainNavigator {
    public static void main(String[] args) {
        Navigator nav = new Navigator(System.getProperty("user.home"));
        nav.mainLoop();
    }
}






