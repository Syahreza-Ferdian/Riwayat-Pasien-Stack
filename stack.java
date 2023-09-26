import java.util.Stack;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.InputMismatchException;

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
        Scanner input = new Scanner(System.in);

        mainMenuInput : while(true) {
            System.out.printf("\n%s>>> Main Menu <<<%s\n", ANSI_CYAN_COLOR, ANSI_RESET);
            System.out.println("1. Masukkan riwayat pasien\n2. Lihat riwayat pasien\n3. Hapus riwayat pasien\n4. Keluar program");
            System.out.print("Masukkan input: ");
            int menuChoice;
    
            try {
                menuChoice = input.nextInt();
                if(menuChoice <= 0 || menuChoice > 4) throw new InputMismatchException();
                input.nextLine();
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.printf("\n%s%sERROR: Input harus berupa angka 1-4!%s\n", ANSI_RED_BACKGROUND, ANSI_WHITE_BOLD, ANSI_RESET);
                continue;
            }

            switch(menuChoice) {
                case 1 : 
                    if(!data.isEmpty()) {
                        System.out.printf("\n%s%sWARNING: Sudah ada data riwayat pasien yang dimasukkan%s\n", ANSI_RED_BACKGROUND, ANSI_WHITE_BOLD, ANSI_RESET);
                        boolean stopLoop = false;

                        char inputChoice = ' ';
                        while(!stopLoop) {
                            System.out.print("Apakah Anda ingin meng-override data riwayat pasien sebelumnya? (Y/N): ");
                            
                            try {
                                inputChoice = input.nextLine().toLowerCase().charAt(0);
                                if(!(inputChoice == 'y' || inputChoice == 'n')) throw new InputMismatchException();
                                stopLoop = true;
                            } catch (InputMismatchException e) {
                                System.out.printf("\n%s%sERROR: Input hanya bisa berupa karakter Y atau N!%s\n", ANSI_RED_BACKGROUND, ANSI_WHITE_BOLD, ANSI_RESET);
                            }
                        }

                        switch(inputChoice) {
                            case 'y':
                                System.out.printf("\n%s%sUSERINFO: Riwayat pasien telah berhasil dihapus. Sekarang Anda dapat memasukkan riwayat baru%s\n", ANSI_GREEN_BACKGROUND, ANSI_WHITE_BOLD, ANSI_RESET);
                                data.removeAllElements();
                                undoRedoHistory.removeAllElements();
                                break;
                            case 'n': break; //do nothing
                        }
                    }
                        
                    inputText: while(true) {
                        System.out.printf("\n%sMasukkan teks riwayat pasien%s\n", ANSI_BLUE_COLOR, ANSI_RESET);
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
                    try {
                        if(data.isEmpty()) throw new EmptyStackException();
                        System.out.printf("\nRiwayat pasien tersebut adalah:\n%s\n", data);
                    } catch(EmptyStackException e) {
                        System.out.printf("\n%s%sERROR: Belum ada riwayat pasien yang dimasukkan!%s\n", ANSI_RED_BACKGROUND, ANSI_WHITE_BOLD, ANSI_RESET);
                    }
                    break;

                case 3 : 
                    char confirm;

                    try {
                        if(data.isEmpty()) throw new EmptyStackException();
                        System.out.print("Apakah Anda yakin ingin menghapus riwayat pasien? (Y/N): ");
                        confirm = input.nextLine().toLowerCase().charAt(0);
                        if(!(confirm == 'y' || confirm == 'n')) throw new InputMismatchException();

                    } catch(InputMismatchException e) {
                        System.out.printf("\n%s%sERROR: Input harus berupa karakter Y atau N!%s\n", ANSI_RED_BACKGROUND, ANSI_WHITE_BOLD, ANSI_RESET);
                        continue;
                        
                    } catch(EmptyStackException e) {
                        System.out.printf("\n%s%sERROR: Belum ada riwayat pasien yang dimasukkan!%s\n", ANSI_RED_BACKGROUND, ANSI_WHITE_BOLD, ANSI_RESET);
                        continue;
                    }
                    
                    switch(confirm) {
                        case 'y' : 
                            data.removeAllElements();
                            undoRedoHistory.removeAllElements();
                            System.out.printf("\n%s%sUSERINFO: Data pasien tersebut telah berhasil dihapus!%s\n", ANSI_GREEN_BACKGROUND, ANSI_WHITE_BOLD, ANSI_RESET);
                            break;
                        case 'n' :
                            continue mainMenuInput;
                    }
                    break;

                case 4 : 
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
Initial design flow
MAIN MENU: 
1. Masukkan riwayat pasien
    'output text goes like this'
        Masukkan kalimat (input z untuk undo, y untuk redo, s untuk save).

2. Lihat riwayat pasien
    - tampilin pesan error apabila belum ada riwayat pasien yg dimasukin

3. Hapus riwayat pasien
    - tampilin pesan apabila belum ada riwayat pasien yg dimasukin
    - tampil konfirmasi beneran hapus apa engga
        - beneran hapus -> data.removeAllElements, undoRedoHistory.removeAllElements
        - tidak -> balik ke main menu

4. Keluar program


Input 1 behaviour
1. Masukkan riwayat pasien
    - apabila sudah ada ada, tampilkan konfirmasi ingin di override atau tidak.
       - override : data.removeAllElements, undoRedoHistory.removeAllElements --> data yg lama dihapus, tar masukin baru lagi
       - Tidak : langsung masukin selanjutnya --> data yg lama masih ada, jadi kalau masukin baru tar automatis ketambah di elemen berikutnya
 */