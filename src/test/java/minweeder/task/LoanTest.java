package minweeder.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LoanTest {

    @Test
    public void toFileString_lentLoan_returnsCorrectFormat() {
        Loan loan = new Loan("Alice", 50.0, LoanType.LENT);

        assertEquals("L | 0 | Alice | LENT | 50.0", loan.toFileString());
    }

    @Test
    public void toFileString_borrowedLoanMarkedDone_returnsCorrectFormat() {
        Loan loan = new Loan("Bob", 12.5, LoanType.BORROWED);
        loan.mark();

        assertEquals("L | 1 | Bob | BORROWED | 12.5", loan.toFileString());
    }

    @Test
    public void toString_lentLoan_returnsDisplayFormat() {
        Loan loan = new Loan("Alice", 50.0, LoanType.LENT);

        assertEquals("[L][ ] Lent $50.00 to Alice", loan.toString());
    }

    @Test
    public void toString_borrowedLoan_returnsDisplayFormat() {
        Loan loan = new Loan("Bob", 12.5, LoanType.BORROWED);

        assertEquals("[L][ ] Borrowed $12.50 from Bob", loan.toString());
    }
}
