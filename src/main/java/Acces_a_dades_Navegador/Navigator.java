package Acces_a_dades_Navegador;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

// Classe principal que fa totes les coses del navegador
class Navigator {
    private File _file;
    private File _logFile;
    private Document _xmlDocument;
    private String _commandsFilePath;
    // Available languages: es, cat, en.
    private String _language = "es";
    private String _log;
    private String _currentCommand;
    private String _startingPath;
    private String[] _parameters;
    private String _errorMessage;
    private Function _function;
    private File _lastDirectory;
    private OrdenationMethod _ordenationMethod;
    // Aquesta classe conté les funcions d'accés a la base de dades
    private LiteralsHib lits;


    private enum OrdenationMethod {
        NAME, DATE
    }
    Navigator(String path) {
        //Es crea una nova Literals BD
//        this.literalsBD = new LiteralsBD();
        this.lits = new LiteralsHib();
        this._language = lits.getLanguageById(1);
        //El llenguatge es pot definir gràcies a la base de dades
//        this._language = literalsBD.getLanguageById(1);
        this._startingPath = path;
        this._file = new File(path);
        this._logFile = new File(path, "Log.txt");
//        try {
//            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/main/java/Acces_a_dades/Navegador/literals.xml"));
//            document.getDocumentElement().normalize();
//            this._xmlDocument = document;
//        } catch (SAXException | IOException | ParserConfigurationException e) {
//            e.printStackTrace();
//        }

        System.out.println("Utilitzi la comanda 'help' per a obtenir la llista de funcions");
    }

