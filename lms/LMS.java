package lms;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LMS {

    private List<Book> books = new ArrayList<>();
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();
    private String stateFilePath;

    public LMS(String stateFilePath) {
        this.stateFilePath = stateFilePath;
        loadState();
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void addBook(Book book) {
        books.add(book);
        saveState();
    }

    public boolean removeBook(Book book) {
        boolean removed = books.remove(book);
        if (removed) {
            saveState();
        }
        return removed;
    }

    public boolean borrowBook(Book book, Student student) {
        if (books.contains(book) && !isBookBorrowed(book)) {
            borrowedBooks.add(new BorrowedBook(book, student));
            saveState();
            return true;
        }
        return false;
    }

    public boolean returnBook(Book book) {
        for (BorrowedBook borrowedBook : borrowedBooks) {
            if (borrowedBook.getBook().equals(book)) {
                borrowedBooks.remove(borrowedBook);
                saveState();
                return true;
            }
        }
        return false;
    }

    private boolean isBookBorrowed(Book book) {
        for (BorrowedBook borrowedBook : borrowedBooks) {
            if (borrowedBook.getBook().equals(book)) {
                return true;
            }
        }
        return false;
    }

    private void saveState() {
        File tempFile = new File(stateFilePath + ".tmp");
        File stateFile = new File(stateFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            for (Book book : books) {
                writer.write("BOOK|" + book.getTitle() + "|" + book.getAuthor());
                writer.newLine();
            }
            for (BorrowedBook borrowedBook : borrowedBooks) {
                writer.write("BORROWED|" + borrowedBook.getBook().getTitle() + "|" + borrowedBook.getBook().getAuthor()
                        +
                        "|" + borrowedBook.getStudent().getName() + "|" + borrowedBook.getStudent().getSurname() +
                        "|" + borrowedBook.getStudent().getPersonalNumber());
                writer.newLine();
            }
            writer.flush();
            tempFile.renameTo(stateFile);
        } catch (IOException e) {
            System.err.println("Error saving state: " + e.getMessage());
        }
    }

    private void loadState() {
        File stateFile = new File(stateFilePath);
        if (!stateFile.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(stateFile))) {
            String line;
            books.clear();
            borrowedBooks.clear();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts[0].equals("BOOK")) {
                    books.add(new Book(parts[1], parts[2]));
                } else if (parts[0].equals("BORROWED")) {
                    Book book = new Book(parts[1], parts[2]);
                    Student student = new Student(parts[3], parts[4], parts[5]);
                    borrowedBooks.add(new BorrowedBook(book, student));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading state: " + e.getMessage());
        }
    }

    public static class BorrowedBook {
        private Book book;
        private Student student;

        public BorrowedBook(Book book, Student student) {
            this.book = book;
            this.student = student;
        }

        public Book getBook() {
            return book;
        }

        public Student getStudent() {
            return student;
        }
    }
}
