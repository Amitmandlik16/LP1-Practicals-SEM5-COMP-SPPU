
import java.util.*;

class MacroProcessor {
    ArrayList<ArrayList<String>> MNT;  // Macro Name Table: [name, mdtIndex, args]
    ArrayList<ArrayList<String>> MDT;  // Macro Definition Table
    ArrayList<ArrayList<String>> ALA;  // Actual Argument Arrays
    ArrayList<String> output;         // Expanded code

    public MacroProcessor() {
        MNT = new ArrayList<>();
        MDT = new ArrayList<>();
        ALA = new ArrayList<>();
        output = new ArrayList<>();
    }

    // Pass 1: Process macro definitions
    public void pass1(String[][] code) {
        boolean isMacro = false;
        int mdtIndex = 0;

        for(int i = 0; i < code.length; i++) {
            if(code[i][0].equals("MACRO")) {
                isMacro = true;
                // Get macro prototype
                String[] prototype = code[i+1];
                
                // Add to MNT
                ArrayList<String> mntEntry = new ArrayList<>();
                mntEntry.add(prototype[0]); // macro name
                mntEntry.add(String.valueOf(mdtIndex)); // MDT index
                mntEntry.add(String.valueOf(prototype.length - 1)); // number of args
                MNT.add(mntEntry);

                // Add prototype to MDT
                ArrayList<String> mdtEntry = new ArrayList<>();
                for(String token : prototype) {
                    mdtEntry.add(token);
                }
                MDT.add(mdtEntry);
                mdtIndex++;
                i++; // Skip prototype line
                continue;
            }

            if(code[i][0].equals("MEND")) {
                isMacro = false;
                ArrayList<String> mdtEntry = new ArrayList<>();
                mdtEntry.add("MEND");
                MDT.add(mdtEntry);
                mdtIndex++;
                continue;
            }

            if(isMacro) {
                ArrayList<String> mdtEntry = new ArrayList<>();
                for(String token : code[i]) {
                    mdtEntry.add(token);
                }
                MDT.add(mdtEntry);
                mdtIndex++;
            }
        }
    }

    // Pass 2: Expand macros
    public void pass2(String[][] code) {
        boolean inMacroDef = false;

        for(int i = 0; i < code.length; i++) {
            if(code[i][0].equals("MACRO")) {
                inMacroDef = true;
                continue;
            }
            if(code[i][0].equals("MEND")) {
                inMacroDef = false;
                continue;
            }
            if(inMacroDef) continue;

            // Check if this line is a macro call
            boolean isMacroCall = false;
            for(ArrayList<String> mntEntry : MNT) {
                if(code[i][0].equals(mntEntry.get(0))) {
                    isMacroCall = true;
                    
                    // Store actual arguments
                    ArrayList<String> actualArgs = new ArrayList<>();
                    for(int j = 1; j < code[i].length; j++) {
                        if(!code[i][j].isEmpty()) {
                            actualArgs.add(code[i][j]);
                        }
                    }
                    ALA.add(actualArgs);

                    // Expand macro
                    expandMacro(Integer.parseInt(mntEntry.get(1)), actualArgs);
                    break;
                }
            }

            if(!isMacroCall && !code[i][0].isEmpty()) {
                StringBuilder line = new StringBuilder();
                for(String token : code[i]) {
                    if(!token.isEmpty()) {
                        line.append(token).append(" ");
                    }
                }
                output.add(line.toString().trim());
            }
        }
    }

    private void expandMacro(int mdtIndex, ArrayList<String> actualArgs) {
        int i = mdtIndex + 1; // Skip prototype
        ArrayList<String> prototype = MDT.get(mdtIndex);
        
        while(!MDT.get(i).get(0).equals("MEND")) {
            ArrayList<String> instruction = MDT.get(i);
            StringBuilder expandedLine = new StringBuilder();
            
            for(String token : instruction) {
                boolean replaced = false;
                // Check if token matches any formal parameter
                for(int j = 1; j < prototype.size(); j++) {
                    if(token.equals(prototype.get(j))) {
                        expandedLine.append(actualArgs.get(j-1)).append(" ");
                        replaced = true;
                        break;
                    }
                }
                if(!replaced) {
                    expandedLine.append(token).append(" ");
                }
            }
            output.add(expandedLine.toString().trim());
            i++;
        }
    }

    public void printTables() {
        System.out.println("Macro Name Table (MNT):");
        System.out.println("Name\tMDT Index\tArgs Count");
        for(ArrayList<String> entry : MNT) {
            System.out.println(String.join("\t", entry));
        }

        System.out.println("\nMacro Definition Table (MDT):");
        for(int i = 0; i < MDT.size(); i++) {
            System.out.print(i + ": ");
            System.out.println(String.join(" ", MDT.get(i)));
        }

        System.out.println("\nArgument List Array (ALA):");
        for(int i = 0; i < ALA.size(); i++) {
            System.out.println("Macro call " + (i+1) + ": " + String.join(", ", ALA.get(i)));
        }

        System.out.println("\nExpanded Code:");
        for(String line : output) {
            System.out.println(line);
        }
    }
}

public class Macro{
    public static void main(String[] args) {
        String[][] code = {
            {"MACRO"},
            {"INCR", "X","Y","REG"},
            {"MOVER", "REG", "X"},
            {"ADD", "REG", "Y"},
            {"MOVEM", "REG", "X"},
            {"MEND"},
            {"MACRO"},
            {"DECR", "A","B","REG"},
            {"MOVER", "REG", "A"},
            {"SUB", "REG", "B"},
            {"MOVEM", "REG", "A"},
            {"MEND"},
            {"START","100"},
            {"READ", "N1"},
            {"READ", "N2"},
            {"INCR", "N1", "N2","CREG"},
            {"DECR", "N1", "N2","BREG"},
            {"STOP"},
            {"N1", "DS", "1"},
            {"N2", "DS", "2"},
            {"END"}
        };

        MacroProcessor mp = new MacroProcessor();
        mp.pass1(code);
        mp.pass2(code);
        mp.printTables();
    }
}