    void mainLoop() {
        if (this._file.isDirectory()) System.out.println("~ " + this._file.getAbsolutePath());
        else if (this._file.isFile())
            System.out.println("~ " + this._file.getAbsolutePath() + "\\" + this._file.getName());
        getInput(true);
        execute();
    }
    //S'gafa l'input per teclat
    private void getInput(boolean readKeyboad) {
        System.out.print("> ");
        if(readKeyboad){
            Scanner keyboard = new Scanner(System.in);
            this._currentCommand = keyboard.nextLine();
        }
        String[] input = this._currentCommand.split(" ");
        input[0] = input[0].toLowerCase();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this._logFile));
            this._log += this._currentCommand + "\n";
            writer.write(this._log);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Control de tots els possibles inputs que estan declarats a la terminal
        switch (input[0]) {
            case "goto":
                if (input.length == 2) {
                    this._parameters = new String[1];
                    this._parameters[0] = input[1];
                    this._function = Function.GOTO;
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "golast":
                if (input.length == 1) {
                    this._function = Function.GOLAST;
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "list":
                if (input.length == 1) {
                    this._function = Function.LIST;
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "up":
                if (input.length == 1) {
                    this._function = Function.UP;
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "infofile":
                if (input.length == 2) {
                    this._parameters = new String[1];
                    this._function = Function.INFOFILE;
                    this._parameters[0] = input[1];
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "infodir":
                if (input.length == 2) {
                    this._parameters = new String[1];
                    this._function = Function.INFODIR;
                    this._parameters[0] = input[1];
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "help":
                if (input.length == 1) {
                    this._function = Function.HELP;
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "createdir":
                if (input.length >= 2) {
                    this._function = Function.CREATEDIR;
                    int cont = 0;
                    for (int i = 0; i < input.length; i++) {
                        if (i != 0) {
                            this._parameters[cont] = input[i];
                            cont++;
                        }
                    }
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "createfile":
                if (input.length >= 2) {
                    this._function = Function.CREATEFILE;
                    int cont = 0;
                    for (int i = 0; i < input.length; i++) {
                        if (i != 0) {
                            this._parameters[cont] = input[i];
                            cont++;
                        }
                    }
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "sortby":
                if (input.length == 2) {
                    this._function = Function.SORTBY;
                    String sortingMethod = input[1].toUpperCase();
                    switch (sortingMethod) {
                        case "NAME":
                            this._ordenationMethod = OrdenationMethod.NAME;
                            break;
                        case "DATE":
                            this._ordenationMethod = OrdenationMethod.DATE;
                        default:
                            this._function = Function.ERROR;
                            this._errorMessage = "Error. Method '" + sortingMethod + "' does not exist.";
                    }
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "deletedir":
                if (input.length >= 2) {
                    this._function = Function.DELETEDIR;
                    int index = 0;
                    for (int x = 1; x < input.length; x++) {
                        this._parameters[index] = input[x];
                        index++;
                    }
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;
            case "deletefile":
                if (input.length >= 2) {
                    this._function = Function.DELETEFILE;
                    int index = 0;
                    for (int x = 1; x < input.length; x++) {
                        this._parameters[index] = input[x];
                        index++;
                    }
                } else {
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "log":
                if(input.length == 2){
                    this._function = Function.LOG;
                    this._parameters[0] = input[1];
                }else{
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "clearlog":
                if(input.length == 1){
                    this._function = Function.CLEARLOG;
                }else{
                    this._function = Function.ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;
            case "load":
                if(input.length == 2){
                    this._function =  Function.LOAD;
                    _parameters[0] = input[1];
                }else{
                    _function = Function.ERROR;
                    _errorMessage = "Nombre de paràmetres incorrecte.";
                }
            case "error":
                this._function = Function.ERROR;
                this._errorMessage = "Ha hagut un error";
                break;
            default:
                this._function = Function.ERROR;
                this._errorMessage = "Sintaxi incorrecta";
        }
    }
    //A aquest metode es realitza el funcionament de cada una de les comandes, amb un switch per al tractament de totes les funcions.
    private void execute() {
        switch (this._function) {
            case GOTO:
                File fileGoto = new File(_file.getAbsolutePath() + "\\" + this._parameters[0]);
                if (fileGoto.isDirectory()) {
                    this._lastDirectory = this._file;
                    this._file = fileGoto;
                } else {
                    System.out.println(this.lits.getMessageByMessageId(this._language, "notrobatfitxercarregar"));
                }
                break;
            case GOLAST:
                this._file = new File(_lastDirectory.getAbsolutePath());
                break;

            case LIST:
                File[] list = this._file.listFiles();
                if (list != null) {
                    for (File file : list) {
                        if (file.isDirectory()) {
                            System.out.println("- " + file.getName() + "--- "+ this.lits.getMessageByMessageId(this._language, "Directori"));
                        } else {
                            System.out.println("- " + file.getName() + " --- " + this.lits.getMessageByMessageId(this._language, "Fitxer"));
                        }
                    }
                }
                break;

            case UP:
                File parentFile = this._file.getParentFile();
                if (parentFile != null) {
                    this._file = parentFile;
                } else {
                    System.out.println(this.lits.getMessageByMessageId(this._language, "directoriparenotrobat"));
                }
                break;

            case INFOFILE:
                File fileToGetInfo = new File(this._file.getAbsolutePath() + "\\" + this._parameters[0]);
                if (fileToGetInfo.isFile()) {
                    String infoMessage = "Informació de: " + this._parameters[0] + "\n";
                    if (fileToGetInfo.canRead()) {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    } else {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    }
                    infoMessage += "\n";
                    if (fileToGetInfo.canWrite()) {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    } else {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    }
                    infoMessage += "\n";
                    if (fileToGetInfo.canExecute()) {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    } else {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    }
                    System.out.println(infoMessage);
                } else {
                    System.out.println(this.lits.getMessageByMessageId(this._language, "noexisteix"));
                }
                break;

            case INFODIR:
                File directoryToGetInfo = new File(this._file.getAbsolutePath() + "\\" + this._parameters[0]);
                if (directoryToGetInfo.isDirectory()) {
                    String infoMessage = "Informació de: " + this._parameters[0] + "\n";
                    if (directoryToGetInfo.canRead()) {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    } else {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    }
                    infoMessage += "\n";
                    if (directoryToGetInfo.canWrite()) {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    } else {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    }
                    infoMessage += "\n";
                    if (directoryToGetInfo.canExecute()) {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    } else {
                        infoMessage += this.lits.getMessageByMessageId(this._language, "nopermisos");
                    }
                    System.out.println(infoMessage);
                } else {
                    System.out.println(this.lits.getMessageByMessageId(this._language, "noexisteix"));
                }
                break;
            case HELP:
                System.out.println("Llista de funcions:\n" +
                        "- goto $ruta (acepta 1 paràmetre): anar a una ruta.\n" +
                        "- golast (sense paràmetres): anar al darrer directori visitat.\n" +
                        "- list (sense paràmetres): llistar elements del directori.\n" +
                        "- up (sense paràmetres): anar al directori pare.\n" +
                        "- infofile $filename (acepta 1 paràmetre: informació del fitxer indicat.\n" +
                        "- infodir $dirname (acepta 1 parametre): informació del directori indicat.\n" +
                        "- help (sense paràmetres): pintar per pantalla tots els comandaments.\n" +
                        "- createdir $dirname...  (acepta 1 o més paràmetres): crear un directori.\n" +
                        "- createfile $filename... (acepta 1 o més paràmetres): eliminar el o els fitxers indicats.\n" +
                        "- sortby $criteri (acepta 1 paràmetre): listar el o els fitxers ordenats per algún criteri.\n" +
                        "- deletedir (acepta 1 o més paràmetres): eliminar el o els directoris indicats.\n" +
                        "- deletefile (acepta 1 o més paràmetres): eliminar el o els fitxers indicats.\n");
                break;
            case CREATEDIR:
                for (String directory : this._parameters) {
                    File file = new File(this._file.getAbsolutePath() + "\\" + directory);
                    if (!file.exists()) {
                        if (file.mkdir()) {
                            System.out.println("Directori " + directory + " " +this.lits.getMessageByMessageId(this._language, "creatcorrectament"));
                        } else {
                            System.out.println("Error al crear el directori " + directory + ".");
                        }
                    }
                }
                break;
            case CREATEFILE:
                for (String filename : this._parameters) {
                    File file = new File(this._file.getAbsolutePath() + "\\" + filename);
                    if (!file.exists()) {
                        try {
                            if (file.createNewFile()) {
                                System.out.println("Arxiu " + filename + " " + this.lits.getMessageByMessageId(this._language, "creatcorrectament"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Error al crear l'arxiu " + filename + ".");
                    }
                }
                break;

            case SORTBY:
                switch (this._ordenationMethod) {
                    case NAME:
                        File[] fileListByName = this._file.listFiles();
                        if (fileListByName != null) {
                            Arrays.sort(fileListByName, new Comparator<File>() {
                                @Override
                                public int compare(File f1, File f2) {
                                    return ((File) f1).getName().compareTo(((File) f2).getName());
                                }
                            });
                            for (File file : fileListByName) {
                                System.out.println(file.getName());
                            }
                        }
                        break;
                    case DATE:
                        File[] fileListByDate = this._file.listFiles();
                        if (fileListByDate != null) {
                            Arrays.sort(fileListByDate, new Comparator<File>() {
                                @Override
                                public int compare(File f1, File f2) {
                                    return Math.round(f1.lastModified() - f2.lastModified());
                                }
                            });
                            for (File file : fileListByDate) {
                                System.out.println(file.getName());
                            }
                        }
                        break;
                }
                break;
            case DELETEDIR:
                for (String directory : this._parameters) {
                    File currentFile = new File(this._file.getAbsolutePath() + "\\" + directory);
                    if (currentFile.delete()) {
                        System.out.println("Directori " + directory + " " + this.lits.getMessageByMessageId(this._language, "eliminatcorrectament"));
                    } else {
                        System.out.println("Error al esborrar el directori " + directory);
                    }
                }
                break;
            case DELETEFILE:
                for (String file : this._parameters) {
                    File currentFile = new File(this._file.getAbsolutePath() + "\\" + file);
                    if (currentFile.delete()) {
                        System.out.println("Arxiu " + file + " " + this.lits.getMessageByMessageId(this._language, "eliminatcorrectament"));
                    } else {
                        System.out.println("Error al esborrar l'arxiu " + file);
                    }
                }
                break;
            case LOG:
                switch (this._parameters[0]){
                    case "0":
                        System.out.println(getElementFromNodeTree("logdesactivat"));
                        this._logFile = null;
                        break;
                    case "1":
                        System.out.println(getElementFromNodeTree("logactivat"));
                        this._logFile = new File(this._startingPath, "Log.txt");
                        break;
                }
                break;
            case CLEARLOG:
                this._logFile = new File(this._startingPath, "Log.txt");
                break;
            case LOAD:
                try {
                    _commandsFilePath = new String(Files.readAllBytes(Paths.get(_parameters[0])));
                    String[] commands = _commandsFilePath.split("\n");
                    for(String command: commands){
                        this._currentCommand = command;
                        getInput(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case ERROR:
                System.out.println(this._errorMessage);
                break;
        }
        this._errorMessage = "";
        mainLoop();
    }

    //Funcio auxiliar per a poder agafar els Strings a dins un XML
    private String getElementFromNodeTree(String element){
        String output = "";
        Node n;
        NodeList nodeList = _xmlDocument.getElementsByTagName("literal");
        for(int i = 0; i < nodeList.getLength(); i++){
            n = nodeList.item(i);
            if(n.getAttributes().getNamedItem("id").getTextContent().equals(element) && n.getNodeType() == Node.ELEMENT_NODE){
                Element el = (Element) n;
                output = el.getElementsByTagName(this._language).item(0).getTextContent();
            }
        }
        return output;
    }
}
