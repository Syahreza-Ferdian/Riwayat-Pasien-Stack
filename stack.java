import java.util.*;

public class stack {
    static final String ANSI_BLUE_COLOR = "\033[0;34m";
    static final String ANSI_CYAN_COLOR = "\033[0;36m";
    static final String ANSI_RESET = "\033[0m";
    static final String ANSI_RED_BACKGROUND = "\033[0;101m";
    static final String ANSI_GREEN_BACKGROUND = "\033[42m";
    static final String ANSI_WHITE_BOLD = "\033[1;37m";
    static final String ANSI_CYAN_BOLD = "\033[1;36m";
    static final String ANSI_YELLOW_BACKGROUND = "\033[43m";

    static Stack<String> data = new Stack<>() {
        @Override
        public String toString() {
            if(data.size() == 0) return "[ ]";
            return super.toString().replaceAll("([\\[\\],])", "");
        }
    };

    static Stack<String> undoRedoHistory = new Stack<>();

    public static void main(String[] args) {
        //TODO : BUAT MENU UNTUK MENGHAPUS TEKS RIWAYAT YANG DIMASUKKAN (CLEAR)

        Scanner input = new Scanner(System.in);

        mainMenuInput : while(true) {
            System.out.printf("\n%s>>> Main Menu <<<%s\n", ANSI_CYAN_COLOR, ANSI_RESET);
            System.out.println("1. Masukkan riwayat pasien\n2. Lihat riwayat pasien\n3. Keluar program");
            System.out.print("Masukkan input: ");
            int menuChoice;
    
            try {
                menuChoice = input.nextInt();
                if(menuChoice <= 0 || menuChoice > 3) throw new InputMismatchException();
                input.nextLine();
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.printf("\n%s%sERROR: Input harus berupa angka 1-3!%s\n", ANSI_RED_BACKGROUND, ANSI_WHITE_BOLD, ANSI_RESET);
                continue;
            }

            switch(menuChoice) {
                case 1 : 
                    inputText: while(true) {
                        System.out.printf("\n\n%sMasukkan teks riwayat pasien%s\n", ANSI_BLUE_COLOR, ANSI_RESET);
                        System.out.println("HINT: Input 'z' untuk undo, 'y' untuk redo, 's' untuk save.");
                        System.out.print("Input something: ");
                        String inputan[] = input.nextLine().split("\s");

                        switch(inputan[0].toLowerCase()) {
                            case "z" :
                                undo();
                                break;
                            case "y" :
                                redo();
                                break;
                            case "s" :
                                System.out.printf("\n%s%sUSERINFO: Data riwayat pasien telah berhasil disimpan%s\n", ANSI_GREEN_BACKGROUND, ANSI_WHITE_BOLD, ANSI_RESET);
                                break inputText;
                            default :
                                makeStack(inputan, data);
                                System.out.printf("%sTeks preview: %s%s\n", ANSI_CYAN_BOLD, data, ANSI_RESET);
                                undoRedoHistory.removeAllElements();
                        }
                    }
                    break;

                case 2 :
                    System.out.printf("\nRiwayat pasien tersebut adalah:\n%s\n", data);
                    break;
                    
                case 3 : 
                    System.out.printf("\n%s%sUSERINFO: Program dihentikan. Terima kasih :)%s\n", ANSI_YELLOW_BACKGROUND, ANSI_WHITE_BOLD, ANSI_RESET);
                    break mainMenuInput;
            }
        }

        input.close();
    }

    public static void undo() {
        if(data.size() != 0) {
            String dataSingDiUndo = data.pop();
            undoRedoHistory.push(dataSingDiUndo);
        }
        System.out.printf("%sTeks preview: %s%s\n", ANSI_CYAN_BOLD, data, ANSI_RESET);
    }

    public static void redo() {
        if(undoRedoHistory.size() != 0) {
            String dataSingDiRedo = undoRedoHistory.pop();
            data.push(dataSingDiRedo);
        }
        System.out.printf("%sTeks preview: %s%s\n", ANSI_CYAN_BOLD, data, ANSI_RESET);
    }

    public static void makeStack(String[] arr, Stack<String> a) {
        for(String s : arr) {
            a.push(s);
        }
    }
}



/*
Initial design flaw
MAIN MENU: 
1. Masukkan riwayat pasien
    'output text goes like this'
        Masukkan kalimat (input z untuk undo, y untuk redo, s untuk save).

2. Lihat riwayat pasien
3. Keluar program

 */