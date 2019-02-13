import java.util.Scanner;

/**
 * 
 */

/**
 * @author Oluwasayo Iroko
 *
 */
public class Q1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Display Information
		System.out.println("This Program Calculates The Future Investment Value" + " of Investment.\n");

		// create Scanner
		Scanner input = new Scanner(System.in);

		// Prompt user to input parameters
		System.out.println("Enter investment amount:");
		double investmentAmount = input.nextDouble();
		System.out.println("Enter annual interest rate in percentage:");
		double monthlyInterestRate = input.nextDouble();
		System.out.println("Enter number of years:");
		double numberOfYears = input.nextDouble();

		// calculate s using futureInvestmentValue =
		// investmentAmount x (1 + monthlyInterestRate)^numberOfYears*12
		double futureInvestmentValue = investmentAmount
				* Math.pow((1 + (monthlyInterestRate / 1200)), (numberOfYears * 12));
		// format futureInvestmentValue to two decimal places
		futureInvestmentValue = (int) (futureInvestmentValue * 100) / 100.0;

		// display the result
		System.out.println("Accumulated value is $" + futureInvestmentValue + "\n");
	}

}
