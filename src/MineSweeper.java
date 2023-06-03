import java.util.Scanner;

public class MineSweeper {
    private int rowSize;
    private int colSize;
    private char[][] board;
    private boolean[][] visited;
    private int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    public void startGame() {
        initializeBoard();
        placeMines();
        play();
    }

    private void initializeBoard() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Satır sayısını girin: ");
        rowSize = scanner.nextInt();
        System.out.print("Sütun sayısını girin: ");
        colSize = scanner.nextInt();
        board = new char[rowSize][colSize];
        visited = new boolean[rowSize][colSize];

        // Board'u boşluklarla doldur
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                board[i][j] = '-';
                visited[i][j] = false;
            }
        }
    }

    private void placeMines() {
        double mineProbability = 0.25; // Mayın oranını belirleyin (örneğin %25)
        int totalCells = rowSize * colSize;
        int mineCount = (int) (totalCells * mineProbability);

        // Belirlenen mayın sayısı kadar rastgele konumlara mayın yerleştir
        int minesPlaced = 0;
        while (minesPlaced < mineCount) {
            int row = (int) (Math.random() * rowSize);
            int col = (int) (Math.random() * colSize);
            if (board[row][col] != '*') {
                board[row][col] = '*';
                minesPlaced++;
            }
        }
    }



    private void play() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Tablo: ");
            printBoard();

            System.out.print("Satırı seçin: ");
            int selectedRow = scanner.nextInt();
            System.out.print("Sütunu seçin: ");
            int selectedCol = scanner.nextInt();

            if (selectedRow < 0 || selectedRow >= rowSize || selectedCol < 0 || selectedCol >= colSize) {
                System.out.println("Geçersiz bir konum seçtiniz. Tekrar deneyin.");
                continue;
            }

            if (visited[selectedRow][selectedCol]) {
                System.out.println("Bu konumu zaten seçtiniz. Tekrar deneyin.");
                continue;
            }

            visited[selectedRow][selectedCol] = true;

            if (board[selectedRow][selectedCol] == '*') {
                System.out.println("Mayına bastınız! Oyun bitti.");
                board[selectedRow][selectedCol] = 'X';
                printBoard();
                break;
            } else {
                int adjacentMines = countAdjacentMines(selectedRow, selectedCol);
                board[selectedRow][selectedCol] = (char) (adjacentMines + '0');
                if (adjacentMines == 0) {
                    exploreAdjacentCells(selectedRow, selectedCol);
                }
                boolean win = checkWin();
                if (win) {
                    System.out.println("Tebrikler! Tüm boşlukları buldunuz. Oyun bitti.");
                    printBoard();
                    break;
                }
            }
        }
    }

    private void printBoard() {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            if (newRow >= 0 && newRow < rowSize && newCol >= 0 && newCol < colSize && board[newRow][newCol] == '*') {
                count++;
            }
        }
        return count;
    }

    private void exploreAdjacentCells(int row, int col) {
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            if (newRow >= 0 && newRow < rowSize && newCol >= 0 && newCol < colSize && !visited[newRow][newCol]) {
                visited[newRow][newCol] = true;
                int adjacentMines = countAdjacentMines(newRow, newCol);
                board[newRow][newCol] = (char) (adjacentMines + '0');
                if (adjacentMines == 0) {
                    exploreAdjacentCells(newRow, newCol);
                }
            }
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (board[i][j] == '-' && !visited[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}