package Acces_a_dades.Navegador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import static Acces_a_dades.Navegador.Function.*;

class Navigator {
    private File _file;
    private File _logFile;
    private String _commandsFilePath;
    private String _log;
    private String _currentCommand;
    private String _startingPath;
    private String[] _parameters;
    private String _errorMessage;
    private Acces_a_dades.Navegador.Function _function;
    private File _lastDirectory;
    private OrdenationMethod _ordenationMethod;


    private enum OrdenationMethod {
        NAME, DATE
    }

    Navigator(String path) {
        this._startingPath = path;
        this._file = new File(path);
        this._logFile = new File(path, "Log.txt");
        System.out.println("Utilitzi la comanda 'help' per a obtenir la llista de funcions");
    }

    void mainLoop() {
        if (this._file.isDirectory()) System.out.println("~ " + this._file.getAbsolutePath());
        else if (this._file.isFile())
            System.out.println("~ " + this._file.getAbsolutePath() + "\\" + this._file.getName());
        getInput(true);
        execute();
    }

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
        switch (input[0]) {
            case "goto":
                if (input.length == 2) {
                    this._parameters = new String[1];
                    this._parameters[0] = input[1];
                    this._function = Acces_a_dades.Navegador.Function.GOTO;
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "golast":
                if (input.length == 1) {
                    this._function = Acces_a_dades.Navegador.Function.GOLAST;
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "list":
                if (input.length == 1) {
                    this._function = Acces_a_dades.Navegador.Function.LIST;
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "up":
                if (input.length == 1) {
                    this._function = Acces_a_dades.Navegador.Function.UP;
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "infofile":
                if (input.length == 2) {
                    this._parameters = new String[1];
                    this._function = Acces_a_dades.Navegador.Function.INFOFILE;
                    this._parameters[0] = input[1];
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "infodir":
                if (input.length == 2) {
                    this._parameters = new String[1];
                    this._function = Acces_a_dades.Navegador.Function.INFODIR;
                    this._parameters[0] = input[1];
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "help":
                if (input.length == 1) {
                    this._function = Acces_a_dades.Navegador.Function.HELP;
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "createdir":
                if (input.length >= 2) {
                    this._function = Acces_a_dades.Navegador.Function.CREATEDIR;
                    int cont = 0;
                    for (int i = 0; i < input.length; i++) {
                        if (i != 0) {
                            this._parameters[cont] = input[i];
                            cont++;
                        }
                    }
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "createfile":
                if (input.length >= 2) {
                    this._function = Acces_a_dades.Navegador.Function.CREATEFILE;
                    int cont = 0;
                    for (int i = 0; i < input.length; i++) {
                        if (i != 0) {
                            this._parameters[cont] = input[i];
                            cont++;
                        }
                    }
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "sortby":
                if (input.length == 2) {
                    this._function = Acces_a_dades.Navegador.Function.SORTBY;
                    String sortingMethod = input[1].toUpperCase();
                    switch (sortingMethod) {
                        case "NAME":
                            this._ordenationMethod = OrdenationMethod.NAME;
                            break;
                        case "DATE":
                            this._ordenationMethod = OrdenationMethod.DATE;
                        default:
                            this._function = ERROR;
                            this._errorMessage = "Error. Method '" + sortingMethod + "' does not exist.";
                    }
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "deletedir":
                if (input.length >= 2) {
                    this._function = Acces_a_dades.Navegador.Function.DELETEDIR;
                    int index = 0;
                    for (int x = 1; x < input.length; x++) {
                        this._parameters[index] = input[x];
                        index++;
                    }
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;
            case "deletefile":
                if (input.length >= 2) {
                    this._function = Acces_a_dades.Navegador.Function.DELETEFILE;
                    int index = 0;
                    for (int x = 1; x < input.length; x++) {
                        this._parameters[index] = input[x];
                        index++;
                    }
                } else {
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "log":
                if(input.length == 2){
                    this._function = LOG;
                    this._parameters[0] = input[1];
                }else{
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;

            case "clearlog":
                if(input.length == 1){
                    this._function = CLEARLOG;
                }else{
                    this._function = ERROR;
                    this._errorMessage = "Nombre de paràmetres incorrecte.";
                }
                break;
            case "load":
                if(input.length == 2){
                    this._function =  LOAD;
                    _parameters[0] = input[1];
                }else{
                    _function = ERROR;
                    _errorMessage = "Nombre de paràmetres incorrecte.";
                }
            case "error":
                this._function = ERROR;
                this._errorMessage = "Ha hagut un error";
                break;
            default:
                this._function = ERROR;
                this._errorMessage = "Sintaxi incorrecta";
        }
    }

    private void execute() {
        switch (this._function) {
            case GOTO:
                File fileGoto = new File(_file.getAbsolutePath() + "\\" + this._parameters[0]);
                if (fileGoto.isDirectory()) {
                    this._lastDirectory = this._file;
                    this._file = fileGoto;
                } else {
                    System.out.println(this._parameters[0] + " no és un directori.");
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
                            System.out.println("- " + file.getName() + "--- Directori");
                        } else {
                            System.out.println("- " + file.getName() + " --- Arxiu");
                        }
                    }
                }
                break;

            case UP:
                File parentFile = this._file.getParentFile();
                if (parentFile != null) {
                    this._file = parentFile;
                } else {
                    System.out.println("Error. El fitxer pare no és un directori.");
                }
                break;

            case INFOFILE:
                File fileToGetInfo = new File(this._file.getAbsolutePath() + "\\" + this._parameters[0]);
                if (fileToGetInfo.isFile()) {
                    String infoMessage = "Informació de: " + this._parameters[0] + "\n";
                    if (fileToGetInfo.canRead()) {
                        infoMessage += "El fitxer té permisos de lectura.";
                    } else {
                        infoMessage += "El fitxer no té permisos de lectura.";
                    }
                    infoMessage += "\n";
                    if (fileToGetInfo.canWrite()) {
                        infoMessage += "El fitxer té permisos d'escriptura.";
                    } else {
                        infoMessage += "El fitxer no té permisos d'escriptura.";
                    }
                    infoMessage += "\n";
                    if (fileToGetInfo.canExecute()) {
                        infoMessage += "El fitxer té permisos d'execució.";
                    } else {
                        infoMessage += "El fitxer no té permisos d'execució.";
                    }
                    System.out.println(infoMessage);
                } else {
                    System.out.println("El fitxer no és un arxiu.");
                }
                break;

            case INFODIR:
                File directoryToGetInfo = new File(this._file.getAbsolutePath() + "\\" + this._parameters[0]);
                if (directoryToGetInfo.isDirectory()) {
                    String infoMessage = "Informació de: " + this._parameters[0] + "\n";
                    if (directoryToGetInfo.canRead()) {
                        infoMessage += "El directori té permisos de lectura.";
                    } else {
                        infoMessage += "El directori no té permisos de lectura.";
                    }
                    infoMessage += "\n";
                    if (directoryToGetInfo.canWrite()) {
                        infoMessage += "El directori té permisos d'escriptura.";
                    } else {
                        infoMessage += "El directori no té permisos d'escriptura.";
                    }
                    infoMessage += "\n";
                    if (directoryToGetInfo.canExecute()) {
                        infoMessage += "El directori té permisos d'execució.";
                    } else {
                        infoMessage += "El directori no té permisos d'execució.";
                    }
                    System.out.println(infoMessage);
                } else {
                    System.out.println("El fitxer no és un directori");
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
                            System.out.println("Directori " + directory + " creat amb èxit.");
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
                                System.out.println("Arxiu " + filename + "creat amb èxit.");
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
                        System.out.println("Directori " + directory + " esborrat amb èxit.");
                    } else {
                        System.out.println("Error al esborrar el directori " + directory);
                    }
                }
                break;
            case DELETEFILE:
                for (String file : this._parameters) {
                    File currentFile = new File(this._file.getAbsolutePath() + "\\" + file);
                    if (currentFile.delete()) {
                        System.out.println("Arxiu " + file + " esborrat amb èxit.");
                    } else {
                        System.out.println("Error al esborrar l'arxiu " + file);
                    }
                }
                break;
            case LOG:
                switch (this._parameters[0]){
                    case "0":
                        this._logFile = null;
                        break;
                    case "1":
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
}
