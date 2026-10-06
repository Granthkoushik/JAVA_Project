import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();

        try {
            Database.setup();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            System.out.println(e.getMessage());
            return;
        }

        while (true) {
            showMenu();
            int choice = readInt(sc, "Enter choice: ");

            try {
                switch (choice) {
                    case 1:
                        addBook(sc, library);
                        break;
                    case 2:
                        System.out.print("Search title/author (press Enter to view all): ");
                        library.viewBooks(sc.nextLine());
                        break;
                    case 3:
                        addMember(sc, library);
                        break;
                    case 4:
                        System.out.print("Search name/phone (press Enter to view all): ");
                        library.viewMembers(sc.nextLine());
                        break;
                    case 5:
                        library.issueBook(readInt(sc, "Book id: "), readInt(sc, "Member id: "));
                        break;
                    case 6:
                        library.returnBook(readInt(sc, "Book id: "), readInt(sc, "Member id: "));
                        break;
                    case 7:
                        library.checkAvailability(readInt(sc, "Book id: "));
                        break;
                    case 8:
                        System.out.println("Goodbye.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n=================================");
        System.out.println("     LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=================================");
        System.out.println("1. Add Book");
        System.out.println("2. View/Search Books");
        System.out.println("3. Add Member");
        System.out.println("4. View/Search Members");
        System.out.println("5. Issue Book");
        System.out.println("6. Return Book");
        System.out.println("7. Check Book Availability");
        System.out.println("8. Exit");
    }

    private static void addBook(Scanner sc, Library library) throws SQLException {
        System.out.print("Title: ");
        String title = sc.nextLine();
        System.out.print("Author: ");
        String author = sc.nextLine();
        int quantity = readInt(sc, "Quantity: ");

        if (title.isEmpty() || author.isEmpty() || quantity < 0) {
            System.out.println("Invalid book details.");
            return;
        }

        library.addBook(new Book(0, title, author, quantity));
    }

    private static void addMember(Scanner sc, Library library) throws SQLException {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Phone: ");
        String phone = sc.nextLine();

        if (name.isEmpty() || phone.isEmpty()) {
            System.out.println("Invalid member details.");
            return;
        }

        library.addMember(new Member(0, name, phone));
    }

    private static int readInt(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }
}
