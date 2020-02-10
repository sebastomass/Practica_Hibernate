package Acces_a_dades_Navegador;

import org.hibernate.Session;

import java.util.List;

public class LiteralsHib {
    public LiteralsHib(){

    }

    String getLanguageById(int id){
        String language = "es";
        Session _session = HibernateUtil.get_session();
        List<idioma> result = (List<idioma>) _session.createQuery("from idioma where idi_cod = '" + language + "'").list();
        int numRes = result.size();
        System.out.println(result.get(0).get_idioma());
        return result.get(0).get_idioma();
    }

    String getMessageByMessageId(String lan, String id){
        Session _session = HibernateUtil.get_session();
        List<literal> result = (List<literal>) _session.createQuery("from literal where idi_cod=" + "'" + lan + "' and lit_clau=" + "'" + id + "';");
        return result.get(0).get_text();
    }

    void clearLogs(){
        Session _session = HibernateUtil.get_session();
        _session.createNativeQuery("truncate table logs").executeUpdate();
    }

    void addLanguage(String language){
        idioma auxIdi = new idioma();
        auxIdi.setIdioma(language);
        Session sessionHib = HibernateUtil.getSessionFactory().openSession();
        sessionHib.beginTransaction();
        sessionHib.save(auxIdi);
        sessionHib.getTransaction().commit();
        sessionHib.close();
    }
}
