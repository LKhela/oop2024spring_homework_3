package lms;

public class LMSTester {
    public static void main(String[] args) {
        String filePath = "lms/libraryState.txt";
        LMS iliauniLibrary = new LMS(filePath);

        Book lor = new Book("Lord of the rings", "Tolkien");
        Book oop = new Book("OOP", "Paata Gogisvhili");
        iliauniLibrary.addBook(lor);
        iliauniLibrary.addBook(oop);

        Student gocha = new Student("Gocha", "Gegeshidze", "dfasdf");
        iliauniLibrary.borrowBook(lor, gocha);

        Student maka = new Student("Maka", "Lobjanidze", "3421325");
        iliauniLibrary.borrowBook(oop, maka);

        LMS loadedLibrary = new LMS(filePath);

        System.out.println("Books:");
        for (Book book : loadedLibrary.getBooks()) {
            System.out.println("Book: " + book.getTitle() + " by " + book.getAuthor());
        }

        System.out.println("Borrowed Books:");
        for (LMS.BorrowedBook borrowedBook : loadedLibrary.getBorrowedBooks()) {
            System.out.println(
                    "Book: " + borrowedBook.getBook().getTitle() + " by " + borrowedBook.getBook().getAuthor() +
                            " borrowed by " + borrowedBook.getStudent().getName() + " "
                            + borrowedBook.getStudent().getSurname());
        }
    }
}
