import java.util.*;

public class stack {
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

        inputText: while(true) {
            String inputan[] = input.nextLine().split("\s");

            switch(inputan[0].toLowerCase()) {
                case "z" :
                    undo();
                    break;
                case "y" :
                    redo();
                    break;
                case "n" :
                    break inputText;
                default :
                    makeStack(inputan, data);
                    System.out.println(data);
                    undoRedoHistory.removeAllElements();
            }
        }

        input.close();
    }

    public static void undo() {
        if(data.size() != 0) {
            String dataSingDiUndo = data.pop();
            undoRedoHistory.push(dataSingDiUndo);
        }
        System.out.println(data);
    }

    public static void redo() {
        if(undoRedoHistory.size() != 0) {
            String dataSingDiRedo = undoRedoHistory.pop();
            data.push(dataSingDiRedo);
        }
        System.out.println(data);
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