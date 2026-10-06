import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Library {
    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getQuantity());
            ps.executeUpdate();
            System.out.println("Book added successfully.");
        }
    }

    public void viewBooks(String search) throws SQLException {
        boolean hasSearch = search != null && !search.trim().isEmpty();
        String sql = hasSearch ? "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? ORDER BY id" : "SELECT * FROM books ORDER BY id";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (hasSearch) {
                ps.setString(1, "%" + search.trim() + "%");
                ps.setString(2, "%" + search.trim() + "%");
            }

            ResultSet rs = ps.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(rs.getInt("id") + " | " + rs.getString("title") + " | " + rs.getString("author") + " | Quantity: " + rs.getInt("quantity"));
            }
            if (!found) {
                System.out.println("No books found.");
            }
        }
    }

    public void addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (name, phone) VALUES (?, ?)";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getPhone());
            ps.executeUpdate();
            System.out.println("Member added successfully.");
        }
    }

    public void viewMembers(String search) throws SQLException {
        boolean hasSearch = search != null && !search.trim().isEmpty();
        String sql = hasSearch ? "SELECT * FROM members WHERE name LIKE ? OR phone LIKE ? ORDER BY id" : "SELECT * FROM members ORDER BY id";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (hasSearch) {
                ps.setString(1, "%" + search.trim() + "%");
                ps.setString(2, "%" + search.trim() + "%");
            }

            ResultSet rs = ps.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(rs.getInt("id") + " | " + rs.getString("name") + " | " + rs.getString("phone"));
            }
            if (!found) {
                System.out.println("No members found.");
            }
        }
    }

    public void issueBook(int bookId, int memberId) throws SQLException {
        try (Connection con = Database.getConnection()) {
            if (!bookExists(con, bookId)) {
                System.out.println("Book not found.");
                return;
            }
            if (!memberExists(con, memberId)) {
                System.out.println("Member not found.");
                return;
            }
            if (getBookQuantity(con, bookId) == 0) {
                System.out.println("Book is currently unavailable.");
                return;
            }

            con.setAutoCommit(false);
            try (PreparedStatement issue = con.prepareStatement("INSERT INTO issues (book_id, member_id) VALUES (?, ?)");
                 PreparedStatement update = con.prepareStatement("UPDATE books SET quantity = quantity - 1 WHERE id = ?")) {
                issue.setInt(1, bookId);
                issue.setInt(2, memberId);
                issue.executeUpdate();
                update.setInt(1, bookId);
                update.executeUpdate();
                con.commit();
                System.out.println("Book issued successfully.");
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public void returnBook(int bookId, int memberId) throws SQLException {
        try (Connection con = Database.getConnection()) {
            if (!bookExists(con, bookId)) {
                System.out.println("Book not found.");
                return;
            }
            if (!memberExists(con, memberId)) {
                System.out.println("Member not found.");
                return;
            }

            int issueId = findActiveIssue(con, bookId, memberId);
            if (issueId == 0) {
                System.out.println("No active issue record found.");
                return;
            }

            con.setAutoCommit(false);
            try (PreparedStatement mark = con.prepareStatement("UPDATE issues SET returned = TRUE WHERE id = ?");
                 PreparedStatement update = con.prepareStatement("UPDATE books SET quantity = quantity + 1 WHERE id = ?")) {
                mark.setInt(1, issueId);
                mark.executeUpdate();
                update.setInt(1, bookId);
                update.executeUpdate();
                con.commit();
                System.out.println("Book returned successfully.");
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public void checkAvailability(int bookId) throws SQLException {
        String sql = "SELECT title, quantity FROM books WHERE id = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println(rs.getString("title") + " - Available copies: " + rs.getInt("quantity"));
            } else {
                System.out.println("Book not found.");
            }
        }
    }

    private boolean bookExists(Connection con, int bookId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT id FROM books WHERE id = ?")) {
            ps.setInt(1, bookId);
            return ps.executeQuery().next();
        }
    }

    private boolean memberExists(Connection con, int memberId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT id FROM members WHERE id = ?")) {
            ps.setInt(1, memberId);
            return ps.executeQuery().next();
        }
    }

    private int getBookQuantity(Connection con, int bookId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT quantity FROM books WHERE id = ?")) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("quantity") : 0;
        }
    }

    private int findActiveIssue(Connection con, int bookId, int memberId) throws SQLException {
        String sql = "SELECT id FROM issues WHERE book_id = ? AND member_id = ? AND returned = FALSE LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setInt(2, memberId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("id") : 0;
        }
    }
}
